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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * @author Nicklas Karlsson
 * @author Martin Kouba
 */
@SpecVersion(spec="cdi", version="20091101")
public class LongRunningConversationPropagatedByFacesContextTest extends AbstractConversationTest
{
   
   private static final String STORM_STRENGTH = "12";
   private static final String REDIRECT_STORM_STRENGTH = "15";
   
   @Deployment(testable=false)
   public static WebArchive createTestArchive() 
	{
       return new WebArchiveBuilder()
           .withTestClass(LongRunningConversationPropagatedByFacesContextTest.class)
           .withClasses(Storm.class, ConversationTestPhaseListener.class, ConversationStatusServlet.class, Cloud.class,
                       OutermostFilter.class)
           .withWebResource("storm.jsf", "storm.jspx")
           .withWebResource("thunder.jsf", "thunder.jspx")
           .withWebResource("lightening.jsf", "lightening.jspx")
           .withWebResource("faces-config.xml", "/WEB-INF/faces-config.xml")
           .withWebXml("web.xml")
           .build();
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertions({
      @SpecAssertion(section = "6.7.4", id = "l"),
      @SpecAssertion(section = "2.4.1", id="ba")
   })
      
   public void testConversationPropagated() throws Exception
   {
      WebClient webClient = new WebClient();
      HtmlPage storm = webClient.getPage(getPath("storm.jsf"));
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(storm, HtmlSubmitInput.class, "beginConversationButton");
      storm = beginConversationButton.click();
      HtmlTextInput stormStrength = getFirstMatchingElement(storm, HtmlTextInput.class, "stormStrength");
      stormStrength.setValueAttribute(STORM_STRENGTH);
      String stormCid = getCid(storm);
      HtmlSubmitInput thunderButton = getFirstMatchingElement(storm, HtmlSubmitInput.class, "thunderButton");
      HtmlPage thunder = thunderButton.click();
      assert stormCid.equals(getCid(thunder));
      stormStrength = getFirstMatchingElement(thunder, HtmlTextInput.class, "stormStrength");
      assert stormStrength.getValueAttribute().equals(STORM_STRENGTH);
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.7.4", id = "m")
   public void testConversationPropagatedOverRedirect() throws Exception
   {
      WebClient webClient = new WebClient();
      HtmlPage storm = webClient.getPage(getPath("storm.jsf"));
      HtmlSubmitInput beginConversationButton = getFirstMatchingElement(storm, HtmlSubmitInput.class, "beginConversationButton");
      storm = beginConversationButton.click();
      HtmlTextInput stormStrength = getFirstMatchingElement(storm, HtmlTextInput.class, "stormStrength");
      stormStrength.setValueAttribute(REDIRECT_STORM_STRENGTH);
      String stormCid = getCid(storm);
      HtmlSubmitInput lighteningButton = getFirstMatchingElement(storm, HtmlSubmitInput.class, "lighteningButton");
      HtmlPage lightening = lighteningButton.click();
      assert lightening.getWebResponse().getRequestUrl().toString().contains("lightening.jsf");
      assert stormCid.equals(getCid(lightening));
      stormStrength = getFirstMatchingElement(lightening, HtmlTextInput.class, "stormStrength");
      assert stormStrength.getValueAttribute().equals(REDIRECT_STORM_STRENGTH);
   }

}