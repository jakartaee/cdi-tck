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

import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContextListener;
import javax.servlet.jsp.tagext.SimpleTag;

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

@Artifact
@Resources( { @Resource(destination = WarArtifactDescriptor.WEB_XML_DESTINATION, source = "web.xml"),
   @Resource(destination = "WEB-INF/faces-config.xml", source = "faces-config.xml"),
   @Resource(destination = "WEB-INF/TestLibrary.tld", source = "TestLibrary.tld"),
   @Resource(destination = "TagPage.jsp", source = "TagPage.jsp"),
   @Resource(destination = "ManagedBeanTestPage.jsp", source = "ManagedBeanTestPage.jsp"),
   @Resource(source = "javax.enterprise.inject.spi.Extension", destination = "WEB-INF/classes/META-INF/services/javax.enterprise.inject.spi.Extension") })
@IntegrationTest
@SpecVersion(spec="cdi", version="20091018")
public class ProcessInjectionTargetEventTest extends AbstractJSR299Test
{
   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "aac"),
      @SpecAssertion(section = "11.5.6", id = "abc")})
   public void testEventFiredForServletListener()
   {
      ProcessInjectionTarget<ServletContextListener> event = ProcessInjectionTargetEventObserver.getListenerEvent();
      assert event != null;
      assert event.getAnnotatedType().getBaseType().equals(TestListener.class);
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "aad"),
      @SpecAssertion(section = "11.5.6", id = "abd")})
   public void testEventFiredForTagHandler()
   {
      ProcessInjectionTarget<SimpleTag> event = ProcessInjectionTargetEventObserver.getTagHandlerEvent();
      assert event != null;
      assert event.getAnnotatedType().getBaseType().equals(TestTagHandler.class);
   }

   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "aae"),
      @SpecAssertion(section = "11.5.6", id = "abe")})
   public void testEventFiredForTagLibraryListener()
   {
      ProcessInjectionTarget<ServletContextListener> event = ProcessInjectionTargetEventObserver.getTagLibraryListenerEvent();
      assert event != null;
      assert event.getAnnotatedType().getBaseType().equals(TagLibraryListener.class);
   }

   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "aah"),
      @SpecAssertion(section = "11.5.6", id = "abh")})
   public void testEventFiredForServlet()
   {
      ProcessInjectionTarget<Servlet> event = ProcessInjectionTargetEventObserver.getServletEvent();
      assert event != null;
      assert event.getAnnotatedType().getBaseType().equals(TestServlet.class);
   }

   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "aai"),
      @SpecAssertion(section = "11.5.6", id = "abi")})
   public void testEventFiredForFilter()
   {
      ProcessInjectionTarget<Filter> event = ProcessInjectionTargetEventObserver.getFilterEvent();
      assert event != null;
      assert event.getAnnotatedType().getBaseType().equals(TestFilter.class);
   }
}
