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

package org.jboss.cdi.tck.tests.context.request.async;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.REQUEST_CONTEXT;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.context.application.async.AsyncServlet;
import org.jboss.shrinkwrap.api.spec.WebArchive;
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
@SpecVersion(spec = "cdi", version = "20091101")
public class RequestContextAsyncListenerTest extends AbstractTest {

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(RequestContextAsyncListenerTest.class).withWebResource("foo.jsp")
                .withWebResource("bar.jsp").build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = REQUEST_CONTEXT, id = "ad"), @SpecAssertion(section = REQUEST_CONTEXT, id = "bd") })
    public void testRequestContextActiveOnComplete() throws Exception {

        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);

        TextPage page01 = webClient.getPage(getPath(AsyncServlet.TEST_COMPLETE));
        assertTrue(page01.getContent().contains("onTimeout: null"));
        assertTrue(page01.getContent().contains("onError: null"));
        assertFalse(page01.getContent().contains("onComplete: null"));

        // Indirectly test request context is destroyed after onComplete()
        TextPage page02 = webClient.getPage(getPath(AsyncServlet.TEST_COMPLETE));
        assertNotEquals(extractSimpleRequestBeanIdString(page01.getContent()),
                extractSimpleRequestBeanIdString(page02.getContent()));
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = REQUEST_CONTEXT, id = "ad")
    public void testRequestContextActiveOnTimeout() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        TextPage page = webClient.getPage(getPath(AsyncServlet.TEST_TIMEOUT));
        assertFalse(page.getContent().contains("onTimeout: null"));
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = REQUEST_CONTEXT, id = "ad")
    public void testRequestContextActiveOnError() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        TextPage page = webClient.getPage(getPath(AsyncServlet.TEST_ERROR));
        assertFalse(page.getContent().contains("onError: null"));
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = REQUEST_CONTEXT, id = "ad")
    public void testRequestContextActiveOnStartAsync() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        TextPage page = webClient.getPage(getPath(AsyncServlet.TEST_LOOP));
        assertFalse(page.getContent().contains("onStartAsync: null"));
        assertFalse(page.getContent().contains("onComplete: null"));
    }

    private String getPath(String test) {
        return contextPath + "AsyncServlet?test=" + test;
    }

    private String extractSimpleRequestBeanIdString(String content) {
        String[] tokens = content.split(",");
        // See SimpleAsyncListener#getInfo()
        return tokens[5];
    }
}
