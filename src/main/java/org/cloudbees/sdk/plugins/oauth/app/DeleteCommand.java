package org.cloudbees.sdk.plugins.oauth.app;

import com.cloudbees.api.oauth.OauthClient;
import com.cloudbees.api.oauth.OauthClientApplication;
import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.kohsuke.args4j.Argument;

import java.util.ArrayList;
import java.util.List;

/**
 * Delete a registered application
 *
 * @author Kohsuke Kawaguchi
 */
@BeesCommand(group="OAuth",description="Delete a registered application")
@CLICommand("oauth:app:delete")
public class DeleteCommand extends AbstractOAuthCommand {
    @Argument(index=0,metaVar="ClientID",usage="OAuth Client IDs")
    List<String> clientIds = new ArrayList<String>();

    @Override
    public int main() throws Exception {
        OauthClient oac = createClient();

        if (clientIds.size()>0) {
            for (String clientId : clientIds) {
                oac.deleteApplication(clientId);
            }
        } else {
            List<String> ids = new ArrayList<String>();
            for (OauthClientApplication app : oac.listApplication()) {
                ids.add(app.client_id);
                System.out.printf("%d : %s (%s)\n", ids.size(), app.name, app.app_url);
            }
            System.out.printf("Which one to delete? (1-%d): ", ids.size());
            int i = Integer.parseInt(System.console().readLine());
            oac.deleteApplication(ids.get(i-1));
        }
        return 0;
    }
}
