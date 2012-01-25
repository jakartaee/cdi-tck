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
package org.jboss.cdi.tck.tests.decorators.interceptor;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class DecoratorAndInterceptorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(DecoratorAndInterceptorTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createInterceptors().clazz(FooInterceptor.class.getName())
                                .up().createDecorators().clazz(FooDecorator.class.getName()).up()).build();
    }

    /**
     * Test that interceptor is called before decorator and that invocations of decorator methods during method or lifecycle
     * callback interception are not business method invocation, and therefore are not intercepted by interceptors.
     */
    @Test
    @SpecAssertions({ @SpecAssertion(section = "8.2", id = "f"), @SpecAssertion(section = "7.2", id = "ka") })
    public void testInterceptorCalledBeforeDecorator() {

        CallStore.resetCallers();

        Foo foo = getInstanceByType(Foo.class);
        foo.doSomething();

        List<String> callers = CallStore.getCallers();
        assertEquals(callers.size(), 2);
        assertTrue(callers.get(0).equals(FooInterceptor.NAME));
        assertTrue(callers.get(1).equals(FooDecorator.NAME));

        List<String> lifecycleCallers = CallStore.getLifecycleCallers();
        assertEquals(lifecycleCallers.size(), 2);
        assertTrue(lifecycleCallers.contains(FooDecorator.class.getName()));
        assertTrue(lifecycleCallers.contains(foo.getClass().getName()));
    }

}
