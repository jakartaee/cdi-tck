/*
 * Copyright 2024, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.invokers.invalid;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;

import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.DeploymentException;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessManagedBean;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "4.1")
@Test(groups = CDI_FULL)
public class DecoratorInvokerTest extends AbstractTest {
    @Deployment
    @ShouldThrowException(DeploymentException.class)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(DecoratorInvokerTest.class)
                .withClasses(MyService.class, MyDecorator.class)
                .withExtension(TestExtension.class)
                .build();
    }

    public static class TestExtension implements Extension {
        public void myDecoratorRegistration(@Observes ProcessManagedBean<MyDecorator> pmb) {
            pmb.getAnnotatedBeanClass()
                    .getMethods()
                    .stream()
                    .filter(it -> "hello".equals(it.getJavaMember().getName()))
                    .forEach(it -> pmb.createInvoker(it).build());
        }
    }

    @Test
    @SpecAssertion(section = Sections.BUILDING_INVOKER_FULL, id = "a")
    public void trigger() {
    }

    public interface MyService {
        String hello();
    }

    @Decorator
    @Priority(1)
    public static class MyDecorator implements MyService {
        @Inject
        @Delegate
        MyService delegate;

        @Override
        public String hello() {
            return "decorated: " + delegate.hello();
        }
    }
}