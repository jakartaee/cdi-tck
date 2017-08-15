/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.impl;

import org.jboss.cdi.tck.api.Configuration;

/**
 * Pulled from jboss-test-harness.
 *
 * @author Martin Kouba
 */
public final class ConfigurationFactory {

    private static Configuration current;

    private ConfigurationFactory() {
    }

    /**
     * @param deploymentPhase Deployment phase (building test archive) initialization includes deployment specific properties
     * @return current JSR 365 TCK configuration
     */
    public static Configuration get(boolean deploymentPhase) {

        if (current == null) {
            try {
                current = new PropertiesBasedConfigurationBuilder().init(deploymentPhase).getConfiguration();
            } catch (Exception e) {
                throw new IllegalStateException("Unable to get configuration", e);
            }
        }
        return current;
    }

    /**
     * @return current JSR 365 TCK configuration
     */
    public static Configuration get() {
        return get(false);
    }

}
