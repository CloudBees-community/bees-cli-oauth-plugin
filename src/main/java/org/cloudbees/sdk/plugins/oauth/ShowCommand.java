package org.cloudbees.sdk.plugins.oauth;

import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.kohsuke.args4j.Argument;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Dump the OAuth client application information
 *
 * @author Kohsuke Kawaguchi
 */
@BeesCommand(group="OAuth",description="Dump the OAuth client application information")
@CLICommand("oauth:app:show")
public class ShowCommand extends AbstractOAuthCommand {
    @Argument(index=0,metaVar="ClientID",usage="OAuth Client ID to dump",required=true)
    String clientId;

    @Override
    public int main() throws Exception {
        HttpURLConnection con = makeGetRequest(new URL("https://grandcentral.cloudbees.com/api/v2/applications/"+clientId));
        return dumpResponse(con);
    }
}
