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
package org.jboss.cdi.tck.tests.full.extensions.beanManager.beanAttributes;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_BEANATTRIBUTES;
import static org.jboss.cdi.tck.util.Assert.assertAnnotationsMatch;
import static org.jboss.cdi.tck.util.Assert.assertTypesMatch;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.AnnotatedConstructor;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanAttributes;
import jakarta.inject.Named;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.annotated.AnnotatedTypeWrapper;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * </p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = CDI_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class CreateBeanAttributesTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(CreateBeanAttributesTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL)).build();
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertion(section = BM_OBTAIN_BEANATTRIBUTES, id = "a")
    public void testBeanAttributesForManagedBean() {
        AnnotatedType<Mountain> type = getCurrentManager().createAnnotatedType(Mountain.class);
        BeanAttributes<Mountain> attributes = getCurrentManager().createBeanAttributes(type);

        assertTypesMatch(attributes.getTypes(), Landmark.class, Object.class);
        assertTypesMatch(attributes.getStereotypes(), TundraStereotype.class);
        assertAnnotationsMatch(attributes.getQualifiers(), Natural.class, Any.class);
        assertEquals(attributes.getScope(), ApplicationScoped.class);
        assertEquals(attributes.getName(), "mountain");
        assertTrue(attributes.isAlternative());
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertion(section = BM_OBTAIN_BEANATTRIBUTES, id = "a")
    public void testBeanAttributesForManagedBeanWithModifiedAnnotatedType() {
        AnnotatedType<Mountain> type = getCurrentManager().createAnnotatedType(Mountain.class);
        AnnotatedType<Mountain> wrappedType = new AnnotatedTypeWrapper<Mountain>(type, false, NamedLiteral.of("Mount Blanc"));
        BeanAttributes<Mountain> attributes = getCurrentManager().createBeanAttributes(wrappedType);

        assertTypesMatch(attributes.getTypes(), Mountain.class, Landmark.class, Object.class);
        assertTrue(attributes.getStereotypes().isEmpty());
        assertAnnotationsMatch(attributes.getQualifiers(), Named.class, Any.class, Default.class);
        assertEquals(attributes.getScope(), Dependent.class);
        assertEquals(attributes.getName(), "Mount Blanc");
        assertFalse(attributes.isAlternative());
    }

    @SuppressWarnings("unchecked")
    private void verifyLakeFish(BeanAttributes<?> attributes) {
        assertTypesMatch(attributes.getTypes(), Fish.class, Object.class);
        assertTypesMatch(attributes.getStereotypes(), TundraStereotype.class);
        assertAnnotationsMatch(attributes.getQualifiers(), Natural.class, Any.class, Named.class);
        assertEquals(attributes.getScope(), ApplicationScoped.class);
        assertEquals(attributes.getName(), "fish");
        assertTrue(attributes.isAlternative());
    }

    @SuppressWarnings("unchecked")
    private void verifyDamFish(BeanAttributes<?> attributes) {
        assertTypesMatch(attributes.getTypes(), Fish.class, Animal.class, Object.class);
        assertAnnotationsMatch(attributes.getQualifiers(), Wild.class, Any.class);
        assertTrue(attributes.getStereotypes().isEmpty());
        assertEquals(attributes.getScope(), Dependent.class);
        assertNull(attributes.getName());
        assertFalse(attributes.isAlternative());
    }

    @SuppressWarnings("unchecked")
    private void verifyVolume(BeanAttributes<?> attributes) {
        assertTypesMatch(attributes.getTypes(), long.class, Object.class);
        assertAnnotationsMatch(attributes.getQualifiers(), Any.class, Default.class, Named.class);
        assertTrue(attributes.getStereotypes().isEmpty());
        assertEquals(attributes.getScope(), Dependent.class);
        assertEquals(attributes.getName(), "volume");
        assertFalse(attributes.isAlternative());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = BM_OBTAIN_BEANATTRIBUTES, id = "c")
    public void testInvalidMember() {
        AnnotatedConstructor<?> constructor = getCurrentManager().createAnnotatedType(InvalidBeanType.class).getConstructors()
                .iterator().next();
        getCurrentManager().createBeanAttributes(constructor);
    }
}
