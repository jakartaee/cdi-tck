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
package org.jboss.cdi.tck.tests.lookup.injection.non.contextual;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.JAX_WS;
import static org.jboss.cdi.tck.cdi.Sections.FIELDS_INITIALIZER_METHODS;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.WebClient;

/**
 * 
 * @author Matus Abaffy
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class WebServiceInjectionIntoNonContextualComponentTest extends AbstractTest {

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(WebServiceInjectionIntoNonContextualComponentTest.class)
                .withClasses(Translator.class, TranslatorEndpoint.class, TranslatorEndpointService.class,
                        TestServlet2.class, TestFilter2.class)
                .withWebXml("web3.xml").build();
    }

    @Test(groups = { JAVAEE_FULL, JAX_WS })
    @SpecAssertion(section = FIELDS_INITIALIZER_METHODS, id = "bo")
    public void testServletInitCalledAfterResourceInjection() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        webClient.getPage(contextPath + "TestServlet2?test=wsresource");
        assertTrue(TestServlet2.initCalledAfterWSResourceInjection);
    }

    @Test(groups = { JAVAEE_FULL, JAX_WS })
    @SpecAssertion(section = FIELDS_INITIALIZER_METHODS, id = "br")
    public void testFilterInitCalledAfterResourceInjection() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        webClient.getPage(contextPath + "TestFilter2?test=wsresource");
        assertTrue(TestFilter2.initCalledAfterWSResourceInjection);
    }
}
