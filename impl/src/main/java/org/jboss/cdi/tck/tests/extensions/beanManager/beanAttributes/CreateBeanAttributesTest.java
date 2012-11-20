/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.tests.extensions.beanManager.beanAttributes;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanAttributes;
import javax.inject.Named;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.NamedLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.annotated.AnnotatedTypeWrapper;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class CreateBeanAttributesTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(CreateBeanAttributesTest.class).build();
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertion(section = "11.3.23", id = "a")
    public void testBeanAttributesForManagedBean() {
        AnnotatedType<Mountain> type = getCurrentManager().createAnnotatedType(Mountain.class);
        BeanAttributes<Mountain> attributes = getCurrentManager().createBeanAttributes(type);

        assertTrue(typeSetMatches(attributes.getTypes(), Landmark.class, Object.class));
        assertTrue(typeSetMatches(attributes.getStereotypes(), TundraStereotype.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), Natural.class, Any.class));
        assertEquals(attributes.getScope(), ApplicationScoped.class);
        assertEquals(attributes.getName(), "mountain");
        assertTrue(attributes.isAlternative());
        assertTrue(attributes.isNullable());
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertion(section = "11.3.23", id = "a")
    public void testBeanAttributesForManagedBeanWithModifiedAnnotatedType() {
        AnnotatedType<Mountain> type = getCurrentManager().createAnnotatedType(Mountain.class);
        AnnotatedType<Mountain> wrappedType = new AnnotatedTypeWrapper<Mountain>(type, false, new NamedLiteral("Mount Blanc"));
        BeanAttributes<Mountain> attributes = getCurrentManager().createBeanAttributes(wrappedType);

        assertTrue(typeSetMatches(attributes.getTypes(), Mountain.class, Landmark.class, Object.class));
        assertTrue(attributes.getStereotypes().isEmpty());
        assertTrue(annotationSetMatches(attributes.getQualifiers(), Named.class, Any.class, Default.class));
        assertEquals(attributes.getScope(), Dependent.class);
        assertEquals(attributes.getName(), "Mount Blanc");
        assertFalse(attributes.isAlternative());
        assertTrue(attributes.isNullable());
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertion(section = "11.3.23", id = "a")
    public void testBeanAttributesForSessionBean() {
        AnnotatedType<Lake> type = getCurrentManager().createAnnotatedType(Lake.class);
        BeanAttributes<Lake> attributes = getCurrentManager().createBeanAttributes(type);

        assertTrue(typeSetMatches(attributes.getTypes(), LakeLocal.class, WaterBody.class, Landmark.class, Object.class));
        assertTrue(typeSetMatches(attributes.getStereotypes(), TundraStereotype.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), Natural.class, Any.class));
        assertEquals(attributes.getScope(), Dependent.class);
        assertEquals(attributes.getName(), "lake");
        assertTrue(attributes.isAlternative());
        assertTrue(attributes.isNullable());
    }

    @Test
    @SpecAssertion(section = "11.3.23", id = "b")
    public void testBeanAttributesForMethod() {
        AnnotatedType<Dam> type = getCurrentManager().createAnnotatedType(Dam.class);

        AnnotatedMethod<?> lakeFishMethod = null;
        AnnotatedMethod<?> damFishMethod = null;
        AnnotatedMethod<?> volumeMethod = null;

        for (AnnotatedMethod<?> method : type.getMethods()) {
            if (method.getJavaMember().getName().equals("getFish")
                    && method.getJavaMember().getDeclaringClass().equals(Dam.class)) {
                damFishMethod = method;
            }
            if (method.getJavaMember().getName().equals("getFish")
                    && method.getJavaMember().getDeclaringClass().equals(Lake.class)) {
                lakeFishMethod = method;
            }
            if (method.getJavaMember().getName().equals("getVolume")
                    && method.getJavaMember().getDeclaringClass().equals(Lake.class)) {
                volumeMethod = method;
            }
        }
        assertNotNull(lakeFishMethod);
        assertNotNull(damFishMethod);
        assertNotNull(volumeMethod);

        verifyLakeFish(getCurrentManager().createBeanAttributes(lakeFishMethod));
        verifyDamFish(getCurrentManager().createBeanAttributes(damFishMethod));
        verifyVolume(getCurrentManager().createBeanAttributes(volumeMethod));
    }

    @Test
    @SpecAssertion(section = "11.3.23", id = "b")
    public void testBeanAttributesForField() {
        AnnotatedType<Dam> type = getCurrentManager().createAnnotatedType(Dam.class);

        AnnotatedField<?> lakeFishField = null;
        AnnotatedField<?> damFishField = null;
        AnnotatedField<?> volumeField = null;

        for (AnnotatedField<?> field : type.getFields()) {
            if (field.getJavaMember().getName().equals("fish") && field.getJavaMember().getDeclaringClass().equals(Dam.class)) {
                damFishField = field;
            }
            if (field.getJavaMember().getName().equals("fish") && field.getJavaMember().getDeclaringClass().equals(Lake.class)) {
                lakeFishField = field;
            }
            if (field.getJavaMember().getName().equals("volume")
                    && field.getJavaMember().getDeclaringClass().equals(Lake.class)) {
                volumeField = field;
            }
        }
        assertNotNull(lakeFishField);
        assertNotNull(damFishField);
        assertNotNull(volumeField);

        verifyLakeFish(getCurrentManager().createBeanAttributes(lakeFishField));
        verifyDamFish(getCurrentManager().createBeanAttributes(damFishField));
        verifyVolume(getCurrentManager().createBeanAttributes(volumeField));
    }

    @SuppressWarnings("unchecked")
    private void verifyLakeFish(BeanAttributes<?> attributes) {
        assertTrue(typeSetMatches(attributes.getTypes(), Fish.class, Object.class));
        assertTrue(typeSetMatches(attributes.getStereotypes(), TundraStereotype.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), Natural.class, Any.class, Named.class));
        assertEquals(attributes.getScope(), ApplicationScoped.class);
        assertEquals(attributes.getName(), "fish");
        assertTrue(attributes.isAlternative());
        assertTrue(attributes.isNullable());
    }

    @SuppressWarnings("unchecked")
    private void verifyDamFish(BeanAttributes<?> attributes) {
        assertTrue(typeSetMatches(attributes.getTypes(), Fish.class, Animal.class, Object.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), Wild.class, Any.class));
        assertTrue(attributes.getStereotypes().isEmpty());
        assertEquals(attributes.getScope(), Dependent.class);
        assertNull(attributes.getName());
        assertFalse(attributes.isAlternative());
        assertTrue(attributes.isNullable());
    }

    @SuppressWarnings("unchecked")
    private void verifyVolume(BeanAttributes<?> attributes) {
        assertTrue(typeSetMatches(attributes.getTypes(), long.class, Object.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), Any.class, Default.class, Named.class));
        assertTrue(attributes.getStereotypes().isEmpty());
        assertEquals(attributes.getScope(), Dependent.class);
        assertEquals(attributes.getName(), "volume");
        assertFalse(attributes.isAlternative());
        assertFalse(attributes.isNullable());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = "11.3.23", id = "c")
    public void testInvalidMember() {
        AnnotatedConstructor<?> constructor = getCurrentManager().createAnnotatedType(InvalidBeanType.class).getConstructors()
                .iterator().next();
        getCurrentManager().createBeanAttributes(constructor);
    }
}
