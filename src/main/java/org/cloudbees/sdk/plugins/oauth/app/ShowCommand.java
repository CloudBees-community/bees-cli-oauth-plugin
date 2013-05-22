package org.cloudbees.sdk.plugins.oauth.app;

import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.kohsuke.args4j.Argument;

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
        prettyPrint(createClient().getApplication(clientId));
        return 0;
    }
}
