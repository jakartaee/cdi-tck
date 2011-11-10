package org.jboss.jsr299.tck.api;

/**
 * SPI component that depends on JSR299Configuration.
 *
 * @author Martin Kouba
 * @deprecated Not used anymore
 */
public interface ConfigurationDependent {

    /**
     *
     * @param configuration
     */
    public void setConfiguration(JSR299Configuration configuration);

}
