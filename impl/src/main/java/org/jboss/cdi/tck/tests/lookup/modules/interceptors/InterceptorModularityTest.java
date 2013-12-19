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
package org.jboss.cdi.tck.tests.lookup.modules.interceptors;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.SELECTION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
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
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.shrinkwrap.descriptor.api.spec.se.manifest.ManifestDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test that interceptor class's injection and injection point validation is done with respect to the module containing the
 * interceptor class and not the intercepted class, i.e. test that interceptor of a bean uses the interceptor's bean manager and
 * not the bean's bean manager (for injection and injection point validation).
 * 
 * @author Matus Abaffy
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class InterceptorModularityTest extends AbstractTest {

    @Deployment
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder()
                .withTestClassDefinition(InterceptorModularityTest.class)
                .withClasses(BarBinding.class, Animal.class, Cow.class, BarSuperInterceptor.class)
                .withBeansXml(Descriptors.create(BeansDescriptor.class).createAlternatives().clazz(Cow.class.getName()).up())
                .noDefaultWebModule().build();

        StringAsset applicationXml = new StringAsset(Descriptors.create(ApplicationDescriptor.class)
                .version(EnterpriseArchiveBuilder.DEFAULT_APP_VERSION).applicationName("Test").createModule()
                .ejb(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME).up().createModule().getOrCreateWeb().webUri("test1.war")
                .contextRoot("/test1").up().up().createModule().getOrCreateWeb().webUri("test2.war").contextRoot("/test2").up()
                .up().exportAsString());
        enterpriseArchive.setApplicationXML(applicationXml);

        WebArchive fooArchive = new WebArchiveBuilder().notTestArchive().withName("test1.war")
                .withClasses(BarInterceptor.class, Dog.class)
                .withDefaultEjbModuleDependency()
                .build();
        enterpriseArchive.addAsModule(fooArchive);

        WebArchive barArchive = Testable.archiveToTest(new WebArchiveBuilder().notTestArchive().withName("test2.war")
                .withClasses(InterceptorModularityTest.class, Bar.class, Cat.class)
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
    @SpecAssertion(section = SELECTION, id = "aa")
    public void testInterceptorEnvironment(Instance<Bar> barInstance, Instance<BarSuperInterceptor> barSIInstance) {
        BarInterceptor.reset();
        barInstance.get().ping();

        assertTrue(BarInterceptor.isInterceptorCalled());
        assertNotNull(BarInterceptor.getAnimal());
        assertEquals(BarInterceptor.getAnimal().getName(), "Dog");
        assertEquals(BarSuperInterceptor.getSuperAnimal().getName(), "Dog");

        barSIInstance.get();
        assertEquals(BarSuperInterceptor.getSuperAnimal().getName(), "Cow");
    }
}
