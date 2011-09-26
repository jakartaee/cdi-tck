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
package org.jboss.jsr299.tck.tests.context.request;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

@SpecVersion(spec = "cdi", version = "20091101")
public class RequestContextTest extends AbstractJSR299Test {

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(RequestContextTest.class)
                .withWebResource("SimplePage.html", "SimplePage.html").withWebXml("web.xml").build();
    }

    /**
     * The request scope is active during the service() method of any Servlet in the web application.
     */
    @Test(groups = { "contexts", "servlet", "integration" })
    @SpecAssertion(section = "6.7.1", id = "aa")
    public void testRequestScopeActiveDuringServiceMethod() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        webClient.getPage(contextPath + "serviceMethodTest");
    }

    /**
     * The request scope is active during the doFilter() method of any Servlet in the web application.
     */
    @Test(groups = { "contexts", "servlet", "integration" })
    @SpecAssertion(section = "6.7.1", id = "ab")
    public void testRequestScopeActiveDuringServletFilter() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        webClient.getPage(contextPath + "SimplePage.html");
    }

    /**
     * The request context is destroyed at the end of the servlet request, after the Servlet service() method returns.
     */
    @Test(groups = { "contexts", "servlet", "integration", "rewrite" })
    @SpecAssertion(section = "6.7.1", id = "baa")
    // TODO Need to tidy this one up, make it actually check that the context is active til after the service method ends
    public void testRequestScopeIsDestroyedAfterServletRequest() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        TextPage firstRequestResult = webClient.getPage(contextPath + "IntrospectRequest");
        assert firstRequestResult.getContent() != null;
        assert Double.parseDouble(firstRequestResult.getContent()) != 0;
        // Make a second request and make sure the same context is not there
        TextPage secondRequestResult = webClient.getPage(contextPath + "IntrospectRequest");
        assert secondRequestResult.getContent() != null;
        assert Double.parseDouble(secondRequestResult.getContent()) != Double.parseDouble(firstRequestResult.getContent());

        // As final confirmation that the context was destroyed, check that its beans
        // were also destroyed.
        // TextPage beanDestructionResult = webClient.getPage(contextPath + "InvalidateSession?isBeanDestroyed");
        // assert Boolean.parseBoolean(beanDestructionResult.getContent());
    }

}
