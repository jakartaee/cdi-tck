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
package org.jboss.cdi.tck.tests.implementation.simple.lifecycle.unproxyable;

import static org.jboss.cdi.tck.cdi.Sections.CONTEXTUAL_REFERENCE;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.UnproxyableResolutionException;
import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0-EDR1")
public class UnproxyableManagedBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(UnproxyableManagedBeanTest.class).build();
    }

    @Test(expectedExceptions = UnproxyableResolutionException.class)
    @SpecAssertion(section = CONTEXTUAL_REFERENCE, id = "a")
    public void testNormalScopedUnproxyableBeanWithPrivateConstructorResolution() {
        testObtainingContextualReference(UnproxyableBean.class);
    }

    @Test(expectedExceptions = UnproxyableResolutionException.class)
    @SpecAssertion(section = CONTEXTUAL_REFERENCE, id = "a")
    public void testNormalScopedUnproxyableBeanWithFinalClassResolution() {
        testObtainingContextualReference(UnproxyableFinalClass.class);
    }

    @Test(expectedExceptions = UnproxyableResolutionException.class)
    @SpecAssertion(section = CONTEXTUAL_REFERENCE, id = "a")
    public void testNormalScopedUnproxyableBeanWithPublicFinalMethodResolution() {
        testObtainingContextualReference(UnproxyableBeanWithPublicFinalMethod.class);
    }

    @Test(expectedExceptions = UnproxyableResolutionException.class)
    @SpecAssertion(section = CONTEXTUAL_REFERENCE, id = "a")
    public void testNormalScopedUnproxyableBeanWithProtectedFinalMethodResolution() {
        testObtainingContextualReference(UnproxyableBeanWithProtectedFinalMethod.class);
    }

    @Test(expectedExceptions = UnproxyableResolutionException.class)
    @SpecAssertion(section = CONTEXTUAL_REFERENCE, id = "a")
    public void testNormalScopedUnproxyableBeanWithPackagePrivateFinalMethodResolution() {
        testObtainingContextualReference(UnproxyableBeanWithPackagePrivateFinalMethod.class);
    }

    private <T> void testObtainingContextualReference(Class<T> t) {
        Bean<T> bean = getUniqueBean(t);
        CreationalContext<T> ctx = getCurrentManager().createCreationalContext(bean);
        getCurrentManager().getReference(bean, t, ctx);
    }

}
