package org.cloudbees.sdk.plugins.oauth.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * @author Kohsuke Kawaguchi
 */
public class ApplicationList {
    @JsonProperty
    public List<Application> applications;
}
