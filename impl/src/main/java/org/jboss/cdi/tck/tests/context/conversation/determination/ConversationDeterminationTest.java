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

package org.jboss.cdi.tck.tests.context.conversation.determination;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * @author Martin Kouba
 * 
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class ConversationDeterminationTest extends AbstractTest {

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ConversationDeterminationTest.class).build();
    }

    @ArquillianResource
    private URL contextPath;

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = "6.7.4", id = "da"), @SpecAssertion(section = "6.7.4", id = "db"),
            @SpecAssertion(section = "6.7.4", id = "dc"), @SpecAssertion(section = "6.7.4", id = "dd") })
    public void testConversationDetermination() throws Exception {

        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);

        // Begin long-running conversation
        TextPage cidPage = webClient.getPage(contextPath + "foo?action=begin");
        String cid = cidPage.getContent().substring(cidPage.getContent().indexOf("cid: [") + 6,
                cidPage.getContent().indexOf("]"));
        assertTrue(cidPage.getContent().contains("transient: false"));

        // Test built-in conversation bean is available
        TextPage resultPage = webClient.getPage(contextPath + "foo?action=test&cid=" + cid.trim());
        assertTrue(resultPage.getContent().contains("testServlet=true"));
        assertTrue(resultPage.getContent().contains("testFilter=true"));
        assertTrue(resultPage.getContent().contains("testListener=true"));

        // Async listener
        resultPage = webClient.getPage(contextPath + "foo-async?action=test&cid=" + cid.trim());
        assertTrue(resultPage.getContent().contains("testAsyncListener=true"));
    }

}
