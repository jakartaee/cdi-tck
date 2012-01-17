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
package org.jboss.cdi.tck.tests.decorators.invocation;

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

/**
 * @author pmuir
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class DecoratorInvocationTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(DecoratorInvocationTest.class)
                .withExcludedClasses(EJBDecoratorInvocationTest.class.getName(), PigSty.class.getName(),
                        PigStyImpl.class.getName(), PigStyDecorator.class.getName(), Pig.class.getName())
                .withBeansXml(
                        Descriptors
                                .create(BeansDescriptor.class)
                                .createDecorators()
                                .clazz(BarDecorator.class.getName(), FooDecorator1.class.getName(),
                                        FooDecorator2.class.getName(), TimestampLogger.class.getName()).up()).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "8.4", id = "a"), @SpecAssertion(section = "8.4", id = "c"),
            @SpecAssertion(section = "8.4", id = "b"), @SpecAssertion(section = "8.1.3", id = "d"),
            @SpecAssertion(section = "8.1.2", id = "f"), @SpecAssertion(section = "7.2", id = "b") })
    public void testDecoratorInvocation() {
        TimestampLogger.reset();
        MockLogger.reset();
        getInstanceByType(CowShed.class).milk();
        assert TimestampLogger.getMessage().equals(CowShed.MESSAGE);
        assert MockLogger.getMessage().equals(TimestampLogger.PREFIX + CowShed.MESSAGE);
        assert MockLogger.isInitializeCalled();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "8.4", id = "d"), @SpecAssertion(section = "8.4", id = "e"),
            @SpecAssertion(section = "8.4", id = "f"),
            // @SpecAssertion(section="8.4", id="a"),
            @SpecAssertion(section = "8.1.3", id = "d"), @SpecAssertion(section = "8.1.2", id = "f"),
            @SpecAssertion(section = "7.2", id = "kb") })
    public void testChainedDecoratorInvocation() {
        FooDecorator1.reset();
        FooDecorator2.reset();
        FooImpl.reset();
        getInstanceByType(CowShed.class).washDown();
        assert FooDecorator1.getMessage().equals(CowShed.MESSAGE);
        assert FooDecorator2.getMessage().equals(CowShed.MESSAGE + FooDecorator1.SUFFIX);
        assert FooImpl.getMessage().equals(CowShed.MESSAGE + FooDecorator1.SUFFIX + FooDecorator2.SUFFIX);
        FooDecorator1.reset();
        FooDecorator2.reset();
        FooImpl.reset();
    }

    @Test
    @SpecAssertion(section = "8.1.2", id = "g")
    // WELD-430
    public void testDecoratorInvokesDelegateMethodOutsideOfBusinessMethodInterception() {
        assert getInstanceByType(Bar.class).foo();
        try {
            BarDecorator.invokeFooOutsideOfBusinessMethodInterception();
        } catch (Throwable t) {
            assert isThrowablePresent(IllegalStateException.class, t);
            return;
        }
        assert false;
    }
}
