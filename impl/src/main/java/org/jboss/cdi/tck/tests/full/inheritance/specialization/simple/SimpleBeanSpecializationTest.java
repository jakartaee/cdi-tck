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
package org.jboss.cdi.tck.tests.full.inheritance.specialization.simple;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DIRECT_AND_INDIRECT_SPECIALIZATION;
import static org.jboss.cdi.tck.cdi.Sections.SPECIALIZATION;
import static org.jboss.cdi.tck.cdi.Sections.SPECIALIZE_MANAGED_BEAN;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.UnsatisfiedResolutionException;
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
public class SimpleBeanSpecializationTest extends AbstractTest {

    @SuppressWarnings("serial")
    private static Annotation LANDOWNER_LITERAL = new Landowner.Literal();

    @SuppressWarnings("serial")
    private static Annotation LAZY_LITERAL = new Lazy.Literal();

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SimpleBeanSpecializationTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION, id = "ia"),
            @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION, id = "ib") })
    public void testIndirectSpecialization() {
        // LazyFarmer specializes directly Farmer and indirectly Human
        Set<Bean<Human>> humanBeans = getBeans(Human.class);
        assertEquals(humanBeans.size(), 1);
        Set<Bean<Farmer>> farmerBeans = getBeans(Farmer.class, LANDOWNER_LITERAL);
        assertEquals(farmerBeans.size(), 1);
        Bean<Farmer> lazyFarmerBean = farmerBeans.iterator().next();
        assertEquals(lazyFarmerBean.getBeanClass(), humanBeans.iterator().next().getBeanClass());

        // Types of specializing bean
        Set<Type> lazyFarmerBeanTypes = lazyFarmerBean.getTypes();
        assertEquals(lazyFarmerBeanTypes.size(), 4);
        assertTrue(typeSetMatches(lazyFarmerBeanTypes, Object.class, Human.class, Farmer.class, LazyFarmer.class));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION, id = "ia") })
    public void testSpecializingBeanInjection(Farmer farmer) {
        assertEquals(farmer.getClassName(), LazyFarmer.class.getName());
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION, id = "j"),
            @SpecAssertion(section = SPECIALIZE_MANAGED_BEAN, id = "aa") })
    public void testSpecializingBeanHasQualifiersOfSpecializedAndSpecializingBean() {
        Bean<LazyFarmer> lazyFarmerBean = getBeans(LazyFarmer.class, LAZY_LITERAL).iterator().next();
        Set<Annotation> lazyFarmerBeanQualifiers = lazyFarmerBean.getQualifiers();
        assertEquals(lazyFarmerBeanQualifiers.size(), 5);
        // LazyFarmer inherits Default from Human; LandOwner and Named from Farmer
        assertTrue(annotationSetMatches(lazyFarmerBean.getQualifiers(), Landowner.class, Lazy.class, Any.class, Named.class,
                Default.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION, id = "k"),
            @SpecAssertion(section = SPECIALIZE_MANAGED_BEAN, id = "ab") })
    public void testSpecializingBeanHasNameOfSpecializedBean() {
        String expectedName = "farmer";
        Set<Bean<?>> beans = getCurrentManager().getBeans(expectedName);
        assertEquals(beans.size(), 1);
        Bean<?> farmerBean = beans.iterator().next();
        assertEquals(farmerBean.getName(), expectedName);
        assertEquals(farmerBean.getBeanClass(), LazyFarmer.class);
    }

    @Test(expectedExceptions = UnsatisfiedResolutionException.class)
    @SpecAssertions({ @SpecAssertion(section = SPECIALIZATION, id = "cb") })
    public void testProducerMethodOnSpecializedBeanNotCalled() {
        assertEquals(getBeans(Waste.class).size(), 0);
        // Throw UnsatisfiedResolutionException
        getContextualReference(Waste.class);
    }
}
