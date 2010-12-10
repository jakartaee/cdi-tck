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
package org.jboss.jsr299.tck.tests.context.conversation;

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
      HtmlPage storm = webClient.getPage(getPath("storm.jsf"));
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(storm, HtmlSubmitInput.class, "beginConversationButton");
      storm = beginConversationButton.click();
      
      String c1 = getCid(storm);
      
      HtmlPage cloud = webClient.getPage(getPath("cloud.jsf", c1));
      
      String c2 = getCid(cloud);
      
      assert isLongRunning(cloud);
      assert c1.equals(c2);
   }
   
}
