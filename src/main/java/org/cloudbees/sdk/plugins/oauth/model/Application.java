package org.cloudbees.sdk.plugins.oauth.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * @author Kohsuke Kawaguchi
 */
public class Application {
    @JsonProperty
    public String name;

    @JsonProperty
    public String callback_uri;

    @JsonProperty
    public String app_url;

    // see https://github.com/cloudbees/grandcentral/pull/1
//    @JsonProperty
//    public String grant_type = "client_credentials";

    /**
     * If your user ID belongs to multiple accounts,
     * specify which account this app is registered under.
     */
    @JsonProperty
    public String account;

    @JsonProperty
    public List<Scope> scopes;

    @JsonProperty
    public String client_id;

    @JsonProperty
    public String client_secret;
}
