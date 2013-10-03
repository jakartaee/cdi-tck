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
package org.jboss.cdi.tck.tests.implementation.simple.resource.ws.staticProducer;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.JAX_WS;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_RESOURCE;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class WebServiceResourceStaticProducerFieldTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(WebServiceResourceStaticProducerFieldTest.class)
                .withWebXml(
                        Descriptors
                                .create(WebAppDescriptor.class)
                                .displayName("WebService resource static producer field test")
                                .createServlet()
                                    .servletName("SheepWSEndPoint")
                                    .servletClass("org.jboss.cdi.tck.tests.implementation.simple.resource.ws.staticProducer.SheepWSEndPoint")
                                    .loadOnStartup(1).up()
                                .createServletMapping()
                                    .servletName("SheepWSEndPoint")
                                    .urlPattern("/TestWebService").up()
                            )
                .build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = { JAVAEE_FULL, JAX_WS })
    @SpecAssertion(section = DECLARING_RESOURCE, id = "ff")
    public void testResourceProduced(@Black SheepWS sheepWS) {
        assertNotNull(sheepWS);
        assertTrue(sheepWS.ping());
    }
}
