package org.cloudbees.sdk.plugins.oauth.token;

import com.cloudbees.api.BeesClient;
import com.cloudbees.api.oauth.OauthClient;
import org.cloudbees.sdk.plugins.oauth.app.AbstractOAuthCommand;

import java.io.IOException;

/**
 * @author Kohsuke Kawaguchi
 */
public abstract class AbstractTokenCommand extends AbstractOAuthCommand {
    protected OauthClient createClient() throws IOException {
        return factory.get(BeesClient.class).getOauthClient();
    }
}
