/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.lookup.injection.ws;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.JAX_WS;
import static org.jboss.cdi.tck.cdi.Sections.FIELDS_INITIALIZER_METHODS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Matus Abaffy
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class WebServiceResourceInjectionTest extends AbstractTest {

	@Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(WebServiceResourceInjectionTest.class)
                .withWebXml(
                        Descriptors
                                .create(WebAppDescriptor.class)
                                .createServlet()
                                .servletName("Translator")
                                .servletClass(
                                        "org.jboss.cdi.tck.tests.lookup.injection.ws.TranslatorEndpoint")
                                .loadOnStartup(1).up().createServletMapping().servletName("Translator")
                                .urlPattern("/translator").up()).build();
    }

    @Test(groups = { JAVAEE_FULL, JAX_WS })
    @SpecAssertions({ @SpecAssertion(section = FIELDS_INITIALIZER_METHODS, id = "bi"),
            @SpecAssertion(section = FIELDS_INITIALIZER_METHODS, id = "bj") })
    public void testInitializerMethodsCalledAfterEEResourceWebServiceRefInjection() throws Exception {
        Alpha alpha = getContextualReference(Alpha.class);
        assertNotNull(alpha);
        assertTrue(alpha.initializerCalledAfterResourceInjection);
        assertEquals("ok", alpha.translator.translate("hello"));
    }
}
