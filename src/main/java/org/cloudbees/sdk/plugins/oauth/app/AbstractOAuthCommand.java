package org.cloudbees.sdk.plugins.oauth.app;

import com.cloudbees.api.BeesClient;
import com.cloudbees.api.BeesClientConfiguration;
import com.cloudbees.api.CBAccount;
import com.cloudbees.api.CBUser;
import com.cloudbees.sdk.cli.AbstractCommand;
import com.cloudbees.sdk.cli.BeesClientFactory;
import com.ning.http.util.Base64;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.kohsuke.args4j.Option;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
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

    protected  <T> T prompt(Class<T> type, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f = getClass().getDeclaredField(fieldName);
        Object r = f.get(this);
        if (r!=null)    return type.cast(r);    // value already present

        System.out.println(f.getAnnotation(Option.class).usage());
        System.out.print(": ");
        return type.cast(System.console().readLine());
    }

    protected void prettyPrint(InputStream json, OutputStream out) throws IOException {
        JsonNode j = om.readTree(json);
        om.writeValue(out,j);
    }

    protected HttpURLConnection makeGetRequest(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BeesClientConfiguration config = factory.createConfigurations();
        con.setRequestProperty("Authorization", "Basic " + Base64.encode((config.getApiKey() + ":" + config.getSecret()).getBytes("UTF-8")));
        return con;
    }

    protected HttpURLConnection makeDeleteRequest(URL url) throws IOException {
        HttpURLConnection con = makeGetRequest(url);
        con.setRequestMethod("DELETE");
        return con;
    }

    protected HttpURLConnection makePostRequest(URL url) throws IOException {
        HttpURLConnection con = makeGetRequest(url);
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type","application/json");
        return con;
    }

    protected int dumpResponse(HttpURLConnection con) throws IOException {
        int rc = con.getResponseCode();

        if (rc/100==2) {
            prettyPrint(con.getInputStream(), System.out);
            return 0;
        } else {
            IOUtils.copy(con.getErrorStream(), System.out);
            return rc;
        }
    }

    protected static ObjectMapper om = new ObjectMapper();

    static {
        om.configure(Feature.AUTO_DETECT_FIELDS, false);
        om.configure(Feature.AUTO_DETECT_SETTERS, false);
        om.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
    }

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
}

