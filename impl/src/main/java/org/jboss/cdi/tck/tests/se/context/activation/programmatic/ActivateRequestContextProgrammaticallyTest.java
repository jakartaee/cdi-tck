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
package org.jboss.cdi.tck.tests.se.context.activation.programmatic;

import static org.jboss.cdi.tck.TestGroups.SE;
import static org.jboss.cdi.tck.cdi.Sections.ACTIVATING_REQUEST_CONTEXT;

import java.io.IOException;
import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.control.RequestContextController;
import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

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
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class ActivateRequestContextProgrammaticallyTest extends Arquillian {

    @Deployment
    public static Archive<?> deployment() throws IOException {
        final JavaArchive bda1 = ShrinkWrap.create(JavaArchive.class)
                .addPackage(ActivateRequestContextProgrammaticallyTest.class.getPackage())
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        return ClassPath.builder().add(bda1).build();

    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ACTIVATING_REQUEST_CONTEXT, id = "a"),
            @SpecAssertion(section = ACTIVATING_REQUEST_CONTEXT, id = "c"),
            @SpecAssertion(section = ACTIVATING_REQUEST_CONTEXT, id = "da"),
            @SpecAssertion(section = ACTIVATING_REQUEST_CONTEXT, id = "e") })
    public void programmaticRequestContextActivation() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer container = seContainerInitializer
                .initialize()) {
            TestContextActivator contextActivator = container.select(TestContextActivator.class).get();
            boolean activated = contextActivator.activate();
            Assert.assertTrue(activated);
            Assert.assertFalse(contextActivator.activate());
            contextActivator.callRequestScopeBean();
            RequestScopeCounter counter = container.select(RequestScopeCounter.class).get();
            Assert.assertEquals(contextActivator.callRequestScopeBean(), 2);
            contextActivator.deactivate();

            contextActivator.activate();
            BeanManager beanManager = container.getBeanManager();
            Assert.assertTrue(beanManager.getContext(RequestScoped.class).isActive());
            Assert.assertEquals(contextActivator.callRequestScopeBean(), 1);
            contextActivator.deactivate();
            try {
                beanManager.getContext(RequestScoped.class).isActive();
                Assert.fail(ContextNotActiveException.class.getSimpleName() + " was not thrown !");
            } catch (ContextNotActiveException e) {

            }
        }
    }

    @Test
    @SpecAssertion(section = ACTIVATING_REQUEST_CONTEXT, id = "b")
    public void requestControllerBuiltInBeanAvailable() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer container = seContainerInitializer
                .initialize()) {
            BeanManager beanManager = container.getBeanManager();
            Bean<?> requestControllerBean = beanManager.resolve(beanManager.getBeans(RequestContextController.class));
            Assert.assertEquals(Dependent.class, requestControllerBean.getScope());
        }
    }

    @Test
    @SpecAssertion(section = ACTIVATING_REQUEST_CONTEXT, id = "dc")
    public void requestControllerDeactivatedThrowsException() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer container = seContainerInitializer
                .initialize()) {
            TestContextActivator contextActivator = container.select(TestContextActivator.class).get();
            try {
                contextActivator.deactivate();
                Assert.fail(ContextNotActiveException.class.getSimpleName() + " was not thrown !");
            } catch (ContextNotActiveException e) {
            }
        }
    }

}
