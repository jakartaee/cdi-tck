package org.jboss.cdi.tck.tests.implementation.producer.method.broken.parameterizedTypeWithWildcard;

import static org.jboss.cdi.tck.cdi.Sections.LEGAL_BEAN_TYPES;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_METHOD;

import javax.enterprise.inject.spi.DefinitionException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class ParametrizedTypeWithWildcard02Test extends AbstractTest {

    @ShouldThrowException(DefinitionException.class)
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(ParametrizedTypeWithWildcard02Test.class)
                .withClasses(Spiderman.class, SpidermanProducer.class, FunnelWeaver.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD, id = "ha"), @SpecAssertion(section = LEGAL_BEAN_TYPES, id = "lb") })
    public void testParameterizedReturnTypeWithDoubleWildcard() throws Exception {
    }

}

