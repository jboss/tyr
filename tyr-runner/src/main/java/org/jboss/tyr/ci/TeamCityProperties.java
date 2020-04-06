package org.jboss.tyr.ci;

import io.quarkus.arc.config.ConfigProperties;

import java.util.Optional;

@ConfigProperties(prefix = "tyr.teamcity")
public interface TeamCityProperties {
    Optional<String> host();
    Optional<Integer> port();
    Optional<String> user();
    Optional<String> password();
    Optional<String> mapping();
}
