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
package org.jboss.jsr299.tck.tests.context.application;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * @author David Allen
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec="cdi", version="20091101")
public class ApplicationContextTest extends AbstractJSR299Test
{
    
    @ArquillianResource
    private URL contextPath;
    
    @Deployment(testable=false)
    public static WebArchive createTestArchive() 
	{
        return new WebArchiveBuilder()
            .withTestClassPackage(ApplicationContextTest.class)
            .withWebXml("web.xml")
            .withWebResource("SimplePage.html")
            .build();
    }

   @Test(groups = { "contexts", "servlet", "integration" })
   @SpecAssertion(section = "6.7.3", id = "aa")
   public void testApplicationScopeActiveDuringServiceMethod() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(contextPath + "TestServlet?test=servlet");
   }

   @Test(groups = { "contexts", "servlet", "integration" })
   @SpecAssertion(section = "6.7.3", id = "ab")
   public void testApplicationScopeActiveDuringDoFilterMethod() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(contextPath + "SimplePage.html");
   }
   
   @Test(groups = { "contexts", "integration" })
   @SpecAssertion(section = "6.7.3", id = "ac")
   public void testApplicationScopeActiveDuringServletContextListenerInvocation() throws Exception {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(contextPath + "TestServlet?test=servletContextListener");
   }
   
   @Test(groups = { "contexts", "integration" })
   @SpecAssertion(section = "6.7.3", id = "ad")
   public void testApplicationScopeActiveDuringHttpSessionListenerInvocation() throws Exception {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(contextPath + "TestServlet?test=httpSessionListener");
   }
   
   @Test(groups = { "contexts", "integration" })
   @SpecAssertion(section = "6.7.3", id = "af")
   public void testApplicationScopeActiveDuringServletRequestListenerInvocation() throws Exception {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(contextPath + "TestServlet?test=servletRequestListener");
   }

   @Test(groups = { "contexts", "integration" })
   @SpecAssertion(section = "6.7.3", id = "e")
   public void testApplicationContextSharedBetweenServletRequests() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      TextPage firstRequestResult = webClient.getPage(contextPath + "IntrospectApplication");
      assert firstRequestResult.getContent() != null;
      assert Double.parseDouble(firstRequestResult.getContent()) != 0;
      // Make a second request and make sure the same context is used
      TextPage secondRequestResult = webClient.getPage(contextPath + "IntrospectApplication");
      assert secondRequestResult.getContent() != null;
      // should be same random number
      assert Double.parseDouble(secondRequestResult.getContent()) == Double.parseDouble(firstRequestResult.getContent());
   }

   /**
    *  Related to CDITCK-96.
    *  
    * @throws Exception
    */
   @Test(groups = { "contexts", "integration" })
   @SpecAssertion(section = "6.7.3", id = "e")
   public void testApplicationContextSharedBetweenJaxRsRequests() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      TextPage firstRequestResult = webClient.getPage(contextPath + "jaxrs/application-id");
      assert firstRequestResult.getContent() != null;
      assert Double.parseDouble(firstRequestResult.getContent()) != 0;
      // Make a second request and make sure the same context is used
      TextPage secondRequestResult = webClient.getPage(contextPath + "jaxrs/application-id");
      assert secondRequestResult.getContent() != null;
      // should be same random number
      assert Double.parseDouble(secondRequestResult.getContent()) == Double.parseDouble(firstRequestResult.getContent());
   }

}
