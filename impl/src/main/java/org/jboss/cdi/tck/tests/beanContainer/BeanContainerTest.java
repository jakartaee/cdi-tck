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

package org.jboss.cdi.tck.tests.beanContainer;

import static org.jboss.cdi.tck.cdi.Sections.BM_DETERMINING_ANNOTATION;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_CONTEXTS;
import static org.jboss.cdi.tck.cdi.Sections.BM_RESOLVE_AMBIGUOUS_DEP;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
    
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.inject.AmbiguousResolutionException;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.inject.Singleton;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.RetentionLiteral;
import org.jboss.cdi.tck.literals.TargetLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests some of {@link jakarta.enterprise.inject.spi.BeanContainer} functionality.
 *
 * This is part of the former {@link org.jboss.cdi.tck.tests.full.extensions.beanManager.BeanManagerTest}.
 *
 * @author Matej Novotny
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class BeanContainerTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BeanContainerTest.class)
                .withClasses(RetentionLiteral.class, TargetLiteral.class)
                .withBuildCompatibleExtension(ContextRegisteringExtension.class).build();
    }

    @Test
    @SpecAssertion(section = BM_RESOLVE_AMBIGUOUS_DEP, id = "a")
    public void testAmbiguousDependencyResolved() {
        Set<Bean<?>> beans = getCurrentBeanContainer().getBeans(Food.class);
        assertEquals(beans.size(), 2);
        Bean<?> bean = getCurrentBeanContainer().resolve(beans);
        assertNotNull(bean);
        assertTrue(bean.isAlternative());
        assertTrue(typeSetMatches(bean.getTypes(), Food.class, Soy.class, Object.class));
    }

    @Test(expectedExceptions = AmbiguousResolutionException.class)
    @SpecAssertion(section = BM_RESOLVE_AMBIGUOUS_DEP, id = "b")
    public void testAmbiguousDependencyNotResolved() {
        Set<Bean<?>> beans = new HashSet<Bean<?>>();
        beans.addAll(getCurrentBeanContainer().getBeans(Dog.class));
        beans.addAll(getCurrentBeanContainer().getBeans(Terrier.class));
        getCurrentBeanContainer().resolve(beans);
    }

    @Test
    @SpecAssertion(section = BM_DETERMINING_ANNOTATION, id = "aa")
    public void testDetermineQualifierType() {
        assertTrue(getCurrentBeanContainer().isQualifier(Any.class));
        assertTrue(getCurrentBeanContainer().isQualifier(Tame.class));
        assertFalse(getCurrentBeanContainer().isQualifier(AnimalStereotype.class));
        assertFalse(getCurrentBeanContainer().isQualifier(ApplicationScoped.class));
        assertFalse(getCurrentBeanContainer().isQualifier(Transactional.class));
    }

    @Test
    @SpecAssertion(section = BM_DETERMINING_ANNOTATION, id = "ab")
    public void testDetermineScope() {
        assertTrue(getCurrentBeanContainer().isScope(ApplicationScoped.class));
        assertFalse(getCurrentBeanContainer().isScope(Tame.class));
        assertFalse(getCurrentBeanContainer().isScope(AnimalStereotype.class));
        assertFalse(getCurrentBeanContainer().isScope(Transactional.class));
    }

    @Test
    @SpecAssertion(section = BM_DETERMINING_ANNOTATION, id = "ac")
    public void testDetermineStereotype() {
        assertTrue(getCurrentBeanContainer().isStereotype(AnimalStereotype.class));
        assertFalse(getCurrentBeanContainer().isStereotype(Tame.class));
        assertFalse(getCurrentBeanContainer().isStereotype(ApplicationScoped.class));
        assertFalse(getCurrentBeanContainer().isStereotype(Transactional.class));
    }

    @Test
    @SpecAssertion(section = BM_DETERMINING_ANNOTATION, id = "ad")
    public void testDetermineInterceptorBindingType() {
        assertTrue(getCurrentBeanContainer().isInterceptorBinding(Transactional.class));
        assertFalse(getCurrentBeanContainer().isInterceptorBinding(Tame.class));
        assertFalse(getCurrentBeanContainer().isInterceptorBinding(AnimalStereotype.class));
        assertFalse(getCurrentBeanContainer().isInterceptorBinding(ApplicationScoped.class));
    }

    @Test
    @SpecAssertion(section = BM_DETERMINING_ANNOTATION, id = "ag")
    public void testDetermineScopeType() {
        assertTrue(getCurrentBeanContainer().isNormalScope(RequestScoped.class));
        assertTrue(getCurrentBeanContainer().isNormalScope(SessionScoped.class));
        assertFalse(getCurrentBeanContainer().isNormalScope(Dependent.class));
    }

    @Test
    @SpecAssertion(section = BM_RESOLVE_AMBIGUOUS_DEP, id = "c")
    public void testResolveWithNull() {
        assertNull(getCurrentBeanContainer().resolve(null));
    }

    @Test
    @SpecAssertion(section = BM_RESOLVE_AMBIGUOUS_DEP, id = "d")
    public void testResolveWithEmptySet() {
        assertNull(getCurrentBeanContainer().resolve(Collections.<Bean<? extends Integer>> emptySet()));
        assertNull(getCurrentBeanContainer().resolve(new HashSet<Bean<? extends String>>()));
    }

    @Test
    @SpecAssertion(section = BM_OBTAIN_CONTEXTS, id = "a")
    public void testGetContexts() {
        // test for scope without any impl, this should be empty set
        Collection<Context> noImpl = getCurrentBeanContainer().getContexts(NoImplScope.class);
        assertEquals(noImpl.size(), 0);

        // custom scope with two existing implementations
        Collection<Context> customContextImpls = getCurrentBeanContainer().getContexts(CustomScoped.class);
        assertEquals(customContextImpls.size(), 2);

        // test any built-in scope for completeness
        // deliberately skips assertions on exact number of implementations as that can differ
        Collection<Context> builtInContextImpls = getCurrentBeanContainer().getContexts(Singleton.class);
        assertTrue(builtInContextImpls.size() >= 1);
    }

}
