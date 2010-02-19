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

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.jboss.testharness.impl.packaging.war.WebXml;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

/**
 * @author Nicklas Karlsson
 * @author Dan Allen
 */
@Artifact(addCurrentPackage=false)
@Classes({Storm.class, ConversationTestPhaseListener.class, ConversationStatusServlet.class, Cloud.class, CloudController.class, Cumulus.class, BuiltInConversation.class})
@IntegrationTest(runLocally=true)
@Resources({
  @Resource(destination="home.jspx", source="home.jsf"),
  @Resource(destination="cloud.jspx", source="cloud.jsf"),
  @Resource(destination="clouds.jspx", source="clouds.jsf"),
  @Resource(destination="cumulus.jspx", source="cumulus.jsf"),
  @Resource(destination="builtin.jspx", source="builtin.jsf"),
  @Resource(destination="error.jspx", source="error.jsf"),
  @Resource(destination="storm.jspx", source="storm.jsf"),
  @Resource(destination="/WEB-INF/faces-config.xml", source="faces-config.xml"),
  @Resource(destination="rain.jspx", source="rain.jsf")
})
@WebXml("web.xml")
@SpecVersion(spec="cdi", version="20091101")
public class ClientConversationContextTest extends AbstractConversationTest
{
   
   private WebClient client;
   
   
   @Override
   public void beforeMethod()
   {
      super.beforeMethod();
      client = new WebClient();
   }

   
   @Test(groups = { "contexts"})
   @SpecAssertions({
      @SpecAssertion(section = "6.7.4", id = "hb"),
      @SpecAssertion(section = "6.7.4", id = "o")
   })
   public void testConversationIdSetByContainerIsUnique() throws Exception
   {
      HtmlPage storm = client.getPage(getPath("/storm.jsf"));
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(storm, HtmlSubmitInput.class, "beginConversationButton");
      storm = beginConversationButton.click();
      
      String c1 = getCid(storm);
      
      storm = client.getPage(getPath("/storm.jsf"));
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
      resetCloud(client);
      HtmlPage page = client.getPage(getPath("/cloud.jsf"));
      assert !isLongRunning(page);
      assert isCloudDestroyed(client);
   }
   
   @Test(groups = { "contexts"})
   @SpecAssertion(section = "6.7.4", id = "k")
   public void testLongRunningConversationInstancesNotDestroyedAtRequestEnd() throws Exception
   {
      HtmlPage storm = client.getPage(getPath("/storm.jsf"));
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(storm, HtmlSubmitInput.class, "beginConversationButton");
      storm = beginConversationButton.click();
      
      resetCloud(client);
      
      client.getPage(getPath("/cloud.jsf", getCid(storm)));
      assert !isCloudDestroyed(client);
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "p")
   public void testConversationsDontCrossSessionBoundary1() throws Exception
   {
      // Load the page
      HtmlPage rain = client.getPage(getPath("/rain.jsf"));
      
      // begin a conversation
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(rain, HtmlSubmitInput.class, "beginConversationButton");
      rain = beginConversationButton.click();
      String cid = getCid(rain);
      
      // Cause rain
      HtmlSubmitInput rainButton = getFirstMatchingElement(rain, HtmlSubmitInput.class, "rain");
      rain = rainButton.click();
      
      // Re-request the page, inside the conversation and check it has rained
      rain = client.getPage(getPath("/rain.jsf", cid));
      assert hasRained(rain);
      
      // Invalidate the session, invalidate the conversation-scoped cloud
      invalidateSession(client);
      
      // Re-request the page, check it hasn't rained
      rain = client.getPage(getPath("/rain.jsf", cid));
      assert !hasRained(rain);
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "p")
   public void testConversationsDontCrossSessionBoundary2() throws Exception
   {
      // Load the page
      HtmlPage rain = client.getPage(getPath("/rain.jsf"));
      
      // begin a conversation
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(rain, HtmlSubmitInput.class, "beginConversationButton");
      rain = beginConversationButton.click();
      String cid = getCid(rain);
      
      // Cause rain
      HtmlSubmitInput rainButton = getFirstMatchingElement(rain, HtmlSubmitInput.class, "rain");
      rain = rainButton.click();
      
      // Re-request the page, inside the conversation and check it has rained
      rain = client.getPage(getPath("/rain.jsf", cid));
      assert hasRained(rain);
      
      // Create a new web client and load the page
      client = new WebClient();
      rain = client.getPage(getPath("/rain.jsf", cid));
      assert !hasRained(rain);
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "a")
   public void testConversationActiveDuringNonFacesRequest() throws Exception
   {
      HtmlPage page = client.getPage(getPath("/cloud.jsf"));
      HtmlSpan span = getFirstMatchingElement(page, HtmlSpan.class, "cloudName");
      assert span.getTextContent().equals(Cloud.NAME);
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "tb")
   public void testConversationPropagationToNonExistentConversationLeadsToTransientConversation() throws Exception
   {
      Page page = client.getPage(getPath("/cloud.jsf", "org.jboss.jsr299"));
      assert !isLongRunning(page);
      assert !getCid(page).equals("org.jboss.jsr299");
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "f")
   public void testConversationBeginMakesConversationLongRunning() throws Exception
   {
      HtmlPage page = client.getPage(getPath("/cumulus.jsf"));
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
      HtmlPage page = client.getPage(getPath("/cumulus.jsf"));
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
      HtmlPage page = client.getPage(getPath("/cumulus.jsf"));
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
      HtmlPage page = client.getPage(getPath("/cumulus.jsf"));
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
      HtmlPage page = client.getPage(getPath("/builtin.jsf"));
      assert page.getBody().getTextContent().contains("Correct scope: true");
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.5", id = "id")
   public void testBeanWithDefaultQualifier() throws Exception
   {
      HtmlPage page = client.getPage(getPath("/builtin.jsf"));
      assert page.getBody().getTextContent().contains("Correct qualifier: true");
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.5", id = "ie")
   public void testBeanWithNameJavaxEnterpriseContextConversation() throws Exception
   {
      HtmlPage page = client.getPage(getPath("/builtin.jsf"));
      assert page.getBody().getTextContent().contains("Correct name: true");
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertions({
      @SpecAssertion(section = "6.7.5", id = "l"),
      @SpecAssertion(section = "6.7.4", id = "e")
   })
   public void testTransientConversationHasNullId() throws Exception
   {
      HtmlPage page = client.getPage(getPath("/builtin.jsf"));
      assert page.getBody().getTextContent().contains("Default conversation has null id: true");
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertions({
      @SpecAssertion(section = "6.7.4", id = "ha"),
      @SpecAssertion(section = "6.7.5", id = "j")
   })
   public void testConversationIdMayBeSetByApplication() throws Exception
   {
      HtmlPage page = client.getPage(getPath("/cumulus.jsf"));
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
      HtmlPage page = client.getPage(getPath("/cumulus.jsf"));
      assert !isLongRunning(page);
      
      // begin a conversation
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class, "beginConversationButton");
      page = beginConversationButton.click();
      assert isLongRunning(page);
      assert getCid(page) != null;
      assert page.getBody().getTextContent().contains("Cumulus congestus");
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "6.7.5", id = "m"),
      @SpecAssertion(section = "6.7.5", id = "n")
   })
   public void testSetConversationTimeoutOverride() throws Exception
   {
      HtmlPage page = client.getPage(getPath("/cumulus.jsf"));
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
      HtmlPage page = client.getPage(getPath("/cumulus.jsf"));
      assert !isLongRunning(page);
      
      // begin a conversation
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class, "beginConversationButton");
      page = beginConversationButton.click();
      assert page.getBody().getTextContent().contains("Stratocumulus");
   }
}