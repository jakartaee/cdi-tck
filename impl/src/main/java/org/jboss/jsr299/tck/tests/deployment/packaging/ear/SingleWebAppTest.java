/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.jsr299.tck.tests.deployment.packaging.ear;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.EnterpriseArchiveBuilder;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.application6.ApplicationDescriptor;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * This test verifies that a CDI extension can be used with a web application bundled inside of an enterprise archive (.ear)
 * 
 * <p>
 * This test was originally part of Seam Compatibility project.
 * <p>
 * 
 * TODO verify assertions
 * 
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 * @author Martin Kouba
 * @see https://issues.jboss.org/browse/JBAS-8683
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class SingleWebAppTest extends AbstractJSR299Test {

    @Deployment
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder().withTestClass(SingleWebAppTest.class).build();
        StringAsset applicationXml = new StringAsset(Descriptors.create(ApplicationDescriptor.class).applicationName("Test")
                .createModule().getOrCreateWeb().webUri("test.war").contextRoot("/test").up().up().exportAsString());
        enterpriseArchive.setApplicationXML(applicationXml);

        WebArchive webArchive = createWar("test.war", createJar("test.jar", FooExtension.class, FooBean.class));
        webArchive.addClass(FooWebBean.class);

        enterpriseArchive.addAsModule(webArchive);
        return enterpriseArchive;
    }

    public static WebArchive createWar(String name, JavaArchive... libraries) {
        StringAsset webXml = new StringAsset(Descriptors.create(WebAppDescriptor.class).exportAsString());
        return ShrinkWrap.create(WebArchive.class, name).setWebXML(webXml)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml").addAsLibraries(libraries);
    }

    public static JavaArchive createJar(String name, Class<? extends Extension> extension, Class<?>... classes) {
        return ShrinkWrap.create(JavaArchive.class, name).addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addClasses(classes).addClass(extension).addAsServiceProvider(Extension.class, extension);
    }

    @Inject
    FooWebBean fooWebBean;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "12.1", id = "bbc"), @SpecAssertion(section = "12.1", id = "bbe") })
    public void test() {
        fooWebBean.ping();
    }
}
