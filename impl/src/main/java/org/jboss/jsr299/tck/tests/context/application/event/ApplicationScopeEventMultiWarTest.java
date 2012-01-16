/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.jsr299.tck.tests.context.application.event;

import static org.jboss.jsr299.tck.TestGroups.JAVAEE_FULL;

import javax.servlet.ServletContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.EnterpriseArchiveBuilder;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.jsr299.tck.tests.deployment.packaging.ear.MultiWebModuleWithExtensionTest;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.application6.ApplicationDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Verifies that an observer is not notified of a non-visible {@link ServletContext}.
 * 
 * <p>
 * Note that this test has to run in as-client mode since arquillian cannot work with such archive (doesn't know which WAR to
 * enrich).
 * </p>
 * 
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = JAVAEE_FULL)
@SpecVersion(spec = "cdi", version = "20091101")
public class ApplicationScopeEventMultiWarTest extends AbstractJSR299Test {

    @Deployment(testable = false)
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder().withTestClass(
                MultiWebModuleWithExtensionTest.class).build();
        StringAsset applicationXml = new StringAsset(Descriptors.create(ApplicationDescriptor.class).applicationName("Test")
                .createModule().getOrCreateWeb().webUri("test1.war").contextRoot("/test1").up().up().createModule()
                .getOrCreateWeb().webUri("test2.war").contextRoot("/test2").up().up().exportAsString());
        enterpriseArchive.setApplicationXML(applicationXml);

        WebArchive fooArchive = new WebArchiveBuilder().notTestArchive().withName("test1.war").withClasses(Observer2.class)
                .withDefaultEjbModuleDependency().build();
        enterpriseArchive.addAsModule(fooArchive);

        WebArchive barArchive = new WebArchiveBuilder().notTestArchive().withName("test2.war").withClasses(Observer3.class)
                .withDefaultEjbModuleDependency().build();
        enterpriseArchive.addAsModule(barArchive);

        return enterpriseArchive;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6.7.3", id = "ga") })
    public void testDeployment() {
        // noop - the deployment either fails or not
    }
}
