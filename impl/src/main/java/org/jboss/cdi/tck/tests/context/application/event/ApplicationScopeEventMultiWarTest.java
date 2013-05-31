/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.context.application.event;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.APPLICATION_CONTEXT;

import javax.servlet.ServletContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
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
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class ApplicationScopeEventMultiWarTest extends AbstractTest {

    @Deployment(testable = false)
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder().notTestArchive().noDefaultWebModule().build();
        StringAsset applicationXml = new StringAsset(Descriptors.create(ApplicationDescriptor.class)
                .version(EnterpriseArchiveBuilder.DEFAULT_APP_VERSION).applicationName("Test").createModule()
                .ejb(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME).up().createModule().getOrCreateWeb().webUri("test1.war")
                .contextRoot("/test1").up().up().createModule().getOrCreateWeb().webUri("test2.war").contextRoot("/test2").up()
                .up().exportAsString());
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
    @SpecAssertions({ @SpecAssertion(section = APPLICATION_CONTEXT, id = "ga") })
    public void testDeployment() {
        // noop - the deployment either fails or not
    }
}
