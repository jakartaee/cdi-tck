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

import java.util.EventListener;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

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

/**
 * This test verifies that ProcessAnnotatedType and ProcessInjectionTarget events are fired
 * for various Java EE components and tests the AnnotatedType implementation.
 * @author Jozef Hartinger
 *
 */
@Artifact
@Resources( { 
   @Resource(destination = WarArtifactDescriptor.WEB_XML_DESTINATION, source = "web.xml"),
   @Resource(destination = "WEB-INF/faces-config.xml", source = "faces-config.xml"),
   @Resource(destination = "WEB-INF/TestLibrary.tld", source = "TestLibrary.tld"),
   @Resource(destination = "TagPage.jsp", source = "TagPage.jsp"),
   @Resource(destination = "ManagedBeanTestPage.jsp", source = "ManagedBeanTestPage.jsp"),
   @Resource(source = "javax.enterprise.inject.spi.Extension", destination = "WEB-INF/classes/META-INF/services/javax.enterprise.inject.spi.Extension") })
@IntegrationTest
@SpecVersion(spec = "cdi", version = "20091018")
public class ContainerEventTest extends AbstractJSR299Test
{
   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "aac"),
      @SpecAssertion(section = "11.5.6", id = "abc"),
      @SpecAssertion(section = "12.3", id = "de")})
   //WELD-204
   public void testProcessInjectionTargetEventFiredForServletListener()
   {
      assert ProcessInjectionTargetObserver.getListenerEvent() != null;
      validateServletListenerAnnotatedType(ProcessInjectionTargetObserver.getListenerEvent().getAnnotatedType());
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "aad"),
      @SpecAssertion(section = "11.5.6", id = "abd"),
      @SpecAssertion(section = "12.3", id = "df")})
   public void testProcessInjectionTargetEventFiredForTagHandler()
   {
      assert ProcessInjectionTargetObserver.getTagHandlerEvent() != null;
      validateTagHandlerAnnotatedType(ProcessInjectionTargetObserver.getTagHandlerEvent().getAnnotatedType());
   }

   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "aae"),
      @SpecAssertion(section = "11.5.6", id = "abe"),
      @SpecAssertion(section = "12.3", id = "dg")})
   //WELD-204
   public void testProcessInjectionTargetEventFiredForTagLibraryListener()
   {
      assert ProcessInjectionTargetObserver.getTagLibraryListenerEvent() != null;
      validateTagLibraryListenerAnnotatedType(ProcessInjectionTargetObserver.getTagLibraryListenerEvent().getAnnotatedType());
   }

   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "aah"),
      @SpecAssertion(section = "11.5.6", id = "abh"),
      @SpecAssertion(section = "12.3", id = "dj")})
   //WELD-204
   public void testProcessInjectionTargetEventFiredForServlet()
   {
      assert ProcessInjectionTargetObserver.getServletEvent() != null;
      validateServletAnnotatedType(ProcessInjectionTargetObserver.getServletEvent().getAnnotatedType());
   }

   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "aai"),
      @SpecAssertion(section = "11.5.6", id = "abi"),
      @SpecAssertion(section = "12.3", id = "dk")})
   //WELD-204
   public void testProcessInjectionTargetEventFiredForFilter()
   {
      assert ProcessInjectionTargetObserver.getFilterEvent() != null;
      validateFilterAnnotatedType(ProcessInjectionTargetObserver.getFilterEvent().getAnnotatedType());
   }
   
   @Test(groups = "ri-broken")
   @SpecAssertion(section = "12.3", id = "dd")
      public void testProcessInjectionTargetEventFiredForJsfManagedBean()
   {
      assert ProcessInjectionTargetObserver.getJsfManagedBeanEvent() != null;
      validateJsfManagedBeanAnnotatedType(ProcessInjectionTargetObserver.getJsfManagedBeanEvent().getAnnotatedType());
   }
   
   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "aas"),
      @SpecAssertion(section = "11.5.6", id = "aao"),
      @SpecAssertion(section = "11.5.6", id = "aan"),
      @SpecAssertion(section = "11.5.6", id = "aap")})
   //WELD-204
   public void testTypeOfProcessInjectionTargetParameter() {
      assert !ProcessInjectionTargetObserver.isStringObserved();
      assert ProcessInjectionTargetObserver.isTagHandlerSubTypeObserved();
      assert !ProcessInjectionTargetObserver.isTagHandlerSuperTypeObserved();
      assert !ProcessInjectionTargetObserver.isServletSuperTypeObserved();
      assert ProcessInjectionTargetObserver.isServletSubTypeObserved();
      assert !ProcessInjectionTargetObserver.isListenerSuperTypeObserved();
   }
   
   @Test
   @SpecAssertion(section = "12.3", id = "be")
   public void testProcessAnnotatedTypeEventFiredForServletListener() {
      assert ProcessAnnotatedTypeObserver.getListenerEvent() != null;
      validateServletListenerAnnotatedType(ProcessAnnotatedTypeObserver.getListenerEvent().getAnnotatedType());
   }
   
   @Test
   @SpecAssertion(section = "12.3", id = "bf")
   public void testProcessAnnotatedTypeEventFiredForTagHandler() {
      assert ProcessAnnotatedTypeObserver.getTagHandlerEvent() != null;
      validateTagHandlerAnnotatedType(ProcessAnnotatedTypeObserver.getTagHandlerEvent().getAnnotatedType());
   }
   
   @Test
   @SpecAssertion(section = "12.3", id = "bg")
   public void testProcessAnnotatedTypeEventFiredForTagLibraryListener() {
      assert ProcessAnnotatedTypeObserver.getTagLibraryListenerEvent() != null;
      validateTagLibraryListenerAnnotatedType(ProcessAnnotatedTypeObserver.getTagLibraryListenerEvent().getAnnotatedType());
   }
   
   @Test
   @SpecAssertion(section = "12.3", id = "bj")
   public void testProcessAnnotatedTypeEventFiredForServlet() {
      assert ProcessAnnotatedTypeObserver.getServletEvent() != null;
      validateServletAnnotatedType(ProcessAnnotatedTypeObserver.getServletEvent().getAnnotatedType());
   }
   
   @Test
   @SpecAssertion(section = "12.3", id = "bk")
   public void testProcessAnnotatedTypeEventFiredForFilter() {
      assert ProcessAnnotatedTypeObserver.getFilterEvent() != null;
      validateFilterAnnotatedType(ProcessAnnotatedTypeObserver.getFilterEvent().getAnnotatedType());
   }
   
   @Test
   @SpecAssertion(section = "12.3", id = "bd")
   public void testProcessAnnotatedTypeEventFiredForJsfManagedBean() {
      assert ProcessAnnotatedTypeObserver.getJsfManagedBeanEvent() != null;
      validateJsfManagedBeanAnnotatedType(ProcessAnnotatedTypeObserver.getJsfManagedBeanEvent().getAnnotatedType());
   }
   
   private void validateServletListenerAnnotatedType(AnnotatedType<TestListener> type) {
      assert type.getBaseType().equals(TestListener.class);
      assert type.getAnnotations().isEmpty();
      assert type.getMethods().size() == 2;
      assert type.getFields().iterator().next().isAnnotationPresent(Inject.class);
      assert type.getAnnotations().isEmpty();
   }
   
   private void validateTagHandlerAnnotatedType(AnnotatedType<TestTagHandler> type) {
      assert type.getBaseType().equals(TestTagHandler.class);
      assert typeSetMatches(type.getTypeClosure(), TestTagHandler.class, SimpleTagSupport.class, SimpleTag.class, JspTag.class);
      assert type.getAnnotations().size() == 1;
      assert type.isAnnotationPresent(Any.class);
   }
   
   private void validateTagLibraryListenerAnnotatedType(AnnotatedType<TagLibraryListener> type) {
      assert type.getBaseType().equals(TagLibraryListener.class);
      assert typeSetMatches(type.getTypeClosure(), TagLibraryListener.class, ServletContextListener.class, EventListener.class, Object.class);
      assert type.getFields().size() == 1;
      assert type.getFields().iterator().next().getJavaMember().getName().equals("sheep");
      assert type.getConstructors().size() == 1;
      assert type.getMethods().size() == 2;
   }
   
   private void validateServletAnnotatedType(AnnotatedType<TestServlet> type) {
      assert type.getBaseType().equals(TestServlet.class);
      assert typeSetMatches(type.getTypeClosure(), TestServlet.class, HttpServlet.class, GenericServlet.class, Servlet.class, ServletConfig.class, Object.class);
      assert type.getAnnotations().isEmpty();
   }
   
   private void validateFilterAnnotatedType(AnnotatedType<TestFilter> type) {
      assert type.getBaseType().equals(TestFilter.class);
      assert typeSetMatches(type.getTypeClosure(), TestFilter.class, Filter.class, Object.class);
      assert type.getFields().size() == 2;
      assert type.getConstructors().size() == 1;
      assert type.getConstructors().iterator().next().getParameters().isEmpty();
      assert type.getMethods().size() == 3;
   }
   
   private void validateJsfManagedBeanAnnotatedType(AnnotatedType<Farm> type) {
      assert type.getFields().size() == 1;
      assert type.getFields().iterator().next().getJavaMember().getName().equals("sheep");
      assert type.getFields().iterator().next().isAnnotationPresent(Inject.class);
      assert type.getMethods().size() == 1;
      assert type.getMethods().iterator().next().getBaseType().equals(boolean.class);
   }
}
