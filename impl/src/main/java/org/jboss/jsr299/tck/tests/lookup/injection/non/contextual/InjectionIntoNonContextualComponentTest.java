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
package org.jboss.jsr299.tck.tests.lookup.injection.non.contextual;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;

@SpecVersion(spec="cdi", version="20091101")
public class InjectionIntoNonContextualComponentTest extends AbstractJSR299Test
{
    
    @ArquillianResource
    private URL contextPath;
    
    @Deployment(testable=false)
    public static WebArchive createTestArchive() 
	{
       return new WebArchiveBuilder()
           .withTestClassPackage(InjectionIntoNonContextualComponentTest.class)
           .withWebXml("web2.xml")
           .withExtension("javax.enterprise.inject.spi.Extension")
           .withWebResource("ManagedBeanTestPage.jsp", "ManagedBeanTestPage.jsp")
           .withWebResource("TagPage.jsp", "TagPage.jsp")
           .withWebResource("faces-config.xml", "/WEB-INF/faces-config.xml")
           .withWebResource("TestLibrary.tld", "WEB-INF/TestLibrary.tld")
           .build();
    }    
    
   @Test
   @SpecAssertions( { 
      @SpecAssertion(section = "5.5", id = "ef"), 
      @SpecAssertion(section = "5.5.2", id = "ae"),
      @SpecAssertion(section = "5.5.2", id = "bn"),
      @SpecAssertion(section = "3.8", id = "b")
   })
   public void testInjectionIntoServlet() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(contextPath + "Test/Servlet?test=injection");
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.5.2", id = "af"),
      @SpecAssertion(section = "5.5.2", id = "bm"),
      @SpecAssertion(section = "3.9", id = "b")
   })
   public void testServletInitializerMethodCalled() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(contextPath + "Test/Servlet?test=initializer");
   }

   @Test
   @SpecAssertions( { 
      @SpecAssertion(section = "5.5", id = "eg"), 
      @SpecAssertion(section = "5.5.2", id = "ag"),
      @SpecAssertion(section = "5.5.2", id = "bq"),
      @SpecAssertion(section = "3.8", id = "b")
      })
   public void testInjectionIntoFilter() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(contextPath + "TestFilter?test=injection");
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.5.2", id = "ah"),
      @SpecAssertion(section = "5.5.2", id = "bp"),
      @SpecAssertion(section = "3.9", id = "b")
   })
   public void testFilterInitializerMethodCalled() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(contextPath + "TestFilter?test=initializer");
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.5", id = "ea"),
      @SpecAssertion(section = "5.5.2", id = "ai"),
      @SpecAssertion(section = "3.8", id = "b")
   })
   public void testInjectionIntoServletListener() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(contextPath + "Test/ServletListener?test=injection");
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.5.2", id = "aj"),
      @SpecAssertion(section = "3.9", id = "b")
   })
   public void testServletListenerInitializerMethodCalled() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(contextPath + "Test/ServletListener?test=initializer");
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.5", id = "eb"),
      @SpecAssertion(section = "5.5.2", id = "am"),
      @SpecAssertion(section = "5.5.2", id = "an"),
      @SpecAssertion(section = "3.8", id = "b")
   })
   public void testInjectionIntoTagHandler() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(false);
      WebResponse response = webClient.getPage(contextPath + "TagPage.jsp").getWebResponse();
      assert response.getStatusCode() == 200;
      assert response.getContentAsString().contains(TestTagHandler.INJECTION_SUCCESS);
      assert response.getContentAsString().contains(TestTagHandler.INITIALIZER_SUCCESS);
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.5", id = "ec"),
      @SpecAssertion(section = "5.5.2", id="as"),
      @SpecAssertion(section = "3.8", id = "b")
   })
   public void testInjectionIntoTagLibraryListener() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(contextPath + "Test/TagLibraryListener?test=injection");
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.5.2", id="at"),
      @SpecAssertion(section = "3.9", id = "b")
   })
   public void testTagLibraryListenerInitializerMethodCalled() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(contextPath + "Test/TagLibraryListener?test=initializer");
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.5", id = "d"),
      @SpecAssertion(section = "5.5.2", id = "au"),
      @SpecAssertion(section = "5.5.2", id = "av"),
      @SpecAssertion(section = "3.8", id = "b")
   })
   public void testInjectionIntoJSFManagedBean() throws Exception
   {
      WebClient webclient = new WebClient();
      webclient.setThrowExceptionOnFailingStatusCode(true);
      String content = webclient.getPage(contextPath + "ManagedBeanTestPage.jsf").getWebResponse().getContentAsString();
      assert content.contains("Injection works");
      assert content.contains("Initializer works");
   }
}
