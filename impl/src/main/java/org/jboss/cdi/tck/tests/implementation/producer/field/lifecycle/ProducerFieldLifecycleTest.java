/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.implementation.producer.field.lifecycle;

import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_FIELD;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_FIELDS_ACCESS;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_FIELD_LIFECYCLE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.IllegalProductException;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class ProducerFieldLifecycleTest extends AbstractTest {

    @SuppressWarnings("serial")
    private AnnotationLiteral<Null> NULL_LITERAL = new Null.Literal();
    @SuppressWarnings("serial")
    private AnnotationLiteral<Broken> BROKEN_LITERAL = new Broken.Literal();
    @SuppressWarnings("serial")
    private AnnotationLiteral<Tame> TAME_LITERAL = new Tame.Literal();

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProducerFieldLifecycleTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_FIELD, id = "a")})
    public void testProducerFieldNotAnotherBean() {
        assert getContextualReference(BrownRecluse.class) != null;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_FIELDS_ACCESS, id = "a"), @SpecAssertion(section = PRODUCER_FIELD, id = "b") })
    public void testProducerStaticFieldBean() {
        StaticTarantulaConsumer tarantulaConsumer = getContextualReference(StaticTarantulaConsumer.class);
        assert tarantulaConsumer.getConsumedTarantula().equals(StaticTarantulaProducer.produceTarantula);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_FIELDS_ACCESS, id = "b"), @SpecAssertion(section = PRODUCER_FIELD_LIFECYCLE, id = "ga") })
    public void testProducerFieldBeanCreate() throws Exception {
        BlackWidowConsumer spiderConsumer = getContextualReference(BlackWidowConsumer.class);
        assert spiderConsumer.getInjectedSpider().equals(BlackWidowProducer.blackWidow);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_FIELD, id = "d"), @SpecAssertion(section = PRODUCER_FIELD_LIFECYCLE, id = "m") })
    public void testProducerFieldReturnsNullIsDependent() throws Exception {
        NullSpiderConsumer consumerBean = getContextualReference(NullSpiderConsumer.class);
        assert consumerBean.getInjectedSpider() == null;
    }

    @Test(expectedExceptions = IllegalProductException.class)
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_FIELD_LIFECYCLE, id = "n") })
    public void testProducerFieldForNullValueNotDependent() throws Exception {
        Bean<BlackWidow> spiderBean = getBeans(BlackWidow.class, NULL_LITERAL, BROKEN_LITERAL).iterator().next();

        CreationalContext<BlackWidow> spiderContext = getCurrentManager().createCreationalContext(spiderBean);
        spiderBean.create(spiderContext);

        assert false;
    }

    @Test(expectedExceptions = IllegalProductException.class)
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_FIELD, id = "e"), @SpecAssertion(section = PRODUCER_FIELD_LIFECYCLE, id = "n") })
    public void testProducerFieldReturnsNullIsNotDependent() throws Exception {
        NullSpiderConsumerForBrokenProducer consumer = getContextualReference(NullSpiderConsumerForBrokenProducer.class);
        // The injected spider is proxied since it is in the request scope.
        // So to actually create the bean instance, we need to invoke
        // some method on the proxy.
        if (consumer.getInjectedSpider() == null) {
            // No proxy in this impl, and no exception = fail
        } else {
            // Proxy is assumed, so invoke some method
            consumer.getInjectedSpider().bite();
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_FIELD_LIFECYCLE, id = "o") })
    public void testProducerFieldBeanDestroy() throws Exception {
        BlackWidowProducer.reset();
        Bean<BlackWidow> bean = getUniqueBean(BlackWidow.class, TAME_LITERAL);
        CreationalContext<BlackWidow> ctx = getCurrentManager().createCreationalContext(bean);
        BlackWidow instance = bean.create(ctx);
        bean.destroy(instance, ctx);
        assertTrue(BlackWidowProducer.blackWidowDestroyed);
        assertEquals(BlackWidowProducer.destroyedBlackWidowTimeOfBirth, instance.getTimeOfBirth());
    }
}
