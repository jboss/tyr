/*
 * Copyright 2019 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.tyr.model;

import org.jboss.logging.Logger;
import org.jboss.tyr.Check;
import org.jboss.tyr.Command;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

@ApplicationScoped
public class AdditionalResourcesLoader {

    private static final Logger log = Logger.getLogger(AdditionalResourcesLoader.class);

    private URL[] jarURLs;
    private List<Check> additionalChecks;
    private List<Command> additionaCommands;

    @Inject
    TyrConfiguration configuration;

    @PostConstruct
    public void init() {
        jarURLs = loadAdditionalJars();
        additionalChecks = loadAdditionalResource(Check.class);
        additionaCommands = loadAdditionalResource(Command.class);
    }

    public List<Check> getAdditionalChecks() {
        return additionalChecks;
    }

    public List<Command> getAdditionalCommands() {
        return additionaCommands;
    }

    private URL[] loadAdditionalJars() {
        if (!configuration.additionalResources().isPresent()) {
            return new URL[0];
        }

        String[] split = configuration.additionalResources().get().split(",");

        List<URL> jarURLs = new ArrayList<>();
        for (String jar : split) {
            File file = new File(jar);

            if (!file.exists()) {
                log.warn("File declared for additional resources does not exist: " + file.getPath());
                continue;
            }

            if (!file.getPath().toLowerCase().endsWith(".jar")) {
                log.warn("Invalid file included for additional resources must be a jar archive: " + file.getPath());
                continue;
            }

            try {
                jarURLs.add(file.toURI().toURL());
            } catch (MalformedURLException e) {
                log.warn("Invalid file name value passed in additional resources", e);
            }
        }

        return jarURLs.toArray(new URL[0]);
    }

    private <S> List<S> loadAdditionalResource(Class<S> clazz) {
        List<S> result = new ArrayList<>();

        ServiceLoader<S> serviceLoader = ServiceLoader.load(clazz,
            new URLClassLoader(jarURLs, Thread.currentThread().getContextClassLoader()));
        serviceLoader.iterator().forEachRemaining(result::add);

        return result;
    }
}
