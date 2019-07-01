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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static org.jboss.tyr.model.Utils.ADDITIONAL_RESOURCES_PROPERTY;

public class AdditionalResourcesLoader {

    private static final Logger log = Logger.getLogger(AdditionalResourcesLoader.class);

    public static List<Check> loadAdditionalChecks() {
        return loadAdditionalResource(Check.class);
    }

    public static List<Command> loadAdditionalCommands() {
        return loadAdditionalResource(Command.class);
    }

    private static <S> List<S> loadAdditionalResource(Class<S> clazz) {
        String additionalCheckPropertyValue = System.getProperty(ADDITIONAL_RESOURCES_PROPERTY);
        List<S> result = new ArrayList<>();

        if (additionalCheckPropertyValue == null) {
            return result;
        }

        String[] split = additionalCheckPropertyValue.split(",");

        List<URL> jarURLs = new ArrayList<>();
        for (String jar : split) {
            File file = new File(jar);

            if (!file.exists()) {
                log.warn("File declared for " + ADDITIONAL_RESOURCES_PROPERTY + " does not exist: " + file.getPath());
                continue;
            }

            if (!file.getPath().toLowerCase().endsWith(".jar")) {
                log.warn("Invalid file included for " + ADDITIONAL_RESOURCES_PROPERTY + ", must be a jar archive: " + file.getPath());
                continue;
            }

            try {
                jarURLs.add(file.toURI().toURL());
            } catch (MalformedURLException e) {
                log.warn("Invalid file name value passed in " + ADDITIONAL_RESOURCES_PROPERTY, e);
            }
        }

        ServiceLoader<S> serviceLoader = ServiceLoader.load(clazz,
                new URLClassLoader(jarURLs.toArray(new URL[0]), Thread.currentThread().getContextClassLoader()));
        serviceLoader.iterator().forEachRemaining(result::add);

        return result;
    }
}
