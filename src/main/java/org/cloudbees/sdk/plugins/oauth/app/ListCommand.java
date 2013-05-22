package org.cloudbees.sdk.plugins.oauth.app;

import com.cloudbees.api.oauth.OauthClientApplication;
import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;

/**
 * List all the registered OAuth applications
 *
 * @author Kohsuke Kawaguchi
 */
@BeesCommand(group="OAuth",description="List all the registered OAuth applications")
@CLICommand("oauth:app:list")
public class ListCommand extends AbstractOAuthCommand {
    @Override
    public int main() throws Exception {
        for (OauthClientApplication d : createClient().listApplication()) {
            prettyPrint(d);
        }
        return 0;
    }

}
