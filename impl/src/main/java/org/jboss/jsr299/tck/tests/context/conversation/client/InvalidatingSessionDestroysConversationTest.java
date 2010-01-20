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

/**
 * @author Nicklas Karlsson
 */
@Artifact(addCurrentPackage=false)
@Classes({Storm.class, ConversationTestPhaseListener.class, ConversationStatusServlet.class, Cloud.class, CloudController.class})
@IntegrationTest(runLocally=true)
@Resources({
  @Resource(destination="WEB-INF/faces-config.xml", source="faces-config.xml"),
  @Resource(destination="clouds.jspx", source="clouds.jsf"),
  @Resource(destination="cloud.jspx", source="cloud.jsf")
})
@WebXml("web.xml")
@SpecVersion(spec="cdi", version="20091101")
public class InvalidatingSessionDestroysConversationTest extends AbstractConversationTest
{
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "qa")
   // TODO this test doesn't precisely probe the boundaries of the service() method
   public void testInvalidatingSessionDestroysConversation() throws Exception
   {
      WebClient webClient = new WebClient();
      resetCloud(webClient);
      webClient.getPage(getPath("/clouds.jsf"));
      assert !isCloudDestroyed(webClient);
      invalidateSession(webClient);
      assert isCloudDestroyed(webClient);
   }

}