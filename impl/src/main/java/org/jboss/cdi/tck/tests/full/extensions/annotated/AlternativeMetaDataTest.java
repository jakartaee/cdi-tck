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

package org.jboss.cdi.tck.tests.full.extensions.annotated;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.ALTERNATIVE_METADATA_SOURCES;
import static org.jboss.cdi.tck.util.Assert.assertAnnotationSetMatches;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.spi.AnnotatedConstructor;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Contains all the functioning tests for the extension which provides alternative meta data sources. Some of the assertions are
 * really statements of intent, but are tested here to make sure the container provides implementations that meet that same
 * intent as any third party extension would.
 * 
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class AlternativeMetaDataTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AlternativeMetaDataTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL)).build();
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "c")
    public void testBaseType() {
        AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(DogHouse.class);
        assert annotatedType.getBaseType().equals(DogHouse.class);
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "d")
    public void testTypeClosure() {
        AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(ClassD.class);
        assert annotatedType.getTypeClosure().contains(Object.class);
        assert annotatedType.getTypeClosure().contains(InterfaceA.class);
        assert annotatedType.getTypeClosure().contains(InterfaceB.class);
        assert annotatedType.getTypeClosure().contains(AbstractC.class);
        assert annotatedType.getTypeClosure().contains(ClassD.class);
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "e")
    public void testGetAnnotation() {
        AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(ClassD.class);
        assert annotatedType.getAnnotation(RequestScoped.class) != null;
        assert annotatedType.getAnnotation(ApplicationScoped.class) == null;
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "f") })
    public void testGetAnnotations() {
        AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(ClassD.class);
        assert annotatedType.getAnnotations().size() == 2;
        assert annotationSetMatches(annotatedType.getAnnotations(), RequestScoped.class, Tame.class);
        AnnotatedType<WildCat> annotatedWildCatType = getCurrentManager().createAnnotatedType(WildCat.class);
        assertAnnotationSetMatches(annotatedWildCatType.getAnnotations(), RequestScoped.class);
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "g")
    public void testIsAnnotationPresent() {
        AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(ClassD.class);
        assert annotatedType.isAnnotationPresent(RequestScoped.class);
        assert !annotatedType.isAnnotationPresent(ApplicationScoped.class);
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "aaa")
    public void testConstructors() {
        AnnotatedType<WildCat> annotatedType = getCurrentManager().createAnnotatedType(WildCat.class);
        Set<AnnotatedConstructor<WildCat>> constructors = annotatedType.getConstructors();
        assertEquals(constructors.size(), 4);
        for (AnnotatedConstructor<WildCat> annotatedConstructor : constructors) {
            if (Modifier.isPrivate(annotatedConstructor.getJavaMember().getModifiers())) {
                verifyConstructor(annotatedConstructor, Integer.class);
            } else if (Modifier.isPublic(annotatedConstructor.getJavaMember().getModifiers())) {
                verifyConstructor(annotatedConstructor, String.class);
            } else if (Modifier.isProtected(annotatedConstructor.getJavaMember().getModifiers())) {
                verifyConstructor(annotatedConstructor, Cat.class);
            } else {
                verifyConstructor(annotatedConstructor, Date.class);
            }
        }
    }

    private void verifyConstructor(AnnotatedConstructor<WildCat> annotatedConstructor, Class<?> paramClass) {
        Class<?>[] constructorParams = annotatedConstructor.getJavaMember().getParameterTypes();
        assertEquals(constructorParams.length, 1);
        assertEquals(constructorParams[0], paramClass);
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "aab")
    public void testMethods() {
        AnnotatedType<WildCat> annotatedType = getCurrentManager().createAnnotatedType(WildCat.class);
        Set<AnnotatedMethod<? super WildCat>> methods = annotatedType.getMethods();
        String[] names = new String[] { "yowl", "jump", "bite", "getName" };
        assertEquals(methods.size(), 4);
        for (AnnotatedMethod<? super WildCat> method : methods) {
            // Just simple test for method name
            assertTrue(arrayContains(names, method.getJavaMember().getName()));
        }
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "aad")
    public void testFields() {
        AnnotatedType<WildCat> annotatedType = getCurrentManager().createAnnotatedType(WildCat.class);
        Set<AnnotatedField<? super WildCat>> fields = annotatedType.getFields();
        String[] names = new String[] { "age", "name", "publicName", "isOld" };
        assertEquals(fields.size(), 4);
        for (AnnotatedField<? super WildCat> field : fields) {
            // Just simple test for field name
            assertTrue(arrayContains(names, field.getJavaMember().getName()));
        }
    }

    private boolean arrayContains(Object[] array, Object objectToFind) {

        if (array == null || objectToFind == null)
            return false;

        for (Object obj : array) {
            if (obj.equals(objectToFind))
                return true;
        }
        return false;
    }

}
