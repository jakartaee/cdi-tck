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
package org.jboss.cdi.tck.tests.invokers.basic;

import static org.testng.Assert.assertThrows;

import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.InvokerFactory;
import jakarta.enterprise.inject.build.compatible.spi.Registration;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import jakarta.enterprise.invoke.Invoker;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.invokers.InvokerHolder;
import org.jboss.cdi.tck.tests.invokers.InvokerHolderCreator;
import org.jboss.cdi.tck.tests.invokers.InvokerHolderExtensionBase;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "4.1")
public class MissingArgumentsInvokerTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(MissingArgumentsInvokerTest.class)
                .withClasses(MyService.class)
                .withBuildCompatibleExtension(TestExtension.class)
                .withClasses(InvokerHolder.class, InvokerHolderCreator.class, InvokerHolderExtensionBase.class)
                .build();
    }

    public static class TestExtension extends InvokerHolderExtensionBase implements BuildCompatibleExtension {
        @Registration(types = MyService.class)
        public void myServiceRegistration(BeanInfo bean, InvokerFactory invokers) {
            registerInvokers(bean, invokers, Set.of("hello", "helloStatic"));
        }

        @Synthesis
        public void synthesis(SyntheticComponents syn) {
            synthesizeInvokerHolder(syn);
        }
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.BEHAVIOR_OF_INVOKE, id = "i")
    @SpecAssertion(section = Sections.BEHAVIOR_OF_INVOKE, id = "j")
    public void test(MyService service, InvokerHolder invokers) {
        Invoker<MyService, String> hello = invokers.get("hello");
        assertThrows(RuntimeException.class, () -> {
            hello.invoke(service, null);
        });
        assertThrows(RuntimeException.class, () -> {
            hello.invoke(service, new Object[] {});
        });
        assertThrows(RuntimeException.class, () -> {
            hello.invoke(new MyService(), new Object[] { 1 });
        });

        Invoker<MyService, String> helloStatic = invokers.get("helloStatic");
        assertThrows(RuntimeException.class, () -> {
            helloStatic.invoke(null, null);
        });
        assertThrows(RuntimeException.class, () -> {
            helloStatic.invoke(null, new Object[] { "" });
        });
        assertThrows(RuntimeException.class, () -> {
            hello.invoke(null, new Object[] {});
        });
    }

    @ApplicationScoped
    public static class MyService {
        public String hello(int param1, String param2) {
            return "foobar_" + param1 + param2;
        }

        public static String helloStatic(String param1, int param2) {
            return "quux_" + param1 + param2;
        }
    }
}
