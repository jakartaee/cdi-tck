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

import static org.jboss.cdi.tck.cdi.Sections.BIZ_METHOD;
import static org.jboss.cdi.tck.cdi.Sections.ENABLED_DECORATORS;
import static org.jboss.cdi.tck.cdi.Sections.ENABLED_INTERCEPTORS;
import static org.testng.Assert.assertEquals;

import java.util.List;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class DecoratorAndInterceptorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(DecoratorAndInterceptorTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).getOrCreateInterceptors()
                                .clazz(FooInterceptor1.class.getName(), FooInterceptor2.class.getName()).up()
                                .getOrCreateDecorators().clazz(FooDecorator1.class.getName(), FooDecorator2.class.getName()).up())
                .build();
    }

    /**
     * Test that interceptor chain is called before decorator chain and that invocations of decorator methods during method or
     * lifecycle callback interception are not business method invocation, and therefore are not intercepted by interceptors.
     */
    @Test
    @SpecAssertions({ @SpecAssertion(section = ENABLED_DECORATORS, id = "b"), @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "i"),
            @SpecAssertion(section = BIZ_METHOD, id = "ka"), @SpecAssertion(section = BIZ_METHOD, id = "kb"),
            @SpecAssertion(section = BIZ_METHOD, id = "kc"), @SpecAssertion(section = BIZ_METHOD, id = "kd") })
    public void testMethodCallbacks() {

        ActionSequence.reset();
        useFoo();

        List<String> sequence = ActionSequence.getSequenceData();
        assertEquals(sequence.size(), 4);
        assertEquals(sequence.get(0), FooInterceptor1.NAME);
        assertEquals(sequence.get(1), FooInterceptor2.NAME);
        assertEquals(sequence.get(2), FooDecorator1.NAME);
        assertEquals(sequence.get(3), FooDecorator2.NAME);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BIZ_METHOD, id = "kc"), @SpecAssertion(section = BIZ_METHOD, id = "kd") })
    public void testLifecycleCallbacks() {

        ActionSequence.reset();
        useFoo();

        List<String> postConstruct = ActionSequence.getSequenceData("postConstruct");
        assertEquals(postConstruct.size(), 2);
        assertEquals(postConstruct.get(0), FooInterceptor1.NAME);
        assertEquals(postConstruct.get(1), FooInterceptor2.NAME);

        List<String> preDestroy = ActionSequence.getSequenceData("preDestroy");
        assertEquals(preDestroy.size(), 2);
        assertEquals(preDestroy.get(0), FooInterceptor1.NAME);
        assertEquals(preDestroy.get(1), FooInterceptor2.NAME);
    }

    private String useFoo() {
        String fooClass = null;
        Bean<Foo> bean = getUniqueBean(Foo.class);
        CreationalContext<Foo> ctx = getCurrentManager().createCreationalContext(bean);
        Foo foo = bean.create(ctx);
        fooClass = foo.getClass().getName();
        foo.doSomething();
        bean.destroy(foo, ctx);
        return fooClass;
    }

}
