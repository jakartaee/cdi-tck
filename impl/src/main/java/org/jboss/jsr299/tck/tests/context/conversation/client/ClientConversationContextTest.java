package org.jboss.jsr299.tck.tests.context.conversation.client;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.jboss.testharness.impl.packaging.war.WarArtifactDescriptor;
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
@Classes({Storm.class, ConversationTestPhaseListener.class, ConversationStatusServlet.class, Cloud.class, CloudController.class})
@IntegrationTest(runLocally=true)
@Resources({
  @Resource(destination=WarArtifactDescriptor.WEB_XML_DESTINATION, source="web.xml"),
  @Resource(destination="home.jspx", source="home.jsf"),
  @Resource(destination="cloud.jspx", source="cloud.jsf"),
  @Resource(destination="clouds.jspx", source="clouds.jsf"),
  @Resource(destination="storm.jspx", source="storm.jsf"),
  @Resource(destination="/WEB-INF/faces-config.xml", source="faces-config.xml"),
  @Resource(destination="rain.jspx", source="rain.jsf")
})
@SpecVersion(spec="cdi", version="PFD2")
public class ClientConversationContextTest extends AbstractConversationTest
{
   
   @Test(groups = { "contexts"})
   @SpecAssertions({
      @SpecAssertion(section = "6.7.4", id = "hb"),
      @SpecAssertion(section = "6.7.4", id = "o")
   })
   public void testConversationIdSetByContainerIsUnique() throws Exception
   {
      WebClient webClient = new WebClient();
      HtmlPage storm = webClient.getPage(getPath("/storm.jsf"));
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(storm, HtmlSubmitInput.class, "beginConversationButton");
      storm = beginConversationButton.click();
      
      String c1 = getCid(storm);
      
      storm = webClient.getPage(getPath("/storm.jsf"));
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
      HtmlPage page = client.getPage(getPath("/cloud.jsf"));
      assert !isLongRunning(page);
      assert isCloudDestroyed(client);
   }
   
   @Test(groups = { "contexts"})
   @SpecAssertion(section = "6.7.4", id = "k")
   public void testLongRunningConversationInstancesNotDestroyedAtRequestEnd() throws Exception
   {
      WebClient client = new WebClient();
      
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
      WebClient webClient = new WebClient();
      
      // Load the page
      HtmlPage rain = webClient.getPage(getPath("/rain.jsf"));
      
      // begin a conversation
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(rain, HtmlSubmitInput.class, "beginConversationButton");
      rain = beginConversationButton.click();
      String cid = getCid(rain);
      
      // Cause rain
      HtmlSubmitInput rainButton = getFirstMatchingElement(rain, HtmlSubmitInput.class, "rain");
      rain = rainButton.click();
      
      // Re-request the page, inside the conversation and check it has rained
      rain = webClient.getPage(getPath("/rain.jsf", cid));
      assert hasRained(rain);
      
      // Invalidate the session, invalidate the conversation-scoped cloud
      invalidateSession(webClient);
      
      // Re-request the page, check it hasn't rained
      rain = webClient.getPage(getPath("/rain.jsf", cid));
      assert !hasRained(rain);
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "p")
   public void testConversationsDontCrossSessionBoundary2() throws Exception
   {
      WebClient webClient = new WebClient();
      
      // Load the page
      HtmlPage rain = webClient.getPage(getPath("/rain.jsf"));
      
      // begin a conversation
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(rain, HtmlSubmitInput.class, "beginConversationButton");
      rain = beginConversationButton.click();
      String cid = getCid(rain);
      
      // Cause rain
      HtmlSubmitInput rainButton = getFirstMatchingElement(rain, HtmlSubmitInput.class, "rain");
      rain = rainButton.click();
      
      // Re-request the page, inside the conversation and check it has rained
      rain = webClient.getPage(getPath("/rain.jsf", cid));
      assert hasRained(rain);
      
      // Create a new web client and load the page
      webClient = new WebClient();
      rain = webClient.getPage(getPath("/rain.jsf", cid));
      assert !hasRained(rain);
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "a")
   public void testConversationActiveDuringNonFacesRequest() throws Exception
   {
      WebClient client = new WebClient();
      HtmlPage page = client.getPage(getPath("/cloud.jsf"));
      HtmlSpan span = getFirstMatchingElement(page, HtmlSpan.class, "cloudName");
      assert span.getTextContent().equals(Cloud.NAME);
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "tb")
   public void testConversationPropagationToNonExistentConversationLeadsToTransientConversation() throws Exception
   {
      WebClient client = new WebClient();
      Page page = client.getPage(getPath("/cloud.jsf", "org.jboss.jsr299"));
      assert !isLongRunning(page);
      assert !getCid(page).equals("org.jboss.jsr299");
   }

}