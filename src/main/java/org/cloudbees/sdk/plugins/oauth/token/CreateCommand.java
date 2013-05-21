package org.cloudbees.sdk.plugins.oauth.token;

import com.cloudbees.api.BeesClient;
import com.cloudbees.api.oauth.OauthClient;
import com.cloudbees.api.oauth.OauthToken;
import com.cloudbees.api.oauth.TokenRequest;
import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.kohsuke.args4j.Option;

import java.io.IOException;
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

    @Option(name="-scope",usage="OAuth scopes that the generated token will have access. Can be specified multiple times.")
    List<String> scopes = new ArrayList<String>();

    @Option(name="--account",usage="Account in which the app gets registered.")
    public String account;

    @Override
    public int main() throws Exception {
        String account = promptAccount(this.account, "Account that the generated token will grant access to.");
        TokenRequest req = new TokenRequest(note,noteUrl,null,account,scopes.toArray(new String[scopes.size()]));

        OauthClient oac = createClient();
        OauthToken token = oac.createToken(req);
        if (token==null) {
            System.err.println("Failed to create a token");
            return 1;
        }

        om.writeValue(System.out,token);

        return 0;
    }

    private OauthClient createClient() throws IOException {
        return factory.get(BeesClient.class).getOauthClient();
    }
}
