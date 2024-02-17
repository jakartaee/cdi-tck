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
package org.jboss.cdi.tck.tests.full.invokers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessManagedBean;
import jakarta.enterprise.invoke.Invoker;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.testng.Assert.assertEquals;

@SpecVersion(spec = "cdi", version = "4.1")
@Test(groups = CDI_FULL)
public class SimpleInvokerTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(SimpleInvokerTest.class)
                .withClasses(MyService.class)
                .withExtension(TestExtension.class)
                .build();
    }

    public static class TestExtension implements Extension {
        private Map<String, Invoker<?, ?>> invokers = new HashMap<>();

        public void myServiceRegistration(@Observes ProcessManagedBean<MyService> pmb) {
            pmb.getAnnotatedBeanClass()
                    .getMethods()
                    .stream()
                    .filter(it -> "hello".equals(it.getJavaMember().getName()))
                    .forEach(it -> invokers.put(it.getJavaMember().getName(), pmb.createInvoker(it).build()));
        }

        public <T, R> Invoker<T, R> getInvoker(String name) {
            return (Invoker<T, R>) invokers.get(name);
        }
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.METHOD_INVOKERS_FULL, id = "a")
    public void test(MyService service) throws Exception {
        Invoker<MyService, String> hello = getCurrentManager().getExtension(TestExtension.class).getInvoker("hello");
        assertEquals(hello.invoke(service, new Object[]{1, List.of()}), "foobar1[]");
    }

    @ApplicationScoped
    public static class MyService {
        public String hello(int param1, List<String> param2) {
            return "foobar" + param1 + param2;
        }
    }
}
