package org.cloudbees.sdk.plugins.oauth.app;

import com.cloudbees.api.oauth.GrantType;
import com.cloudbees.api.oauth.OauthClientApplication;
import com.cloudbees.api.oauth.ScopeDefinition;
import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.MapOptionHandler;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Register new OAuth application
 *
 * @author Kohsuke Kawaguchi
 */
@BeesCommand(group="OAuth",description="Register new OAuth application")
@CLICommand("oauth:app:register")
public class RegisterCommand extends AbstractOAuthCommand {
    @Option(name="-n",usage="Application Name. This name will be displayed on the authorization screen.")
    public String name;

    @Option(name="--callback",usage="Absolute HTTPS URL where CloudBees authorization server will redirect during OAuth2 authorization.")
    public String callback_uri;

    @Option(name="--url",usage="Absolute URL of your application.")
    public String app_url;

    @Option(name="--account",usage="Account in which the app gets registered.")
    public String account;

    @Option(name="--grant-type",metaVar="[authorization_code|client_credentials|all]",
            usage="Token issuance mode allowed for this application. Multiple options are allowed. If you are unsure, request all grant types by passing 'all'")
    public List<String> grantTypes = new ArrayList<String>();


    @Option(name="-S",usage="Register OAuth scope parameters. -S name=https://acme.com/scope1 -S display_name=\"My secured OAuth app\"", handler = MapOptionHandler.class, metaVar = "name=SCOPE_URL\n   display_name=USER_VISIBLE_NAME")
    Map<String,String> scopeParams = new HashMap<String,String>();


    @Override
    public int main() throws Exception {
        OauthClientApplication reg = new OauthClientApplication();
        reg.name = prompt(String.class,"name");
        reg.callback_uri = prompt(String.class,"callback_uri");
        reg.app_url = prompt(String.class,"app_url");
        reg.account = promptAccount(account, "Account in which the app gets registered.");

        if (grantTypes.isEmpty()) {
            reg.grant_types.add(GrantType.AUTHORIZATION_CODE);
        } else {
            for (String gt : grantTypes) {
                if (gt.equals("all")) {
                    reg.grant_types.addAll(EnumSet.allOf(GrantType.class));
                    continue;
                }
                GrantType gtv = GrantType.parse(gt);
                if (gtv==null)  throw new IllegalArgumentException("Invalid grant type: "+gt);
                reg.grant_types.add(gtv);
            }
        }

        if(scopeParams.size() > 0){
            if(scopeParams.get("name") == null || scopeParams.get("display_name") == null ){
                throw new RuntimeException("Both name and display_name must be present with -S option");
            }
            ScopeDefinition scopeDefinition = new ScopeDefinition();
            scopeDefinition.name = scopeParams.get("name");
            scopeDefinition.display_name = scopeParams.get("display_name");
            reg.scopes.add(scopeDefinition);
        }

        OauthClientApplication r = createClient().registerApplication(reg);
        prettyPrint(r);

        return 0;
    }

}
