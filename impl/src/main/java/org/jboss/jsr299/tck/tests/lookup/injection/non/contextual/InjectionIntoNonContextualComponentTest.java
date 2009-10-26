/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.jboss.testharness.impl.packaging.war.WarArtifactDescriptor;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;

@Artifact
@IntegrationTest(runLocally = true)
@Resources( { 
   @Resource(destination = WarArtifactDescriptor.WEB_XML_DESTINATION, source = "web.xml"),
   @Resource(destination = "WEB-INF/faces-config.xml", source = "faces-config.xml"),
   @Resource(destination = "WEB-INF/TestLibrary.tld", source = "TestLibrary.tld"), 
   @Resource(destination = "TagPage.jsp", source = "TagPage.jsp"),
   @Resource(destination = "ManagedBeanTestPage.jsp", source = "ManagedBeanTestPage.jsp")})
@SpecVersion(spec="cdi", version="20091018")
public class InjectionIntoNonContextualComponentTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertions( { 
      @SpecAssertion(section = "5.6", id = "ef"), 
      @SpecAssertion(section = "5.6.4", id = "ae"),
      @SpecAssertion(section = "5.6.4", id = "bn")
   })
   public void testInjectionIntoServlet() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(getContextPath() + "Test/Servlet?test=injection");
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.6.4", id = "af"),
      @SpecAssertion(section = "5.6.4", id = "bm")
   })
   public void testServletInitializerMethodCalled() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(getContextPath() + "Test/Servlet?test=initializer");
   }

   @Test
   @SpecAssertions( { 
      @SpecAssertion(section = "5.6", id = "eg"), 
      @SpecAssertion(section = "5.6.4", id = "ag"),
      @SpecAssertion(section = "5.6.4", id = "bq")
      })
   public void testInjectionIntoFilter() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(getContextPath() + "TestFilter?test=injection");
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.6.4", id = "ah"),
      @SpecAssertion(section = "5.6.4", id = "bp")
   })
   public void testFilterInitializerMethodCalled() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(getContextPath() + "TestFilter?test=initializer");
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.6", id = "ea"),
      @SpecAssertion(section = "5.6.4", id = "ai")
   })
   public void testInjectionIntoServletListener() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(getContextPath() + "Test/ServletListener?test=injection");
   }
   
   @Test
   @SpecAssertion(section = "5.6.4", id = "aj")
   public void testServletListenerInitializerMethodCalled() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(getContextPath() + "Test/ServletListener?test=initializer");
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.6", id = "eb"),
      @SpecAssertion(section = "5.6.4", id = "am"),
      @SpecAssertion(section = "5.6.4", id = "an")
   })
   public void testInjectionIntoTagHandler() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(false);
      WebResponse response = webClient.getPage(getContextPath() + "TagPage.jsp").getWebResponse();
      assert response.getStatusCode() == 200;
      assert response.getContentAsString().contains(TestTagHandler.INJECTION_SUCCESS);
      assert response.getContentAsString().contains(TestTagHandler.INITIALIZER_SUCCESS);
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.6", id = "ec"),
      @SpecAssertion(section = "5.6.4", id="as")
   })
   public void testInjectionIntoTagLibraryListener() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(getContextPath() + "Test/TagLibraryListener?test=injection");
   }
   
   @Test
   @SpecAssertion(section = "5.6.4", id="at")
   public void testTagLibraryListenerInitializerMethodCalled() throws Exception
   {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      webClient.getPage(getContextPath() + "Test/TagLibraryListener?test=initializer");
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.6", id = "d"),
      @SpecAssertion(section = "5.6.4", id = "au"),
      @SpecAssertion(section = "5.6.4", id = "av")
   })
   public void testInjectionIntoJSFManagedBean() throws Exception
   {
      WebClient webclient = new WebClient();
      webclient.setThrowExceptionOnFailingStatusCode(true);
      String content = webclient.getPage(getContextPath() + "ManagedBeanTestPage.jsf").getWebResponse().getContentAsString();
      assert content.contains("Injection works");
      assert content.contains("Initializer works");
   }
}
