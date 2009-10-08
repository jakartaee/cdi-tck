package org.jboss.jsr299.tck.tests.context.request;

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
@SpecVersion(spec="cdi", version="PFD2")
public class RequestContextTest extends AbstractJSR299Test
{

   /**
    * The request scope is active during the service() method of any Servlet in
    * the web application.
    */
   @Test(groups = { "contexts", "servlet", "integration" })
   @SpecAssertion(section = "6.7.1", id = "aa")
   public void testRequestScopeActiveDuringServiceMethod() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(getContextPath() + "serviceMethodTest");
   }

   /**
    * The request scope is active during the doFilter() method of any Servlet in
    * the web application.
    */
   @Test(groups = { "contexts", "servlet", "integration" })
   @SpecAssertion(section = "6.7.1", id = "ab")
   public void testRequestScopeActiveDuringServletFilter() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(getContextPath() + "SimplePage.html");
   }

   /**
    * The request context is destroyed at the end of the servlet request, after
    * the Servlet service() method returns.
    */
   @Test(groups = { "contexts", "servlet", "integration", "rewrite" })
   @SpecAssertion(section="6.7.1", id="baa")
   // TODO Need to tidy this one up, make it actually check that the context is active til after the service method ends
   public void testRequestScopeIsDestroyedAfterServletRequest() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      TextPage firstRequestResult = webClient.getPage(getContextPath() + "IntrospectRequest");
      assert firstRequestResult.getContent() != null;
      assert Double.parseDouble(firstRequestResult.getContent()) != 0;
      // Make a second request and make sure the same context is not there
      TextPage secondRequestResult = webClient.getPage(getContextPath() + "IntrospectRequest");
      assert secondRequestResult.getContent() != null;
      assert Double.parseDouble(secondRequestResult.getContent()) != Double.parseDouble(firstRequestResult.getContent());
      
      // As final confirmation that the context was destroyed, check that its beans
      // were also destroyed.
//      TextPage beanDestructionResult = webClient.getPage(getContextPath() + "InvalidateSession?isBeanDestroyed");
//      assert Boolean.parseBoolean(beanDestructionResult.getContent());
   }

}
