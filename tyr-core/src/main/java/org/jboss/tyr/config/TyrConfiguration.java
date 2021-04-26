package org.jboss.tyr.config;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;

@ConfigProperties(prefix = "tyr")
public interface TyrConfiguration {

    @ConfigProperty(name = "template.format.url")
    Optional<String> formatFileUrl();

    @ConfigProperty(name = "template.format.file")
    Optional<String> configFileName();

    @ConfigProperty(name = "whitelist.enabled", defaultValue = "false")
    boolean whitelistEnabled();

    @ConfigProperty(name = "github.oauth.token")
    String oauthToken();

    @ConfigProperty(name = "github.status.push", defaultValue = "true")
    boolean statusPush();

    @ConfigProperty(name = "github.commit.check.disabled", defaultValue = "false")
    boolean disableCommitCheck();

    @ConfigProperty(name = "users.dir")
    Optional<String> whitelistDir();

    @ConfigProperty(name = "additional-resources")
    Optional<String> additionalResources();
}
