package org.cloudbees.sdk.plugins.oauth.token;

import com.cloudbees.api.oauth.OauthClient;
import com.cloudbees.api.oauth.OauthTokenDetail;
import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.apache.commons.io.output.CloseShieldOutputStream;

/**
 * @author Kohsuke Kawaguchi
 */
@BeesCommand(group="OAuth",description="List all the registered OAuth applications")
@CLICommand("oauth:token:list")
public class ListCommand extends AbstractTokenCommand {
    @Override
    public int main() throws Exception {
        OauthClient oac = createClient();
        for (OauthTokenDetail d : oac.listTokens()) {
            om.writeValue(new CloseShieldOutputStream(System.out), d);
            System.out.println();
        }
        return 0;
    }
}
