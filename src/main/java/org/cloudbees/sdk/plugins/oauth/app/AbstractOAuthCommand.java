package org.cloudbees.sdk.plugins.oauth.app;

import com.cloudbees.api.BeesClient;
import com.cloudbees.api.CBAccount;
import com.cloudbees.api.CBUser;
import com.cloudbees.api.oauth.OauthClient;
import com.cloudbees.sdk.cli.AbstractCommand;
import com.cloudbees.sdk.cli.BeesClientFactory;
import org.apache.commons.io.output.CloseShieldOutputStream;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.kohsuke.args4j.Option;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Kohsuke Kawaguchi
 */
public abstract class AbstractOAuthCommand extends AbstractCommand {
    @Inject
    protected BeesClientFactory factory;

    protected String getDefaultAccount() {
        return factory.getConfigProperties().getProperty("bees.project.app.domain");
    }

    protected OauthClient createClient() throws IOException {
        return factory.get(BeesClient.class).getOauthClient();
    }

    protected  <T> T prompt(Class<T> type, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f = getClass().getDeclaredField(fieldName);
        Object r = f.get(this);
        if (r!=null)    return type.cast(r);    // value already present

        System.out.println(f.getAnnotation(Option.class).usage());
        System.out.print(": ");
        return type.cast(System.console().readLine());
    }

    protected static ObjectMapper om = new ObjectMapper();

    static {
        om.configure(Feature.AUTO_DETECT_FIELDS, false);
        om.configure(Feature.AUTO_DETECT_SETTERS, false);
        om.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
    }

    // TODO: push this down to some common code as it's useful elsewhere
    protected String promptAccount(String valueSpecifiedByOption, String prompt) throws IOException {
        if (valueSpecifiedByOption!=null)   return valueSpecifiedByOption; // this takes the most precedence

        BeesClient bees = factory.get(BeesClient.class);
        CBUser self = bees.getSelfUser();
        if (self.accounts.size()==1)
            return self.accounts.get(0).name;   // only one valid account

        Set<String> names = new TreeSet<String>();
        for (CBAccount a : self.accounts) {
            names.add(a.name);
        }

        String defaultValue = getDefaultAccount();
        if (!names.contains(defaultValue))
            defaultValue = null;

        if (defaultValue!=null)
            prompt += " Default = "+defaultValue;

        while (true) {
            System.out.println(prompt);
            System.out.print(": ");
            String v = System.console().readLine();

            if (v.isEmpty()) {
                if (defaultValue!=null)
                    return defaultValue;
            } else {
                if (names.contains(v))
                    return v;
            }
        }
    }

    protected void prettyPrint(Object jacksonBoundObject) throws IOException {
        om.writeValue(new CloseShieldOutputStream(System.out), jacksonBoundObject);
        System.out.println();
    }
}

