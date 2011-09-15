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
package org.jboss.jsr299.tck.tests.context.conversation;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

/**
 * @author Nicklas Karlsson
 * @author Dan Allen
 * @author Martin Kouba
 */
@SpecVersion(spec="cdi", version="20091101")
public class ClientConversationContextTest extends AbstractConversationTest
{
    
    @Deployment(testable=false)
    public static WebArchive createTestArchive() 
	{
        return new WebArchiveBuilder()
            .withTestClass(ClientConversationContextTest.class)
            .withClasses(Storm.class, ConversationTestPhaseListener.class, ConversationStatusServlet.class, Cloud.class,
                        CloudController.class, OutermostFilter.class, Cumulus.class, BuiltInConversation.class)
            .withWebResource("home.jsf", "home.jspx")
            .withWebResource("cloud.jsf", "cloud.jspx")
            .withWebResource("clouds.jsf", "clouds.jspx")
            .withWebResource("cumulus.jsf", "cumulus.jspx")
            .withWebResource("builtin.jsf", "builtin.jspx")
            .withWebResource("error.jsf", "error.jspx")
            .withWebResource("storm.jsf", "storm.jspx")
            .withWebResource("rain.jsf", "rain.jspx")
            .withWebResource("faces-config.xml", "/WEB-INF/faces-config.xml")
            .withWebXml("web.xml")
            .build();
    }
   
   @Test(groups = { "contexts"})
   @SpecAssertions({
      @SpecAssertion(section = "6.7.4", id = "hb"),
      @SpecAssertion(section = "6.7.4", id = "o")
   })
   public void testConversationIdSetByContainerIsUnique() throws Exception
   {
      WebClient client = new WebClient();
      HtmlPage storm = client.getPage(getPath("storm.jsf"));
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(storm, HtmlSubmitInput.class, "beginConversationButton");
      storm = beginConversationButton.click();
      
      String c1 = getCid(storm);
      
      storm = client.getPage(getPath("storm.jsf"));
      beginConversationButton = getFirstMatchingElement(storm, HtmlSubmitInput.class, "beginConversationButton");
      storm = beginConversationButton.click();
      
      String c2 = getCid(storm);
      
      assert !c1.equals(c2);
   }
   
   @Test(groups = { "contexts", "rewrite" })
   @SpecAssertion(section = "6.7.4", id = "j")
   // TODO this test doesn't verify that the conversation context itself is destroyed
   public void testTransientConversationInstancesDestroyedAtRequestEnd() throws Exception
   {
      WebClient client = new WebClient();
      resetCloud(client);
      HtmlPage page = client.getPage(getPath("cloud.jsf"));
      assert !isLongRunning(page);
      assert isCloudDestroyed(client);
   }
   
   @Test(groups = { "contexts"})
   @SpecAssertion(section = "6.7.4", id = "k")
   public void testLongRunningConversationInstancesNotDestroyedAtRequestEnd() throws Exception
   {
      WebClient client = new WebClient();
      HtmlPage storm = client.getPage(getPath("storm.jsf"));
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(storm, HtmlSubmitInput.class, "beginConversationButton");
      storm = beginConversationButton.click();
      
      resetCloud(client);
      
      client.getPage(getPath("cloud.jsf", getCid(storm)));
      assert !isCloudDestroyed(client);
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "p")
   public void testConversationsDontCrossSessionBoundary1() throws Exception
   {
      WebClient client = new WebClient();
      client.setThrowExceptionOnFailingStatusCode(false);
      // Load the page
      HtmlPage rain = client.getPage(getPath("rain.jsf"));
      
      // begin a conversation
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(rain, HtmlSubmitInput.class, "beginConversationButton");
      rain = beginConversationButton.click();
      String cid = getCid(rain);
      
      // Cause rain
      HtmlSubmitInput rainButton = getFirstMatchingElement(rain, HtmlSubmitInput.class, "rain");
      rain = rainButton.click();
      
      // Re-request the page, inside the conversation and check it has rained
      rain = client.getPage(getPath("rain.jsf", cid));
      assert hasRained(rain);
      
      // Invalidate the session, invalidate the conversation-scoped cloud
      invalidateSession(client);
      
      // Re-request the page, check it hasn't rained
      rain = client.getPage(getPath("rain.jsf", cid));
      assert !hasRained(rain);
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "p")
   public void testConversationsDontCrossSessionBoundary2() throws Exception
   {
      WebClient client = new WebClient();
      
      // Load the page
      HtmlPage rain = client.getPage(getPath("rain.jsf"));
      
      // begin a conversation
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(rain, HtmlSubmitInput.class, "beginConversationButton");
      rain = beginConversationButton.click();
      String cid = getCid(rain);
      
      // Cause rain
      HtmlSubmitInput rainButton = getFirstMatchingElement(rain, HtmlSubmitInput.class, "rain");
      rain = rainButton.click();
      
      // Re-request the page, inside the conversation and check it has rained
      rain = client.getPage(getPath("rain.jsf", cid));
      assert hasRained(rain);
      
      // Create a new web client and load the page
      WebClient client2 = new WebClient();
      client2.setThrowExceptionOnFailingStatusCode(false);
      rain = client2.getPage(getPath("rain.jsf", cid));
      assert !hasRained(rain);
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "a")
   public void testConversationActiveDuringNonFacesRequest() throws Exception
   {
      WebClient client = new WebClient();
      HtmlPage page = client.getPage(getPath("cloud.jsf"));
      HtmlSpan span = getFirstMatchingElement(page, HtmlSpan.class, "cloudName");
      assert span.getTextContent().equals(Cloud.NAME);
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "f")
   public void testConversationBeginMakesConversationLongRunning() throws Exception
   {
      WebClient client = new WebClient();
      HtmlPage page = client.getPage(getPath("cumulus.jsf"));
      assert !isLongRunning(page);
      
      // begin a conversation
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class, "beginConversationButton");
      page = beginConversationButton.click();
      assert isLongRunning(page);
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.5", id = "r")
   public void testBeginAlreadyLongRunningConversationThrowsException() throws Exception
   {
      WebClient client = new WebClient();
      HtmlPage page = client.getPage(getPath("cumulus.jsf"));
      assert !isLongRunning(page);
      
      // begin a conversation
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class, "beginConversationButton");
      page = beginConversationButton.click();
      assert isLongRunning(page);
      
      // begin a conversation again and check that IllegalStateException is thrown
      HtmlSubmitInput beginConversationButton2 = getFirstMatchingElement(page, HtmlSubmitInput.class, "beginConversationAndSwallowException");
      page = beginConversationButton2.click();
      assert page.getBody().getTextContent().contains("Hello world!");
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertions({
      @SpecAssertion(section = "6.7.4", id = "g"),
      @SpecAssertion(section = "6.7.5", id = "k"),
      @SpecAssertion(section = "6.7.5", id = "o")
   })
   public void testConversationEndMakesConversationTransient() throws Exception
   {
      WebClient client = new WebClient();
      HtmlPage page = client.getPage(getPath("cumulus.jsf"));
      assert !isLongRunning(page);
      
      // begin a conversation
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class, "beginConversationButton");
      page = beginConversationButton.click();
      assert isLongRunning(page);
      
      // end a conversation
      HtmlSubmitInput endConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class, "endConversationButton");
      page = endConversationButton.click();
      assert !isLongRunning(page);
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.5", id = "q")
   public void testEndTransientConversationThrowsException() throws Exception
   {
      WebClient client = new WebClient();
      HtmlPage page = client.getPage(getPath("cumulus.jsf"));
      assert !isLongRunning(page);
      
      // try ending a transient conversation
      HtmlSubmitInput endConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class, "endConversationAndSwallowException");
      page = endConversationButton.click();
      assert page.getBody().getTextContent().contains("Hello world!");
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertions({
      @SpecAssertion(section = "6.7.5", id = "ib"),
      @SpecAssertion(section = "6.7.5", id = "iaa")
   })
   public void testBeanWithRequestScope() throws Exception
   {
      WebClient client = new WebClient();
      HtmlPage page = client.getPage(getPath("builtin.jsf"));
      assert page.getBody().getTextContent().contains("Correct scope: true");
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.5", id = "id")
   public void testBeanWithDefaultQualifier() throws Exception
   {
      WebClient client = new WebClient();
      HtmlPage page = client.getPage(getPath("builtin.jsf"));
      assert page.getBody().getTextContent().contains("Correct qualifier: true");
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.5", id = "ie")
   public void testBeanWithNameJavaxEnterpriseContextConversation() throws Exception
   {
      WebClient client = new WebClient();
      HtmlPage page = client.getPage(getPath("builtin.jsf"));
      assert page.getBody().getTextContent().contains("Correct name: true");
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertions({
      @SpecAssertion(section = "6.7.5", id = "l"),
      @SpecAssertion(section = "6.7.4", id = "e")
   })
   public void testTransientConversationHasNullId() throws Exception
   {
      WebClient client = new WebClient();
      HtmlPage page = client.getPage(getPath("builtin.jsf"));
      assert page.getBody().getTextContent().contains("Default conversation has null id: true");
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertions({
      @SpecAssertion(section = "6.7.4", id = "ha"),
      @SpecAssertion(section = "6.7.5", id = "j")
   })
   public void testConversationIdMayBeSetByApplication() throws Exception
   {
      WebClient client = new WebClient();
      HtmlPage page = client.getPage(getPath("cumulus.jsf"));
      assert !isLongRunning(page);
      
      // begin a conversation
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class, "beginConversationIdentifiedByCustomIdentifier");
      page = beginConversationButton.click();
      assert isLongRunning(page);
      assert getCid(page).equals("humilis");
      assert page.getBody().getTextContent().contains("Cumulus humilis");
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertions({
      @SpecAssertion(section = "6.7.4", id = "hb"),
      @SpecAssertion(section = "6.7.5", id = "j")
   })
   public void testConversationIdMayBeSetByContainer() throws Exception
   {
      WebClient client = new WebClient();
      HtmlPage page = client.getPage(getPath("cumulus.jsf"));
      assert !isLongRunning(page);
      
      // begin a conversation
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class, "beginConversationButton");
      page = beginConversationButton.click();
      assert isLongRunning(page);
      assert getCid(page) != null;
      assert page.getBody().getTextContent().contains("Cumulus congestus");
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "tb")
   public void testNonexistentConversationExceptionThrown() throws Exception
   {
      WebClient client = new WebClient();
      HtmlPage page = client.getPage(getPath("cumulus.jsf?cid=foo"));
      
      assert page.getBody().getTextContent().contains("NonexistentConversationException thrown properly");
      // FIXME WELD-878
      //assert page.getBody().getTextContent().contains("Conversation.isTransient: true");
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "6.7.5", id = "m"),
      @SpecAssertion(section = "6.7.5", id = "n")
   })
   public void testSetConversationTimeoutOverride() throws Exception
   {
      WebClient client = new WebClient();
      HtmlPage page = client.getPage(getPath("cumulus.jsf"));
      assert !isLongRunning(page);
   
      // begin a conversation and set a custom timeout
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class, "beginConversationAndSetTimeout");
      page = beginConversationButton.click();
      assert page.getBody().getTextContent().contains("Cumulonimbus");
   }
   
   @Test
   @SpecAssertion(section = "6.7.5", id = "m")
   public void testConversationHasDefaultTimeout() throws Exception
   {
      WebClient client = new WebClient();
      HtmlPage page = client.getPage(getPath("cumulus.jsf"));
      assert !isLongRunning(page);
      
      // begin a conversation
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class, "beginConversationButton");
      page = beginConversationButton.click();
      assert page.getBody().getTextContent().contains("Stratocumulus");
   }
}