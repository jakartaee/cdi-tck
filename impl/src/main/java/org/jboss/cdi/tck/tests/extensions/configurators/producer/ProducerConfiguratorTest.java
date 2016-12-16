/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.configurators.producer;

import static org.jboss.cdi.tck.cdi.Sections.PROCESS_PRODUCER;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_CONFIGURATOR;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@Test
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class ProducerConfiguratorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProducerConfiguratorTest.class)
            .withExtension(ProducerConfiguringExtension.class)
            .build();
    }

    @Inject
    SomeBeanWithInjectionPoints bean;

    @Inject
    @Another
    Instance<Bar> barInstance;

    @Test
    @SpecAssertions({
        @SpecAssertion(section = PROCESS_PRODUCER, id = "f"),
        @SpecAssertion(section = PRODUCER_CONFIGURATOR, id = "a"),
        @SpecAssertion(section = PRODUCER_CONFIGURATOR, id = "b"),
        @SpecAssertion(section = PRODUCER_CONFIGURATOR, id = "c"),
        @SpecAssertion(section = PRODUCER_CONFIGURATOR, id = "d"),
        @SpecAssertion(section = PRODUCER_CONFIGURATOR, id = "e") })
    public void configuratorOptionsTest() {
        // Just by deploying this, we are making sure that all IPs within SomeBeanWithInjectionPoints are satisfied
        // Now check then one by one for the annotation with which they were created

        // should have @YetAnother annotation
        assertEquals(bean.getBarOne().getAnnotationWithWhichBarWasProduced().getSimpleName(), YetAnother.class.getSimpleName());

        // should have @Some annotation
        assertEquals(bean.getBarTwo().getAnnotationWithWhichBarWasProduced().getSimpleName(), Some.class.getSimpleName());
        assertEquals(bean.getBarThree().getAnnotationWithWhichBarWasProduced().getSimpleName(), Some.class.getSimpleName());

        //should have @OneAmongMany annotation
        assertEquals(bean.getBarFour().getAnnotationWithWhichBarWasProduced().getSimpleName(), OneAmongMany.class.getSimpleName());
        assertEquals(bean.getBarFive().getAnnotationWithWhichBarWasProduced().getSimpleName(), OneAmongMany.class.getSimpleName());

        // should have @Default annotation
        assertEquals(bean.getBarSix().getAnnotationWithWhichBarWasProduced().getSimpleName(), Default.class.getSimpleName());
        assertEquals(bean.getBarSeven().getAnnotationWithWhichBarWasProduced().getSimpleName(), Default.class.getSimpleName());

        // should have @Another annotation, also has changed producer/disposer
        assertEquals(bean.getBarEight().getAnnotationWithWhichBarWasProduced().getSimpleName(), Another.class.getSimpleName());
        assertTrue(barInstance.isResolvable());
        // verify producer/disposer
        Bar actualInstance = barInstance.get();
        assertTrue(ProducerConfiguringExtension.producerCalled.get());
        barInstance.destroy(actualInstance);
        assertTrue(ProducerConfiguringExtension.disposerCalled.get());
    }
}
