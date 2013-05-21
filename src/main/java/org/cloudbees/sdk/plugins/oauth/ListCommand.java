package org.cloudbees.sdk.plugins.oauth;

import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Kohsuke Kawaguchi
 */
@BeesCommand(group="OAuth",description="List all the reigstered OAuth applications")
@CLICommand("oauth:list")
public class ListCommand extends AbstractOAuthCommand {
    @Override
    public int main() throws Exception {
        HttpURLConnection con = makeGetRequest(new URL("https://grandcentral.cloudbees.com/api/v2/applications/"));
        return dumpResponse(con);
    }
}
