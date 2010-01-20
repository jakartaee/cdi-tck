package org.jboss.jsr299.tck.tests.context.conversation.client;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.jboss.testharness.impl.packaging.war.WebXml;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

/**
 * @author Nicklas Karlsson
 * @author Dan Allen
 * 
 */
@Artifact(addCurrentPackage=false)
@Classes({Storm.class, ConversationTestPhaseListener.class, ConversationStatusServlet.class, Cloud.class, CloudController.class})
@IntegrationTest(runLocally=true)
@Resources({
  @Resource(destination="cloud.jspx", source="cloud.jsf"),
  @Resource(destination="storm.jspx", source="storm.jsf"),
  @Resource(destination="clouds.jspx", source="clouds.jsf"),
  @Resource(destination="/WEB-INF/faces-config.xml", source="faces-config.xml")
})
@WebXml("web.xml")
@SpecVersion(spec="cdi", version="20091101")
public class ManualCidPropagationTest extends AbstractConversationTest
{
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "n")
   public void testManualCidPropagation() throws Exception
   {
      WebClient webClient = new WebClient();
      HtmlPage storm = webClient.getPage(getPath("/storm.jsf"));
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(storm, HtmlSubmitInput.class, "beginConversationButton");
      storm = beginConversationButton.click();
      
      String c1 = getCid(storm);
      
      HtmlPage cloud = webClient.getPage(getPath("/cloud.jsf", c1));
      
      String c2 = getCid(cloud);
      
      assert isLongRunning(cloud);
      assert c1.equals(c2);
   }
   
}
