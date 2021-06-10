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
package org.jboss.cdi.tck.tests.interceptors.invocation;

import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY_STEPS;
import static org.jboss.cdi.tck.cdi.Sections.BIZ_METHOD;
import static org.jboss.cdi.tck.cdi.Sections.INITIALIZER_METHODS;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.DependentInstance;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class InterceptorInvocationTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InterceptorInvocationTest.class)
                .withBeansXml(new BeansXml().interceptors(AlmightyInterceptor.class)).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BIZ_METHOD, id = "a"),
            @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "g"),  @SpecAssertion(section = BIZ_METHOD, id = "aa")})
    public void testManagedBeanIsIntercepted() {

        AlmightyInterceptor.reset();
        Missile missile = getContextualReference(Missile.class);
        missile.fire();

        assertTrue(AlmightyInterceptor.methodIntercepted);
        assertNotNull(missile.getWarhead()); // test that injection works

        AlmightyInterceptor.reset();
        // Test interception of application scoped bean invocation
        Watcher watcher = getContextualReference(Watcher.class);
        watcher.ping();

        assertTrue(AlmightyInterceptor.methodIntercepted);
        assertTrue(AlmightyInterceptor.lifecycleCallbackIntercepted);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BIZ_METHOD, id = "ad"), @SpecAssertion(section = INITIALIZER_METHODS, id = "f") })
    public void testInitializerMethodsNotIntercepted() {

        AlmightyInterceptor.reset();
        Missile missile = getContextualReference(Missile.class);

        assertFalse(AlmightyInterceptor.methodIntercepted);
        assertTrue(missile.initCalled()); // this call is intercepted
        assertTrue(AlmightyInterceptor.methodIntercepted);
    }

    @Test
    @SpecAssertion(section = BIZ_METHOD, id = "ia")
    public void testProducerMethodsAreIntercepted() {

        AlmightyInterceptor.reset();
        getContextualReference(Wheat.class);

        assertTrue(AlmightyInterceptor.methodIntercepted);
    }

    @Test
    @SpecAssertion(section = BIZ_METHOD, id = "ic")
    public void testDisposerMethodsAreIntercepted() {

        AlmightyInterceptor.reset();

        DependentInstance<Wheat> bean = newDependentInstance(Wheat.class);
        Wheat instance = bean.get();
        assertNotNull(instance);
        AlmightyInterceptor.methodIntercepted = false;
        bean.destroy();

        assertTrue(WheatProducer.destroyed);
        assertTrue(AlmightyInterceptor.methodIntercepted);
    }

    @Test
    @SpecAssertion(section = BIZ_METHOD, id = "ie")
    public void testObserverMethodsAreIntercepted() {

        AlmightyInterceptor.reset();
        getCurrentManager().getEvent().select(Missile.class).fire(new Missile());

        assertTrue(MissileObserver.observed);
        assertTrue(AlmightyInterceptor.methodIntercepted);
    }

    @Test
    @SpecAssertion(section = BIZ_METHOD, id = "j")
    public void testLifecycleCallbacksAreIntercepted() {

        AlmightyInterceptor.reset();
        Rye rye = getContextualReference(Rye.class);
        rye.ping();

        assertTrue(AlmightyInterceptor.methodIntercepted);
        assertTrue(AlmightyInterceptor.lifecycleCallbackIntercepted);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BIZ_METHOD, id = "m"), @SpecAssertion(section = BIZ_METHOD, id = "g") })
    public void testObjectMethodsAreNotIntercepted() {

        AlmightyInterceptor.reset();
        getContextualReference(Missile.class).toString();

        assertFalse(AlmightyInterceptor.methodIntercepted);
        assertTrue(AlmightyInterceptor.lifecycleCallbackIntercepted);
    }

}
