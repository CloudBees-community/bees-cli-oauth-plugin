package org.cloudbees.sdk.plugins.oauth.token;

import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.kohsuke.args4j.Argument;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kohsuke Kawaguchi
 */
@BeesCommand(group="OAuth",description="Deletes a token")
@CLICommand("oauth:token:delete")
public class DeleteCommand extends AbstractTokenCommand {
    @Argument(index=0,metaVar="TOKEN",usage="OAuth tokens")
    List<String> tokens = new ArrayList<String>();

    @Override
    public int main() throws Exception {
        for (String token : tokens) {
            createClient().deleteToken(token);
        }
        return 0;   // all success
    }
}
