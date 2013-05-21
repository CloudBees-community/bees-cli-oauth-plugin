package org.cloudbees.sdk.plugins.oauth.token;

import com.cloudbees.api.BeesClient;
import com.cloudbees.api.oauth.OauthClientException;
import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.cloudbees.sdk.plugins.oauth.app.AbstractOAuthCommand;
import org.kohsuke.args4j.Argument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kohsuke Kawaguchi
 */
@BeesCommand(group="OAuth",description="Deletes a token")
@CLICommand("oauth:token:delete")
public class DeleteCommand extends AbstractOAuthCommand {
    @Argument(index=0,metaVar="TOKEN",usage="OAuth tokens")
    List<String> tokens = new ArrayList<String>();

    @Override
    public int main() throws Exception {
        for (String token : tokens) {
            delete(token);
        }
        return 0;   // all success
    }

    private void delete(String token) throws OauthClientException, IOException {
        factory.get(BeesClient.class).getOauthClient().deleteToken(token);
    }
}
