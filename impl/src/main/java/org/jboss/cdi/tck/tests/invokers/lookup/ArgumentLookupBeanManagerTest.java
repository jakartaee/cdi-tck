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
package org.jboss.cdi.tck.tests.invokers.lookup;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.InvokerFactory;
import jakarta.enterprise.inject.build.compatible.spi.Registration;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.invoke.Invoker;
import jakarta.enterprise.util.TypeLiteral;

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
public class ArgumentLookupBeanManagerTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(ArgumentLookupBeanManagerTest.class)
                .withClasses(MyService.class, MyDependency.class)
                .withBuildCompatibleExtension(TestExtension.class)
                .withClasses(InvokerHolder.class, InvokerHolderCreator.class, InvokerHolderExtensionBase.class)
                .build();
    }

    public static class TestExtension extends InvokerHolderExtensionBase implements BuildCompatibleExtension {
        @Registration(types = MyService.class)
        public void myServiceRegistration(BeanInfo bean, InvokerFactory invokers) {
            registerInvokers(bean, invokers, Set.of("hello"), builder -> builder.withArgumentLookup(0));
        }

        @Synthesis
        public void synthesis(SyntheticComponents syn) {
            synthesizeInvokerHolder(syn);
        }
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.CONFIGURING_LOOKUPS, id = "a")
    @SpecAssertion(section = Sections.CONFIGURING_LOOKUPS, id = "de")
    @SpecAssertion(section = Sections.CONFIGURING_LOOKUPS, id = "i")
    public void test(MyService service, InvokerHolder invokers) throws Exception {
        Invoker<MyService, String> invoker = invokers.get("hello");
        assertEquals("foobar0", invoker.invoke(service, new Object[] { null }));
        assertEquals("foobar1", invoker.invoke(service, new Object[] { null }));

        assertEquals(MyService.observed, List.of("foo", "bar", "foo", "bar"));

        assertEquals(2, MyDependency.CREATED);
        assertEquals(2, MyDependency.DESTROYED);
    }

    @ApplicationScoped
    static class MyService {
        static final List<String> observed = new ArrayList<>();

        public String hello(BeanManager beanManager) {
            Instance<Object> instance = beanManager.createInstance();
            MyDependency dependency = instance.select(MyDependency.class).get();
            int id = dependency.getId();
            instance.destroy(dependency);

            beanManager.getEvent().select(new TypeLiteral<List<String>>() {
            }).fire(List.of("foo", "bar"));

            return "foobar" + id;
        }

        public void observe(@Observes List<String> event) {
            observed.addAll(event);
        }
    }

    @Dependent
    static class MyDependency {
        static int CREATED = 0;

        static int DESTROYED = 0;

        private int id;

        @PostConstruct
        public void init() {
            this.id = CREATED++;
        }

        @PreDestroy
        public void destroy() {
            DESTROYED++;
        }

        public int getId() {
            return id;
        }
    }
}
