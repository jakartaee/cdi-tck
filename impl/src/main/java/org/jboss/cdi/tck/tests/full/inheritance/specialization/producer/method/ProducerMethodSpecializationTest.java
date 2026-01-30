/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.inheritance.specialization.producer.method;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DIRECT_AND_INDIRECT_SPECIALIZATION;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_OR_DISPOSER_METHODS_INVOCATION;
import static org.jboss.cdi.tck.cdi.Sections.SPECIALIZE_PRODUCER_METHOD;
import static org.jboss.cdi.tck.util.Assert.assertAnnotationsMatch;
import static org.jboss.cdi.tck.util.Assert.assertTypesMatch;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.inject.Named;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class ProducerMethodSpecializationTest extends AbstractTest {

    @SuppressWarnings("serial")
    private static Annotation EXPENSIVE_LITERAL = new Expensive.Literal();
    @SuppressWarnings("serial")
    private static Annotation SPARKLY_LITERAL = new Sparkly.Literal();

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProducerMethodSpecializationTest.class).build();
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION, id = "ia"),
            @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION, id = "k"),
            @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION, id = "j"),
            @SpecAssertion(section = PRODUCER_OR_DISPOSER_METHODS_INVOCATION, id = "c"),
            @SpecAssertion(section = SPECIALIZE_PRODUCER_METHOD, id = "aa") })
    public void testSpecializingProducerMethod() {

        Set<Bean<Necklace>> expensiveNecklaceBeans = getBeans(Necklace.class, EXPENSIVE_LITERAL);
        // There is only one bean for type Necklace and qualifier Expensive
        assertEquals(expensiveNecklaceBeans.size(), 1);

        Bean<Necklace> expensiveNecklaceBean = expensiveNecklaceBeans.iterator().next();

        // Check types of specializing bean
        Set<Type> expensiveNecklaceBeanTypes = expensiveNecklaceBean.getTypes();
        assertEquals(expensiveNecklaceBeanTypes.size(), 3);
        assertTypesMatch(expensiveNecklaceBeanTypes, Object.class, Product.class, Necklace.class);

        // Check qualifiers of specializing bean
        Set<Annotation> expensiveNecklaceQualifiers = expensiveNecklaceBean.getQualifiers();
        assertEquals(expensiveNecklaceQualifiers.size(), 4);
        assertAnnotationsMatch(expensiveNecklaceQualifiers, Expensive.class, Sparkly.class, Any.class, Named.class);

        // There is only one bean for type Necklace and qualifier Sparkly
        Set<Bean<Necklace>> sparklyNecklaceBeans = getBeans(Necklace.class, SPARKLY_LITERAL);
        assertEquals(sparklyNecklaceBeans.size(), 1);
        Bean<Necklace> sparklyBean = sparklyNecklaceBeans.iterator().next();
        // Check EL name of specializing bean
        assertEquals(sparklyBean.getName(), "expensiveGift");
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION, id = "ia") })
    public void testSpecializingBeanInjection(@Expensive Product product) {
        assertTrue(product instanceof Necklace);
        assertEquals(product.getPrice(), 10);
    }

}
