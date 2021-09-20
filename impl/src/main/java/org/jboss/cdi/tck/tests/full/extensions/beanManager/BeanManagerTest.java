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

package org.jboss.cdi.tck.tests.full.extensions.beanManager;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BM_DETERMINING_ANNOTATION;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_ANNOTATEDTYPE;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_ELRESOLVER;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_EXTENSION;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_INJECTIONTARGET;
import static org.jboss.cdi.tck.cdi.Sections.BM_VALIDATE_IP;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.InjectionException;
import jakarta.enterprise.inject.Specializes;
import jakarta.enterprise.inject.Stereotype;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.interceptor.InterceptorBinding;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.RetentionLiteral;
import org.jboss.cdi.tck.literals.TargetLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.beanContainer.BeanContainerTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

/**
 * Mostly tests for extensions specified in chapter 11 of the specification and not already tested elsewhere.
 * Functionality that is also present in {@link jakarta.enterprise.inject.spi.BeanContainer} should be tested
 * in {@link BeanContainerTest}
 *
 * @author David Allen
 * @author Martin Kouba
 */
@Test(groups = CDI_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class BeanManagerTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BeanManagerTest.class)
                .withClasses(RetentionLiteral.class, TargetLiteral.class)
                .withExtensions(AfterBeanDiscoveryObserver.class).build();
    }

    @Test(expectedExceptions = InjectionException.class)
    @SpecAssertion(section = BM_VALIDATE_IP, id = "a")
    public void testValidateThrowsException() {
        DogHouse dogHouse = getContextualReference(DogHouse.class);
        InjectionPoint injectionPoint = new InjectionPointDecorator(dogHouse.getDog().getInjectedMetadata());
        // Wrap the injection point to change the type to a more generalized class
        getCurrentManager().validate(injectionPoint);
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertion(section = BM_DETERMINING_ANNOTATION, id = "ae")
    public void testGetMetaAnnotationsForStereotype() {
        Set<Annotation> stereotypeAnnotations = getCurrentManager().getStereotypeDefinition(AnimalStereotype.class);
        assertEquals(stereotypeAnnotations.size(), 5);
        assertTrue(stereotypeAnnotations.contains(new AnnotationLiteral<Stereotype>() {
        }));
        assertTrue(stereotypeAnnotations.contains(RequestScoped.Literal.INSTANCE));
        assertTrue(stereotypeAnnotations.contains(new AnnotationLiteral<Inherited>() {
        }));
        assertTrue(stereotypeAnnotations.contains(new RetentionLiteral() {

            public RetentionPolicy value() {
                return RetentionPolicy.RUNTIME;
            }

        }));
        assertTrue(stereotypeAnnotations.contains(new TargetLiteral() {

            public ElementType[] value() {
                ElementType[] value = { TYPE, METHOD, FIELD };
                return value;
            }

        }));
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertion(section = BM_DETERMINING_ANNOTATION, id = "af")
    public void testGetMetaAnnotationsForInterceptorBindingType() {
        Set<Annotation> metaAnnotations = getCurrentManager().getInterceptorBindingDefinition(Transactional.class);
        assertEquals(metaAnnotations.size(), 4);
        assert annotationSetMatches(metaAnnotations, Target.class, Retention.class, Documented.class, InterceptorBinding.class);
    }

    @Test
    @SpecAssertion(section = BM_DETERMINING_ANNOTATION, id = "ag")
    public void testDetermineScopeType() {
        assertTrue(getCurrentManager().isNormalScope(RequestScoped.class));
        assertFalse(getCurrentManager().isPassivatingScope(RequestScoped.class));
        assertTrue(getCurrentManager().isNormalScope(SessionScoped.class));
        assertTrue(getCurrentManager().isPassivatingScope(SessionScoped.class));
        assertTrue(getCurrentManager().isNormalScope(DummyScoped.class));
        assertFalse(getCurrentManager().isPassivatingScope(DummyScoped.class));
    }

    @Test
    @SpecAssertion(section = BM_OBTAIN_ELRESOLVER, id = "a")
    public void testGetELResolver() {
        assertNotNull(getCurrentManager().getELResolver());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_ANNOTATEDTYPE, id = "a") })
    public void testObtainingAnnotatedType() {
        AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(DerivedBean.class);
        assertTrue(annotatedType.isAnnotationPresent(Specializes.class));
        assertTrue(annotatedType.isAnnotationPresent(Tame.class));
        assertEquals(1, annotatedType.getFields().size());
        assertTrue(annotatedType.getMethods().isEmpty());
        assertEquals(3, annotatedType.getTypeClosure().size());
    }

    @Test
    @SpecAssertion(section = BM_OBTAIN_INJECTIONTARGET, id = "aa")
    // CDI-83
    public void testObtainingInjectionTarget() {
        AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(DerivedBean.class);
        assertNotNull(getCurrentManager().getInjectionTargetFactory(annotatedType).createInjectionTarget(null));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = BM_OBTAIN_INJECTIONTARGET, id = "ab")
    public void testObtainingInjectionTargetWithDefinitionError() {
        AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(Snake.class);
        getCurrentManager().getInjectionTargetFactory(annotatedType).createInjectionTarget(null);
    }

    /**
     * The method BeanManager.getExtension() returns the container's instance of an Extension class declared in
     * META-INF/services, or throws an IllegalArgumentException if the container has no instance of the given class.
     */
    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_EXTENSION, id = "a"), @SpecAssertion(section = BM_OBTAIN_EXTENSION, id = "b") })
    public void testGetExtension() {

        AfterBeanDiscoveryObserver extension = getCurrentManager().getExtension(AfterBeanDiscoveryObserver.class);
        assertNotNull(extension);
        assertTrue(extension.getAfterBeanDiscoveryObserved());

        try {
            getCurrentManager().getExtension(UnregisteredExtension.class);
        } catch (Throwable t) {
            assertTrue(isThrowablePresent(IllegalArgumentException.class, t));
            return;
        }
        fail();
    }
}
