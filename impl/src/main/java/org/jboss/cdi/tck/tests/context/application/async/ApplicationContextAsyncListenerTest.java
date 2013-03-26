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

package org.jboss.cdi.tck.tests.context.application.async;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.APPLICATION_CONTEXT;
import static org.testng.Assert.assertFalse;
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

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class ApplicationContextAsyncListenerTest extends AbstractTest {

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ApplicationContextAsyncListenerTest.class)
                .build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = APPLICATION_CONTEXT, id = "ae")
    public void testApplicationContextActiveOnComplete() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        TextPage page = webClient.getPage(getPath(AsyncServlet.TEST_COMPLETE));
        assertTrue(page.getContent().contains("onTimeout: null"));
        assertTrue(page.getContent().contains("onError: null"));
        assertFalse(page.getContent().contains("onComplete: null"));
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = APPLICATION_CONTEXT, id = "ae")
    public void testApplicationContextActiveOnTimeout() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        TextPage page = webClient.getPage(getPath(AsyncServlet.TEST_TIMEOUT));
        assertFalse(page.getContent().contains("onTimeout: null"));
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = APPLICATION_CONTEXT, id = "ae")
    public void testApplicationContextActiveOnError() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(false);
        TextPage page = webClient.getPage(getPath(AsyncServlet.TEST_ERROR));
        assertFalse(page.getContent().contains("onError: null"));
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = APPLICATION_CONTEXT, id = "ae")
    public void testApplicationContextActiveOnStartAsync() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        TextPage page = webClient.getPage(getPath(AsyncServlet.TEST_LOOP));
        assertFalse(page.getContent().contains("onStartAsync: null"));
        assertFalse(page.getContent().contains("onComplete: null"));
    }

    private String getPath(String test) {
        return contextPath + "AsyncServlet?test=" + test;
    }
}
