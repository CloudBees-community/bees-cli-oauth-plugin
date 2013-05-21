package org.cloudbees.sdk.plugins.oauth.token;

import com.cloudbees.api.BeesClientConfiguration;
import com.cloudbees.oauth.OauthClient;
import com.cloudbees.oauth.OauthClientImpl;
import com.cloudbees.oauth.OauthToken;
import com.cloudbees.oauth.TokenRequest;
import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.kohsuke.args4j.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates a new token.
 * 
 * @author Kohsuke Kawaguchi
 */
@BeesCommand(group="OAuth",description="Create a new token")
@CLICommand("oauth:token:create")
public class CreateCommand extends AbstractTokenCommand {
    @Option(name="-note",usage="A short human-readable text that explains which app created this token for what.")
    String note;

    @Option(name="-note-url",usage="URL that supplements the note.")
    String noteUrl;

    @Option(name="-scope",usage="Scope")
    List<String> scopes = new ArrayList<String>();

    @Override
    public int main() throws Exception {
        TokenRequest req = new TokenRequest(note,noteUrl,null,scopes.toArray(new String[scopes.size()]));

        OauthClient oac = createClient();
        BeesClientConfiguration config = factory.createConfigurations();
        OauthToken token = oac.createToken(config.getApiKey(), config.getSecret(), req);
        if (token==null) {
            System.err.println("Failed to create a token");
            return 1;
        }

        om.writeValue(System.out,token);

        return 0;
    }

    private OauthClientImpl createClient() {
        return new OauthClientImpl();
    }
}
