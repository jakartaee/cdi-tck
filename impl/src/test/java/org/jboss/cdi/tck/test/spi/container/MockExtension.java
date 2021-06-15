package org.jboss.cdi.tck.test.spi.container;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.core.spi.LoadableExtension;

/**
 * Register the MockContainer as the arquillian DeployableContainer
 */
public class MockExtension implements LoadableExtension {
    @Override
    public void register(ExtensionBuilder builder) {
        builder.service(DeployableContainer.class, MockContainer.class);
    }
}
