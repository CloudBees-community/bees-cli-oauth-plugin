package org.cloudbees.sdk.plugins.oauth.app;

import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.cloudbees.sdk.plugins.oauth.model.Application;
import org.cloudbees.sdk.plugins.oauth.model.ApplicationList;
import org.kohsuke.args4j.Argument;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
        if (clientIds.size()>0) {
            for (String clientId : clientIds) {
                int r = delete(clientId);
                if (r!=0)
                    return r;   // failure. abort
            }
            return 0;   // all success
        } else {
            HttpURLConnection con = makeGetRequest(new URL("https://grandcentral.cloudbees.com/api/v2/applications/"));
            ApplicationList all = om.readValue(con.getInputStream(), ApplicationList.class);
            List<String> ids = new ArrayList<String>();
            for (Application app : all.applications) {
                ids.add(app.client_id);
                System.out.printf("%d : %s (%s)\n", ids.size(), app.name, app.app_url);
            }
            System.out.printf("Which one to delete? (1-%d): ", ids.size());
            int i = Integer.parseInt(System.console().readLine());
            return delete(ids.get(i-1));
        }
    }

    private int delete(String clientId) throws IOException {
        HttpURLConnection con = makeDeleteRequest(new URL("https://grandcentral.cloudbees.com/api/v2/applications/" + clientId));
        return dumpResponse(con);
    }
}
