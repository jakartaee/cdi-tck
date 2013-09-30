package org.jboss.cdi.tck.tests.implementation.producer.method.broken.parameterizedTypeWithTypeParameter;

import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_METHOD;

import javax.enterprise.inject.spi.DefinitionException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.testng.annotations.Test;

public class ParametrizedReturnTypeWithTypeVariable02Test extends AbstractTest {

    @ShouldThrowException(DefinitionException.class)
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(ParametrizedReturnTypeWithTypeVariable02Test.class).withClasses(DoubleListProducer.class).build();
    }

    @Test
    @SpecAssertion(section = PRODUCER_METHOD, id = "iab")
    public void testNonDependentScopedProducerMethodWithParameterizedTypeWithTypeVariable() {
    }
}
