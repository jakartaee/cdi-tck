/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.se.context.activation.interceptor;

import static org.jboss.cdi.tck.TestGroups.SE;
import static org.jboss.cdi.tck.cdi.Sections.ACTIVATING_REQUEST_CONTEXT;
import static org.jboss.cdi.tck.cdi.Sections.REQUEST_CONTEXT;

import java.io.IOException;
import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import org.jboss.arquillian.container.se.api.ClassPath;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@Test(groups = SE)
@SpecVersion(spec = "cdi", version = "2.0-PFD")
public class ActivateRequestContextByInterceptorTest extends Arquillian {

    @Deployment
    public static Archive<?> deployment() throws IOException {
        final JavaArchive bda1 = ShrinkWrap.create(JavaArchive.class)
                .addPackage(ActivateRequestContextByInterceptorTest.class.getPackage())
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        return ClassPath.builder().add(bda1).build();

    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = REQUEST_CONTEXT, id = "a"), @SpecAssertion(section = REQUEST_CONTEXT, id = "b"),
            @SpecAssertion(section = REQUEST_CONTEXT, id = "c"), @SpecAssertion(section = ACTIVATING_REQUEST_CONTEXT, id = "a"),
            @SpecAssertion(section = ACTIVATING_REQUEST_CONTEXT, id = "f") })
    public void classInterceptorRequestContextActivation() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer container = seContainerInitializer
                .initialize()) {
            ClassInterceptorContextActivator activator = container.select(ClassInterceptorContextActivator.class).get();
            Assert.assertEquals(activator.callRequestScopeBean(), 11);
            RequestContextObserver requestContextObserver = container.select(RequestContextObserver.class).get();
            Assert.assertEquals(requestContextObserver.getInitCounter(), 1);
            Assert.assertEquals(requestContextObserver.getBeforeDestroyedCounter(), 1);
            Assert.assertEquals(requestContextObserver.getDestroyedCounter(), 1);
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = REQUEST_CONTEXT, id = "a"), @SpecAssertion(section = REQUEST_CONTEXT, id = "b"),
            @SpecAssertion(section = REQUEST_CONTEXT, id = "c"), @SpecAssertion(section = ACTIVATING_REQUEST_CONTEXT, id = "a"),
            @SpecAssertion(section = ACTIVATING_REQUEST_CONTEXT, id = "f") })
    public void methodInterceptorRequestContextActivation() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer container = seContainerInitializer
                .initialize()) {
            MethodInterceptorContextActivator activator = container.select(MethodInterceptorContextActivator.class).get();
            Assert.assertEquals(activator.callRequestScopeBean(), 11);
            RequestContextObserver requestContextObserver = container.select(RequestContextObserver.class).get();
            Assert.assertEquals(requestContextObserver.getInitCounter(), 1);
            Assert.assertEquals(requestContextObserver.getBeforeDestroyedCounter(), 1);
            Assert.assertEquals(requestContextObserver.getDestroyedCounter(), 1);
        }
    }

    // indirectly test ActivateRequestContext interceptor priority
    @Test
    @SpecAssertions({ @SpecAssertion(section = ACTIVATING_REQUEST_CONTEXT, id = "g") })
    public void builtInInterceptorHasGivenPriority() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer container = seContainerInitializer
                .initialize()) {
            ClassInterceptorContextActivator activator = container.select(ClassInterceptorContextActivator.class).get();
            activator.callRequestScopeBean();
            Assert.assertFalse(BeforeActivationInterceptor.isRequestContextActive.get());
            Assert.assertTrue(AfterActivationInterceptor.isRequestContextActive.get());
        }
    }

}
