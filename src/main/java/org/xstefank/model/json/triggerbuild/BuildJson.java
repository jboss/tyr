package org.xstefank.model.json.triggerbuild;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BuildJson {

    @JsonProperty("branchName")
    private String teamCityBranch;

    private BuildType buildType;
    private Properties properties;

    public BuildJson(String teamCityBranch, String id, String sha, String pull, String gitBranch) {
        this.teamCityBranch = teamCityBranch;
        buildType = new BuildType(id);
        properties = new Properties(sha, pull, gitBranch);
    }

    public String getTeamCityBranch() {
        return teamCityBranch;
    }

    public void setTeamCityBranch(String branchName) {
        this.teamCityBranch = branchName;
    }

    public BuildType getBuildType() {
        return buildType;
    }

    public void setBuildType(BuildType buildType) {
        this.buildType = buildType;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
