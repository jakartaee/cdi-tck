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
package org.jboss.cdi.tck.tests.context.session;

import static org.jboss.cdi.tck.TestGroups.CONTEXTS;
import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.SERVLET;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.Timer;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class SessionContextTest extends AbstractTest {

    private static final long DEFAULT_SLEEP_INTERVAL = 3000;

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SessionContextTest.class)
                .withWebResource("SimplePage.html", "SimplePage.html").withWebXml("web.xml").build();
    }

    @Test(groups = { CONTEXTS, SERVLET })
    @SpecAssertion(section = "6.7.2", id = "aa")
    public void testSessionScopeActiveDuringServiceMethod() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        webClient.getPage(contextPath + "serviceMethodTest");
    }

    @Test(groups = { CONTEXTS, SERVLET })
    @SpecAssertion(section = "6.7.2", id = "ab")
    public void testSessionScopeActiveDuringDoFilterMethod() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        webClient.getPage(contextPath + "SimplePage.html");
    }

    @Test(groups = { CONTEXTS, SERVLET })
    @SpecAssertion(section = "6.7.2", id = "b")
    public void testSessionContextSharedBetweenServletRequestsInSameHttpSession() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        TextPage firstRequestResult = webClient.getPage(contextPath + "IntrospectSession");
        assert firstRequestResult.getContent() != null;
        assert Long.parseLong(firstRequestResult.getContent()) != 0;
        // Make a second request and make sure the same context is used
        TextPage secondRequestResult = webClient.getPage(contextPath + "IntrospectSession");
        assert secondRequestResult.getContent() != null;
        assert Long.parseLong(secondRequestResult.getContent()) == Long.parseLong(firstRequestResult.getContent());
    }

    @Test(groups = { CONTEXTS, SERVLET })
    @SpecAssertion(section = "6.7.2", id = "ca")
    public void testSessionContextDestroyedWhenHttpSessionInvalidated() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        TextPage firstRequestResult = webClient.getPage(contextPath + "IntrospectSession");
        assert firstRequestResult.getContent() != null;
        assert Long.parseLong(firstRequestResult.getContent()) != 0;
        webClient.getPage(contextPath + "InvalidateSession");
        // Make a second request and make sure the same context is not there
        TextPage secondRequestResult = webClient.getPage(contextPath + "IntrospectSession");
        assert secondRequestResult.getContent() != null;
        assert Long.parseLong(secondRequestResult.getContent()) != Long.parseLong(firstRequestResult.getContent());

        // As final confirmation that the context was destroyed, check that its beans
        // were also destroyed.
        TextPage beanDestructionResult = webClient.getPage(contextPath + "InvalidateSession?isBeanDestroyed");
        assert Boolean.parseBoolean(beanDestructionResult.getContent());
    }

    @Test(groups = { CONTEXTS, SERVLET })
    @SpecAssertion(section = "6.7.2", id = "cb")
    public void testSessionContextDestroyedWhenHttpSessionTimesOut() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        TextPage firstRequestResult = webClient.getPage(contextPath + "IntrospectSession");
        assert firstRequestResult.getContent() != null;
        assert Long.parseLong(firstRequestResult.getContent()) != 0;
        webClient.getPage(contextPath + "InvalidateSession?timeout=1");

        Timer.startNew(DEFAULT_SLEEP_INTERVAL);

        // Make a second request and make sure the same context is not there
        TextPage secondRequestResult = webClient.getPage(contextPath + "IntrospectSession");
        assert secondRequestResult.getContent() != null;
        assert Long.parseLong(secondRequestResult.getContent()) != Long.parseLong(firstRequestResult.getContent());

        // As final confirmation that the context was destroyed, check that its beans
        // were also destroyed.
        TextPage beanDestructionResult = webClient.getPage(contextPath + "InvalidateSession?isBeanDestroyed");
        assert Boolean.parseBoolean(beanDestructionResult.getContent());
    }

}
