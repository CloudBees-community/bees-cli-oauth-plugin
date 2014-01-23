package org.cloudbees.sdk.plugins.oauth.token;

import com.cloudbees.api.BeesClient;
import com.cloudbees.api.oauth.OauthClient;
import com.cloudbees.api.oauth.OauthToken;
import com.cloudbees.api.oauth.TokenRequest;
import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.kohsuke.args4j.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates a new token.
 *
 * @author Kohsuke Kawaguchi
 * @author Vivek Pandey
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

    @Option(name="-generate-refresh-token",usage="Generate refresh_token along with access_token.")
    public boolean generateRefreshToken;

    @Option(name="-clientId",usage="Generate token with clientId")
    public String clientId;

    @Option(name="-clientSecret",usage="Generate token with clientSecret")
    public String clientSecret;

    @Override
    public int main() throws Exception {
        TokenRequest req = new TokenRequest().withScopes(scopes.toArray(new String[scopes.size()])).withAccountName(account);
        req.withGenerateRequestToken(generateRefreshToken);

        OauthClient beesClient = createClient();
        OauthToken token;
        if (clientId != null && clientSecret!=null) {
            token = new BeesClient(factory.getApiUrl(),clientId,clientSecret).getOauthClient().createOAuthClientToken(req);
        }else{
            String account = promptAccount(this.account, "Account that the generated token will grant access to.");
            req.withNote(note, noteUrl).withAccountName(account);
            token = beesClient.createToken(req);
        }
        if (token == null) {
            System.err.println("Failed to create a token");
            return 1;
        }

        prettyPrint(token);

        return 0;
    }
}
