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
package org.jboss.jsr299.tck.tests.lookup.el.integration;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.jboss.testharness.impl.packaging.war.WarArtifactDescriptor;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.WebClient;

@Artifact
@IntegrationTest(runLocally = true)
@Resources( { 
   @Resource(destination = WarArtifactDescriptor.WEB_XML_DESTINATION, source = "web.xml"), 
   @Resource(destination = "WEB-INF/faces-config.xml", source = "faces-config.xml"), 
   @Resource(destination = "JSFTestPage.jsp", source = "JSFTestPage.jsp"),
   @Resource(destination = "JSPTestPage.jsp", source = "JSPTestPage.jsp")})
@SpecVersion(spec="cdi", version="20091101")
public class IntegrationWithUnifiedELTest extends AbstractJSR299Test
{
   @Test(groups = {"el" } )
   @SpecAssertion(section = "12.4", id = "a")
   public void testELResolverRegisteredWithJsf() throws Exception
   {
      WebClient webclient = new WebClient();
      String content = webclient.getPage(getContextPath() + "JSFTestPage.jsf").getWebResponse().getContentAsString();
      assert content.contains("Dolly");
   }

   @Test(groups = { "el" } )
   @SpecAssertion(section = "12.4", id = "a")
   public void testELResolverRegisteredWithServletContainer() throws Exception
   {
      WebClient webclient = new WebClient();
      String content = webclient.getPage(getContextPath() + "JSPTestPage.jsp").getWebResponse().getContentAsString();
      assert content.contains("Dolly");
   }
}
