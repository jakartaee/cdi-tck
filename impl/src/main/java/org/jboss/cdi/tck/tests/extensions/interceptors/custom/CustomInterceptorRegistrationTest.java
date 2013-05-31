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
package org.jboss.cdi.tck.tests.extensions.interceptors.custom;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.ABD;

import javax.interceptor.Interceptor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.HierarchyDiscovery;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Registers an extension-provided implementation of the {@link Interceptor} interface.
 * 
 * <p>
 * This test was originally part of Weld test suite. Causes deployment error on Weld if an intercepted bean is passivation
 * capable - WELD-996.
 * <p>
 * 
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class CustomInterceptorRegistrationTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {

        return new WebArchiveBuilder()
                .withTestClass(CustomInterceptorRegistrationTest.class)
                .withClasses(HierarchyDiscovery.class, AbstractInterceptor.class, CustomInterceptor.class,
                        CustomInterceptorExtension.class, FooInterceptor.class, FooInterceptorBinding.class,
                        InterceptedBean.class, InterceptedSerializableBean.class)
                .withExtension(CustomInterceptorExtension.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createInterceptors().clazz(FooInterceptor.class.getName())
                                .up()).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ABD, id = "dd") })
    public void testCustomInterceptorRegistration() {
        // noop, we verify that the app deploys
    }

}
