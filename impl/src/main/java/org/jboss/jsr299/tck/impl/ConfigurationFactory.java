package org.jboss.jsr299.tck.impl;

import org.jboss.jsr299.tck.api.JSR299Configuration;

/**
 * Pulled from jboss-test-harness.
 * 
 * @author Martin Kouba
 */
public final class ConfigurationFactory {

    private static JSR299Configuration current;

    private ConfigurationFactory() {
    }

    /**
     * @param minimal Minimal configuration includes simple properties only (no SPI instances)
     * @return current JSR299 configuration
     */
    public static JSR299Configuration get(boolean deploymentPhase) {

        if (current == null) {
            try {
                current = new JSR299PropertiesBasedConfigurationBuilder().init(deploymentPhase).getJsr299Configuration();
            } catch (Exception e) {
                throw new IllegalStateException("Unable to get configuration", e);
            }
        }
        return current;
    }

    /**
     * @return current JSR299 configuration
     */
    public static JSR299Configuration get() {
        return get(false);
    }

}
