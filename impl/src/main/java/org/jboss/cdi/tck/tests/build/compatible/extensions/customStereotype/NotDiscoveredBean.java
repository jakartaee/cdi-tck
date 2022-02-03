package org.jboss.cdi.tck.tests.build.compatible.extensions.customStereotype;

import jakarta.enterprise.context.Dependent;

/**
 * Deployment for this test contains only one archive with no beans.xml and an extension.
 * For some EE containers this might trigger an optimization where they do not bootstrap CDI container.
 * Presence of this CDI-annotated class can help prevent it.
 *
 * Due to the archive not being a bean archive, this class should not be discovered as a bean despite having bean
 * defining annotation.
 */
@Dependent
public class NotDiscoveredBean {
}
