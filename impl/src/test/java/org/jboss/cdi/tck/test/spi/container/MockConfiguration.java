package org.jboss.cdi.tck.test.spi.container;

import org.jboss.arquillian.container.spi.ConfigurationException;
import org.jboss.arquillian.container.spi.client.container.ContainerConfiguration;

/**
 * A noop container configuration
 */
public class MockConfiguration implements ContainerConfiguration {
    @Override
    public void validate() throws ConfigurationException {

    }
}
