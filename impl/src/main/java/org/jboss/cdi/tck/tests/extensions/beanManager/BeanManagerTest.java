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
import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.REWRITE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.AmbiguousResolutionException;
import javax.enterprise.inject.InjectionException;
import javax.enterprise.inject.Specializes;
import javax.enterprise.inject.Stereotype;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.util.AnnotationLiteral;
import javax.interceptor.InterceptorBinding;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.RetentionLiteral;
import org.jboss.cdi.tck.literals.TargetLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.extensions.alternative.metadata.AnnotatedTypeWrapper;
import org.jboss.cdi.tck.tests.extensions.alternative.metadata.AnnotatedWrapper;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Mostly tests for extensions specified in chapter 11 of the specification and not already tested elsewhere.
 * 
 * @author David Allen
 * @author Martin Kouba
 */
// SHRINKWRAP-369
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class BeanManagerTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BeanManagerTest.class)
                .withClasses(AnnotatedTypeWrapper.class, AnnotatedWrapper.class)
                .withExtensions(AfterBeanDiscoveryObserver.class, ProcessAnnotatedTypeObserver.class).build();
    }

    @Test
    @SpecAssertion(section = "11.3.8", id = "a")
    public void testAmbiguousDependencyResolved() {
        Set<Bean<?>> beans = new HashSet<Bean<?>>();
        beans.addAll(getCurrentManager().getBeans(SimpleBean.class));
        beans.addAll(getCurrentManager().getBeans(DerivedBean.class));
        getCurrentManager().resolve(beans);
    }

    @Test(expectedExceptions = AmbiguousResolutionException.class)
    @SpecAssertion(section = "11.3.8", id = "b")
    public void testAmbiguousDependencyNotResolved() {
        Set<Bean<?>> beans = new HashSet<Bean<?>>();
        beans.addAll(getCurrentManager().getBeans(Dog.class));
        beans.addAll(getCurrentManager().getBeans(Terrier.class));
        getCurrentManager().resolve(beans);
    }

    @Test(expectedExceptions = InjectionException.class)
    @SpecAssertion(section = "11.3.9", id = "a")
    public void testValidateThrowsException() {
        DogHouse dogHouse = getInstanceByType(DogHouse.class);
        InjectionPoint injectionPoint = new InjectionPointDecorator(dogHouse.getDog().getInjectedMetadata());
        // Wrap the injection point to change the type to a more generalized class
        getCurrentManager().validate(injectionPoint);
    }

    @Test(groups = REWRITE)
    @SpecAssertion(section = "11.3.14", id = "aa")
    // Should also check a custom bindingtype
    public void testDetermineBindingType() {
        assert getCurrentManager().isQualifier(Tame.class);
        assert !getCurrentManager().isQualifier(AnimalStereotype.class);
        assert !getCurrentManager().isQualifier(ApplicationScoped.class);
        assert !getCurrentManager().isQualifier(Transactional.class);
    }

    @Test(groups = REWRITE)
    // Should also check a custom scope
    @SpecAssertion(section = "11.3.14", id = "ab")
    public void testDetermineScopeType() {
        assert getCurrentManager().isScope(ApplicationScoped.class);
        assert !getCurrentManager().isScope(Tame.class);
        assert !getCurrentManager().isScope(AnimalStereotype.class);
        assert !getCurrentManager().isScope(Transactional.class);
    }

    @Test(groups = REWRITE)
    @SpecAssertion(section = "11.3.14", id = "ac")
    // Should also check a custom stereotype
    public void testDetermineStereotype() {
        assert getCurrentManager().isStereotype(AnimalStereotype.class);
        assert !getCurrentManager().isStereotype(Tame.class);
        assert !getCurrentManager().isStereotype(ApplicationScoped.class);
        assert !getCurrentManager().isStereotype(Transactional.class);
    }

    @Test(groups = REWRITE)
    // WBRI-59
    // Should also check a custom interceptor binding type
    @SpecAssertion(section = "11.3.14", id = "ad")
    public void testDetermineInterceptorBindingType() {
        assert getCurrentManager().isInterceptorBinding(Transactional.class);
        assert !getCurrentManager().isInterceptorBinding(Tame.class);
        assert !getCurrentManager().isInterceptorBinding(AnimalStereotype.class);
        assert !getCurrentManager().isInterceptorBinding(ApplicationScoped.class);
    }

    @Test(groups = { REWRITE })
    @SpecAssertion(section = "11.3.14", id = "ae")
    // Should also check a custom sterotype
    public void testGetMetaAnnotationsForStereotype() {
        Set<Annotation> stereotypeAnnotations = getCurrentManager().getStereotypeDefinition(AnimalStereotype.class);
        assert stereotypeAnnotations.size() == 5;
        assert stereotypeAnnotations.contains(new AnnotationLiteral<Stereotype>() {
        });
        assert stereotypeAnnotations.contains(new AnnotationLiteral<RequestScoped>() {
        });
        assert stereotypeAnnotations.contains(new AnnotationLiteral<Inherited>() {
        });
        assert stereotypeAnnotations.contains(new RetentionLiteral() {

            public RetentionPolicy value() {
                return RetentionPolicy.RUNTIME;
            }

        });
        assert stereotypeAnnotations.contains(new TargetLiteral() {

            public ElementType[] value() {
                ElementType[] value = { TYPE, METHOD, FIELD };
                return value;
            }

        });
    }

    @Test(groups = { REWRITE })
    @SpecAssertion(section = "11.3.14", id = "af")
    public void testGetMetaAnnotationsForInterceptorBindingType() {
        Set<Annotation> metaAnnotations = getCurrentManager().getInterceptorBindingDefinition(Transactional.class);
        assert metaAnnotations.size() == 4;
        assert annotationSetMatches(metaAnnotations, Target.class, Retention.class, Documented.class, InterceptorBinding.class);
    }

    @Test(groups = { REWRITE })
    @SpecAssertion(section = "11.3.14", id = "ag")
    // Should also check a custom defined scope
    public void testgetScope() {
        assert getCurrentManager().isNormalScope(RequestScoped.class);
        assert !getCurrentManager().isPassivatingScope(RequestScoped.class);

        assert getCurrentManager().isNormalScope(SessionScoped.class);
        assert getCurrentManager().isPassivatingScope(SessionScoped.class);
    }

    @Test
    @SpecAssertion(section = "11.3.16", id = "a")
    public void testGetELResolver() {
        assert getCurrentManager().getELResolver() != null;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.3.18", id = "a") })
    public void testObtainingAnnotatedType() {
        AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(DerivedBean.class);
        assertTrue(annotatedType.isAnnotationPresent(Specializes.class));
        assertTrue(annotatedType.isAnnotationPresent(Tame.class));
        assertEquals(1, annotatedType.getFields().size());
        assertTrue(annotatedType.getMethods().isEmpty());
        assertEquals(3, annotatedType.getTypeClosure().size());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.3.18", id = "b") })
    public void testObtainingWrappedAnnotatedType() {
        AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(WrappedBean.class);
        assertTrue(annotatedType.isAnnotationPresent(Tame.class));
        assertTrue(annotatedType.isAnnotationPresent(Transactional.class));
        assertTrue(annotatedType.getFields().isEmpty());
        assertTrue(annotatedType.getMethods().isEmpty());
        assertEquals(2, annotatedType.getTypeClosure().size());
    }

    @Test
    @SpecAssertion(section = "11.3.19", id = "aa")
    // CDI-83
    public void testObtainingInjectionTarget() {
        AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(DerivedBean.class);
        assert getCurrentManager().createInjectionTarget(annotatedType) != null;
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = "11.3.19", id = "ab")
    public void testObtainingInjectionTargetWithDefinitionError() {
        AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(Snake.class);
        getCurrentManager().createInjectionTarget(annotatedType);
    }

    /**
     * The method BeanManager.getExtension() returns the container's instance of an Extension class declared in
     * META-INF/services, or throws an IllegalArgumentException if the container has no instance of the given class.
     */
    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.3.24", id = "a"), @SpecAssertion(section = "11.3.24", id = "b") })
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
