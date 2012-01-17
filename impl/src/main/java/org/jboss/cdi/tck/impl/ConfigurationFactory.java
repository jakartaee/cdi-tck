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
     * @return current JSR299 configuration
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
     * @return current JSR299 configuration
     */
    public static Configuration get() {
        return get(false);
    }

}
