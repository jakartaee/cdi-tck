package org.jboss.cdi.tck.tests.full.decorators.lookup.clientProxy.unproxyable.decorator;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.UNPROXYABLE;

import jakarta.enterprise.inject.spi.DeploymentException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class DecoratoredBeanProxyTest extends AbstractTest {


	@Deployment
	@ShouldThrowException(DeploymentException.class)
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage(DecoratoredBeanProxyTest.class)
				.withBeansXml(new BeansXml().decorators(MarineDecorator.class))
				.build();
	}

	@Test(groups = CDI_FULL)
	@SpecAssertions({ @SpecAssertion(section = UNPROXYABLE, id = "ea") })
	public void testClientProxyBeanWithAssociatedDecorator() {
		
	}
}
