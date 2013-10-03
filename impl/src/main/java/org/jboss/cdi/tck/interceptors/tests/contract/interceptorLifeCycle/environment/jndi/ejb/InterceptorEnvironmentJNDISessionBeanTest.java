/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.interceptors.tests.contract.interceptorLifeCycle.environment.jndi.ejb;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import javax.enterprise.inject.Instance;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.Testable;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.application6.ApplicationDescriptor;
import org.jboss.shrinkwrap.descriptor.api.spec.se.manifest.ManifestDescriptor;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Matus Abaffy
 */
@SpecVersion(spec = "int", version = "1.2")
public class InterceptorEnvironmentJNDISessionBeanTest extends AbstractTest {

    private static final String GREETING = "greeting";
    private static final String JAVA_LANG_STRING = "java.lang.String";

    private static final String HELLO = "Hello";
    private static final String BYE = "Bye";

    @Deployment
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder()
                .withTestClassDefinition(InterceptorEnvironmentJNDISessionBeanTest.class)
                .withClasses(BarBinding.class, Animal.class)
                .noDefaultWebModule().build();

        StringAsset applicationXml = new StringAsset(Descriptors.create(ApplicationDescriptor.class)
                .version(EnterpriseArchiveBuilder.DEFAULT_APP_VERSION).applicationName("Test").createModule()
                .ejb(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME).up().createModule().getOrCreateWeb().webUri("test1.war")
                .contextRoot("/test1").up().up().createModule().getOrCreateWeb().webUri("test2.war").contextRoot("/test2").up()
                .up().exportAsString());
        enterpriseArchive.setApplicationXML(applicationXml);

        WebArchive fooArchive = new WebArchiveBuilder().notTestArchive().withName("test1.war")
                .withClasses(BarInterceptor.class, Foo.class, FooInterceptor.class, FooBinding.class, Dog.class)
                .withWebXml(
                        Descriptors.create(WebAppDescriptor.class).createEnvEntry().envEntryName(GREETING)
                                .envEntryType(JAVA_LANG_STRING).envEntryValue(BYE).up())
                .withDefaultEjbModuleDependency()
                .build();
        enterpriseArchive.addAsModule(fooArchive);

        WebArchive barArchive = Testable.archiveToTest(new WebArchiveBuilder().notTestArchive().withName("test2.war")
                .withClasses(InterceptorEnvironmentJNDISessionBeanTest.class, Bar.class, Cat.class)
                .withWebXml(
                        Descriptors.create(WebAppDescriptor.class).createEnvEntry().envEntryName(GREETING)
                                .envEntryType(JAVA_LANG_STRING).envEntryValue(HELLO).up())
                .build()
                .setManifest(
                        new StringAsset(Descriptors.create(ManifestDescriptor.class)
                                .addToClassPath(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME)
                                .addToClassPath("test1.war")
                                .exportAsString())));
        enterpriseArchive.addAsModule(barArchive);

        return enterpriseArchive;
    }

    @Test(groups = JAVAEE_FULL, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = "2.2.1", id = "a")
    public void testInterceptorEnvironment(Instance<Bar> barInstance, Instance<Foo> fooInstance) {
        FooInterceptor.reset();
        Foo foo = fooInstance.get();
        String fooAnimalName = foo.getAnimal().getName();
        String fooGreeting = foo.getGreeting();

        assertTrue(FooInterceptor.isInterceptorCalled());
        assertEquals(FooInterceptor.getGreeting(), fooGreeting);
        assertEquals(FooInterceptor.getAnimal().getName(), fooAnimalName);
        assertEquals(foo.getGreeting(), BYE);
        assertEquals(fooAnimalName, "Dog");

        BarInterceptor.reset();
        Bar bar = barInstance.get();
        String barAnimalName = bar.getAnimal().getName();
        String barGreeting = bar.getGreeting();

        assertTrue(BarInterceptor.isInterceptorCalled());
        assertEquals(BarInterceptor.getGreeting(), barGreeting);
        assertEquals(BarInterceptor.getAnimal().getName(), barAnimalName);
        assertEquals(bar.getGreeting(), HELLO);
        assertEquals(barAnimalName, "Cat");
    }
}
