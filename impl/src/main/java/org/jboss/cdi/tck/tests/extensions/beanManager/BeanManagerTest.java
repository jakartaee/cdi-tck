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

package org.jboss.cdi.tck.tests.extensions.beanManager;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static org.jboss.cdi.tck.cdi.Sections.BM_DETERMINING_ANNOTATION;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_ANNOTATEDTYPE;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_ELRESOLVER;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_EXTENSION;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_INJECTIONTARGET;
import static org.jboss.cdi.tck.cdi.Sections.BM_RESOLVE_AMBIGUOUS_DEP;
import static org.jboss.cdi.tck.cdi.Sections.BM_VALIDATE_IP;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.AmbiguousResolutionException;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.InjectionException;
import jakarta.enterprise.inject.Specializes;
import jakarta.enterprise.inject.Stereotype;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.interceptor.InterceptorBinding;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.RetentionLiteral;
import org.jboss.cdi.tck.literals.TargetLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Mostly tests for extensions specified in chapter 11 of the specification and not already tested elsewhere.
 * 
 * TODO add tests for custom scope, qualifier, stereotype, interceptor added via extension
 * 
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class BeanManagerTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BeanManagerTest.class)
                .withClasses(RetentionLiteral.class, TargetLiteral.class)
                .withBeansXml(Descriptors.create(BeansDescriptor.class).getOrCreateAlternatives().clazz(Soy.class.getName()).up())
                .withExtensions(AfterBeanDiscoveryObserver.class).build();
    }

    @Test
    @SpecAssertion(section = BM_RESOLVE_AMBIGUOUS_DEP, id = "a")
    public void testAmbiguousDependencyResolved() {
        Set<Bean<?>> beans = getCurrentManager().getBeans(Food.class);
        assertEquals(beans.size(), 2);
        Bean<?> bean = getCurrentManager().resolve(beans);
        assertNotNull(bean);
        assertTrue(bean.isAlternative());
        assertTrue(typeSetMatches(bean.getTypes(), Food.class, Soy.class, Object.class));
    }

    @Test(expectedExceptions = AmbiguousResolutionException.class)
    @SpecAssertion(section = BM_RESOLVE_AMBIGUOUS_DEP, id = "b")
    public void testAmbiguousDependencyNotResolved() {
        Set<Bean<?>> beans = new HashSet<Bean<?>>();
        beans.addAll(getCurrentManager().getBeans(Dog.class));
        beans.addAll(getCurrentManager().getBeans(Terrier.class));
        getCurrentManager().resolve(beans);
    }

    @Test(expectedExceptions = InjectionException.class)
    @SpecAssertion(section = BM_VALIDATE_IP, id = "a")
    public void testValidateThrowsException() {
        DogHouse dogHouse = getContextualReference(DogHouse.class);
        InjectionPoint injectionPoint = new InjectionPointDecorator(dogHouse.getDog().getInjectedMetadata());
        // Wrap the injection point to change the type to a more generalized class
        getCurrentManager().validate(injectionPoint);
    }

    @Test
    @SpecAssertion(section = BM_DETERMINING_ANNOTATION, id = "aa")
    public void testDetermineQualifierType() {
        assertTrue(getCurrentManager().isQualifier(Any.class));
        assertTrue(getCurrentManager().isQualifier(Tame.class));
        assertFalse(getCurrentManager().isQualifier(AnimalStereotype.class));
        assertFalse(getCurrentManager().isQualifier(ApplicationScoped.class));
        assertFalse(getCurrentManager().isQualifier(Transactional.class));
    }

    @Test
    @SpecAssertion(section = BM_DETERMINING_ANNOTATION, id = "ab")
    public void testDetermineScope() {
        assertTrue(getCurrentManager().isScope(ApplicationScoped.class));
        assertTrue(getCurrentManager().isScope(DummyScoped.class));
        assertFalse(getCurrentManager().isScope(Tame.class));
        assertFalse(getCurrentManager().isScope(AnimalStereotype.class));
        assertFalse(getCurrentManager().isScope(Transactional.class));
    }

    @Test
    @SpecAssertion(section = BM_DETERMINING_ANNOTATION, id = "ac")
    public void testDetermineStereotype() {
        assertTrue(getCurrentManager().isStereotype(AnimalStereotype.class));
        assertFalse(getCurrentManager().isStereotype(Tame.class));
        assertFalse(getCurrentManager().isStereotype(ApplicationScoped.class));
        assertFalse(getCurrentManager().isStereotype(Transactional.class));
    }

    @Test
    @SpecAssertion(section = BM_DETERMINING_ANNOTATION, id = "ad")
    public void testDetermineInterceptorBindingType() {
        assertTrue(getCurrentManager().isInterceptorBinding(Transactional.class));
        assertFalse(getCurrentManager().isInterceptorBinding(Tame.class));
        assertFalse(getCurrentManager().isInterceptorBinding(AnimalStereotype.class));
        assertFalse(getCurrentManager().isInterceptorBinding(ApplicationScoped.class));
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
        assertNotNull(getCurrentManager().createInjectionTarget(annotatedType));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = BM_OBTAIN_INJECTIONTARGET, id = "ab")
    public void testObtainingInjectionTargetWithDefinitionError() {
        AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(Snake.class);
        getCurrentManager().createInjectionTarget(annotatedType);
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

    @Test
    @SpecAssertion(section = BM_RESOLVE_AMBIGUOUS_DEP, id = "c")
    public void testResolveWithNull() {
        assertNull(getCurrentManager().resolve(null));
    }

    @Test
    @SpecAssertion(section = BM_RESOLVE_AMBIGUOUS_DEP, id = "d")
    public void testResolveWithEmptySet() {
        assertNull(getCurrentManager().resolve(Collections.<Bean<? extends Integer>> emptySet()));
        assertNull(getCurrentManager().resolve(new HashSet<Bean<? extends String>>()));
    }

}
