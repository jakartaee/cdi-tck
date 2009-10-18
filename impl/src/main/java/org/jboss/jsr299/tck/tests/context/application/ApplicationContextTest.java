package org.jboss.jsr299.tck.tests.context.application;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.jboss.testharness.impl.packaging.war.WarArtifactDescriptor;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * @author David Allen
 */
@Artifact
@IntegrationTest(runLocally=true)
@Resources({
  @Resource(destination=WarArtifactDescriptor.WEB_XML_DESTINATION, source="web.xml"),
  @Resource(destination="SimplePage.html", source="SimplePage.html")
})
@SpecVersion(spec="cdi", version="20091018")
public class ApplicationContextTest extends AbstractJSR299Test
{

   @Test(groups = { "contexts", "servlet", "integration" })
   @SpecAssertion(section = "6.7.3", id = "aa")
   public void testApplicationScopeActiveDuringServiceMethod() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(getContextPath() + "serviceMethodTest");
   }

   @Test(groups = { "contexts", "servlet", "integration" })
   @SpecAssertion(section = "6.7.3", id = "ab")
   public void testApplicationScopeActiveDuringDoFilterMethod() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(getContextPath() + "SimplePage.html");
   }

   @Test(groups = { "contexts", "integration" })
   @SpecAssertion(section = "6.7.3", id = "e")
   public void testApplicationContextSharedBetweenServletRequests() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      TextPage firstRequestResult = webClient.getPage(getContextPath() + "IntrospectApplication");
      assert firstRequestResult.getContent() != null;
      assert Double.parseDouble(firstRequestResult.getContent()) != 0;
      // Make a second request and make sure the same context is used
      TextPage secondRequestResult = webClient.getPage(getContextPath() + "IntrospectApplication");
      assert secondRequestResult.getContent() != null;
      // should be same random number
      assert Double.parseDouble(secondRequestResult.getContent()) == Double.parseDouble(firstRequestResult.getContent());
   }

}
