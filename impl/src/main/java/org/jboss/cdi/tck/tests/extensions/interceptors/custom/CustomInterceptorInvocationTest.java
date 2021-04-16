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

import static org.jboss.cdi.tck.cdi.Sections.AFTER_BEAN_DISCOVERY;
import static org.jboss.cdi.tck.cdi.Sections.INTERCEPTOR;
import static org.testng.Assert.assertTrue;

import jakarta.inject.Inject;
import jakarta.interceptor.Interceptor;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.HierarchyDiscovery;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Registers an extension-provided implementation of the {@link Interceptor} interface and verifies that the implementation is
 * invoked upon invocation of an intercepted method.
 * 
 * <p>
 * This test was originally part of Weld test suite - WELD-997.
 * <p>
 * 
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 * @author Martin Kouba
 */
@Test
@SpecVersion(spec = "cdi", version = "2.0")
public class CustomInterceptorInvocationTest extends AbstractTest {

    @Inject
    private InterceptedBean bean;

    @Deployment
    public static WebArchive createTestArchive() {

        return new WebArchiveBuilder()
                .withTestClass(CustomInterceptorInvocationTest.class)
                .withClasses(HierarchyDiscovery.class, AbstractInterceptor.class, CustomInterceptor.class,
                        CustomInterceptorExtension.class, FooInterceptor.class, FooInterceptorBinding.class,
                        InterceptedBean.class)
                .withExtension(CustomInterceptorExtension.class)
                .withBeansXml(new BeansXml().interceptors(FooInterceptor.class)).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = AFTER_BEAN_DISCOVERY, id = "dd"), @SpecAssertion(section = INTERCEPTOR, id = "d") })
    public void testCustomInterceptorInvocation() {
        CustomInterceptor.reset();
        FooInterceptor.reset();
        bean.foo();
        assertTrue(CustomInterceptor.isInvoked());
        assertTrue(FooInterceptor.isInvoked());
    }
}
