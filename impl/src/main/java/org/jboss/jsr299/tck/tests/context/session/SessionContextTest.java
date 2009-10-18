package org.jboss.jsr299.tck.tests.context.session;

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

@Artifact
@IntegrationTest(runLocally=true)
@Resources({
  @Resource(destination=WarArtifactDescriptor.WEB_XML_DESTINATION, source="web.xml"),
  @Resource(destination="SimplePage.html", source="SimplePage.html")
})
@SpecVersion(spec="cdi", version="20091018")
public class SessionContextTest extends AbstractJSR299Test
{
   @Test(groups = { "contexts", "servlet", "integration" })
   @SpecAssertion(section = "6.7.2", id = "aa")
   public void testSessionScopeActiveDuringServiceMethod() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(getContextPath() + "serviceMethodTest");
   }

   @Test(groups = { "contexts", "servlet", "integration" })
   @SpecAssertion(section = "6.7.2", id = "ab")
   public void testSessionScopeActiveDuringDoFilterMethod() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(getContextPath() + "SimplePage.html");
   }

   @Test(groups = { "contexts", "servlet", "integration" })
   @SpecAssertion(section = "6.7.2", id = "b")
   public void testSessionContextSharedBetweenServletRequestsInSameHttpSession() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      TextPage firstRequestResult = webClient.getPage(getContextPath() + "IntrospectSession");
      assert firstRequestResult.getContent() != null;
      assert Long.parseLong(firstRequestResult.getContent()) != 0;
      // Make a second request and make sure the same context is used
      TextPage secondRequestResult = webClient.getPage(getContextPath() + "IntrospectSession");
      assert secondRequestResult.getContent() != null;
      assert Long.parseLong(secondRequestResult.getContent()) == Long.parseLong(firstRequestResult.getContent());
   }

   @Test(groups = { "contexts", "integration" })
   @SpecAssertion(section = "6.7.2", id = "ca")
   public void testSessionContextDestroyedWhenHttpSessionInvalidated() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      TextPage firstRequestResult = webClient.getPage(getContextPath() + "IntrospectSession");
      assert firstRequestResult.getContent() != null;
      assert Long.parseLong(firstRequestResult.getContent()) != 0;
      webClient.getPage(getContextPath() + "InvalidateSession");
      // Make a second request and make sure the same context is not there
      TextPage secondRequestResult = webClient.getPage(getContextPath() + "IntrospectSession");
      assert secondRequestResult.getContent() != null;
      assert Long.parseLong(secondRequestResult.getContent()) != Long.parseLong(firstRequestResult.getContent());
      
      // As final confirmation that the context was destroyed, check that its beans
      // were also destroyed.
      TextPage beanDestructionResult = webClient.getPage(getContextPath() + "InvalidateSession?isBeanDestroyed");
      assert Boolean.parseBoolean(beanDestructionResult.getContent());
   }
   
   @Test(groups = { "contexts", "integration" })
   @SpecAssertion(section = "6.7.2", id = "cb")
   public void testSessionContextDestroyedWhenHttpSessionTimesOut() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      TextPage firstRequestResult = webClient.getPage(getContextPath() + "IntrospectSession");
      assert firstRequestResult.getContent() != null;
      assert Long.parseLong(firstRequestResult.getContent()) != 0;
      webClient.getPage(getContextPath() + "InvalidateSession?timeout=1");
      Thread.sleep(1500);
      // Make a second request and make sure the same context is not there
      TextPage secondRequestResult = webClient.getPage(getContextPath() + "IntrospectSession");
      assert secondRequestResult.getContent() != null;
      assert Long.parseLong(secondRequestResult.getContent()) != Long.parseLong(firstRequestResult.getContent());
      
      // As final confirmation that the context was destroyed, check that its beans
      // were also destroyed.
      TextPage beanDestructionResult = webClient.getPage(getContextPath() + "InvalidateSession?isBeanDestroyed");
      assert Boolean.parseBoolean(beanDestructionResult.getContent());
   }

}
