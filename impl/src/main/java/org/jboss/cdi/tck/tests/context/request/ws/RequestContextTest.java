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
package org.jboss.cdi.tck.tests.context.request.ws;

import static org.jboss.cdi.tck.TestGroups.CONTEXTS;
import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.xml.namespace.QName;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * 
 * @author Martin Kouba
 */
@Test(groups = JAVAEE_FULL)
@SpecVersion(spec = "cdi", version = "20091101")
public class RequestContextTest extends AbstractTest {

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(RequestContextTest.class)
                .withExcludedClass(TranslatorEndpointService.class.getName())
                .withWebXml(
                        Descriptors.create(WebAppDescriptor.class).createServlet().servletName("Translator")
                                .servletClass("org.jboss.cdi.tck.tests.context.request.ws.TranslatorEndpoint").loadOnStartup(1)
                                .up().createServletMapping().servletName("Translator").urlPattern("/translator").up()).build();
    }

    @Test(groups = { CONTEXTS })
    @SpecAssertions({ @SpecAssertion(section = "6.7.1", id = "c"), @SpecAssertion(section = "6.7.1", id = "d"),
            @SpecAssertion(section = "6.7.3", id = "b") })
    public void testRequestScopeActiveDuringWebServiceInvocation() throws Exception {

        URL wsdlLocation = new URL(contextPath.toExternalForm() + "translator?wsdl");
        TranslatorEndpointService endpointService = new TranslatorEndpointService(wsdlLocation, new QName(
                "http://ws.request.context.tests.tck.cdi.jboss.org/", "Translator"));
        Translator translator = endpointService.getTranslatorPort();

        // New instance of Foo is created for each WS request
        Long id01 = Long.valueOf(translator.translate());
        Long id02 = Long.valueOf(translator.translate());
        assertNotEquals(id01, id02);

        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);

        TextPage info = webClient.getPage(contextPath + "info");
        assertTrue(info.getContent().contains("Foo destroyed:2"));
    }

}
