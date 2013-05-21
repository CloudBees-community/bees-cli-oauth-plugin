package org.cloudbees.sdk.plugins.oauth.token;

import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Kohsuke Kawaguchi
 */
@BeesCommand(group="OAuth",description="List all the registered OAuth applications")
@CLICommand("oauth:token:list")
public class ListCommand extends AbstractTokenCommand {
    @Override
    public int main() throws Exception {
        HttpURLConnection con = makeGetRequest(new URL("https://grandcentral.cloudbees.com/api/v2/authorizations/"));
        return dumpResponse(con);
    }
}
