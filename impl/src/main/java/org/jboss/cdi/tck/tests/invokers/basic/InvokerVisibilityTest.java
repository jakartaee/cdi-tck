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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.InvokerFactory;
import jakarta.enterprise.inject.build.compatible.spi.Registration;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import jakarta.enterprise.lang.model.declarations.MethodInfo;
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

import java.util.List;

import static org.testng.Assert.assertEquals;

@SpecVersion(spec = "cdi", version = "4.1")
public class InvokerVisibilityTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(InvokerVisibilityTest.class)
                .withClasses(MyPublicService.class, MyProtectedService.class, MyPackagePrivateService.class)
                .withBuildCompatibleExtension(TestExtension.class)
                .withClasses(InvokerHolder.class, InvokerHolderCreator.class, InvokerHolderExtensionBase.class)
                .build();
    }

    public static class TestExtension extends InvokerHolderExtensionBase implements BuildCompatibleExtension {
        @Registration(types = MyPublicService.class)
        public void myPublicServiceRegistration(BeanInfo bean, InvokerFactory invokers) {
            invokersForAllMethods(bean, invokers);
        }

        @Registration(types = MyProtectedService.class)
        public void myProtectedServiceRegistration(BeanInfo bean, InvokerFactory invokers) {
            invokersForAllMethods(bean, invokers);
        }

        @Registration(types = MyPackagePrivateService.class)
        public void myPackagePrivateServiceRegistration(BeanInfo bean, InvokerFactory invokers) {
            invokersForAllMethods(bean, invokers);
        }

        private void invokersForAllMethods(BeanInfo bean, InvokerFactory invokers) {
            for (MethodInfo method : bean.declaringClass().methods()) {
                registerInvoker(bean.declaringClass().simpleName() + "_" + method.name(),
                        invokers.createInvoker(bean, method).build());
            }
        }

        @Synthesis
        public void synthesis(SyntheticComponents syn) {
            synthesizeInvokerHolder(syn);
        }
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.BUILDING_INVOKER, id = "be")
    public void test(Instance<Object> lookup, InvokerHolder invokers) throws Exception {
        for (Class<?> clazz : List.of(MyPublicService.class, MyProtectedService.class, MyPackagePrivateService.class)) {
            Object service = lookup.select(clazz).get();

            for (String method : List.of("hello", "helloProtected", "helloPackagePrivate",
                    "helloStatic", "helloProtectedStatic", "helloPackagePrivateStatic")) {
                String id = clazz.getSimpleName() + "_" + method;
                assertEquals(invokers.get(id).invoke(service, null), id);
            }
        }
    }

    @ApplicationScoped
    public static class MyPublicService {
        public String hello() {
            return "MyPublicService_hello";
        }

        protected String helloProtected() {
            return "MyPublicService_helloProtected";
        }

        String helloPackagePrivate() {
            return "MyPublicService_helloPackagePrivate";
        }

        public static String helloStatic() {
            return "MyPublicService_helloStatic";
        }

        protected static String helloProtectedStatic() {
            return "MyPublicService_helloProtectedStatic";
        }

        static String helloPackagePrivateStatic() {
            return "MyPublicService_helloPackagePrivateStatic";
        }
    }

    @ApplicationScoped
    protected static class MyProtectedService {
        public String hello() {
            return "MyProtectedService_hello";
        }

        protected String helloProtected() {
            return "MyProtectedService_helloProtected";
        }

        String helloPackagePrivate() {
            return "MyProtectedService_helloPackagePrivate";
        }

        public static String helloStatic() {
            return "MyProtectedService_helloStatic";
        }

        protected static String helloProtectedStatic() {
            return "MyProtectedService_helloProtectedStatic";
        }

        static String helloPackagePrivateStatic() {
            return "MyProtectedService_helloPackagePrivateStatic";
        }
    }

    @ApplicationScoped
    static class MyPackagePrivateService {
        public String hello() {
            return "MyPackagePrivateService_hello";
        }

        protected String helloProtected() {
            return "MyPackagePrivateService_helloProtected";
        }

        String helloPackagePrivate() {
            return "MyPackagePrivateService_helloPackagePrivate";
        }

        public static String helloStatic() {
            return "MyPackagePrivateService_helloStatic";
        }

        protected static String helloProtectedStatic() {
            return "MyPackagePrivateService_helloProtectedStatic";
        }

        static String helloPackagePrivateStatic() {
            return "MyPackagePrivateService_helloPackagePrivateStatic";
        }
    }
}
