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
package org.jboss.cdi.tck.tests.invokers.invalid;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.InvokerFactory;
import jakarta.enterprise.inject.build.compatible.spi.Registration;
import jakarta.enterprise.inject.spi.DeploymentException;

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
public class PrivateMethodInvokerTest extends AbstractTest {
    @Deployment
    @ShouldThrowException(DeploymentException.class)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(PrivateMethodInvokerTest.class)
                .withClasses(MyService.class)
                .withBuildCompatibleExtension(TestExtension.class)
                .build();
    }

    public static class TestExtension implements BuildCompatibleExtension {
        @Registration(types = MyService.class)
        public void myServiceRegistration(BeanInfo bean, InvokerFactory invokers) {
            bean.declaringClass()
                    .methods()
                    .stream()
                    .filter(it -> "hello".equals(it.name()))
                    .forEach(it -> invokers.createInvoker(bean, it).build());
        }
    }

    @Test
    @SpecAssertion(section = Sections.BUILDING_INVOKER, id = "ba")
    public void trigger() {
    }

    @ApplicationScoped
    public static class MyService {
        private String hello() {
            return "foobar";
        }
    }
}
