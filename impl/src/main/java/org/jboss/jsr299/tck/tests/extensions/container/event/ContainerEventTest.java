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
package org.jboss.jsr299.tck.tests.extensions.container.event;

import static javax.enterprise.inject.spi.SessionBeanType.STATEFUL;
import static javax.enterprise.inject.spi.SessionBeanType.STATELESS;

import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.AnnotatedType;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.testng.annotations.Test;

/**
 * Tests for ProcessAnnotatedType, ProcessBean and ProcessInjectionTarget events.
 * @author Jozef Hartinger
 *
 */
@Artifact
@Resources({
   @Resource(source = "javax.enterprise.inject.spi.Extension", destination = "META-INF/services/javax.enterprise.inject.spi.Extension"),
   @Resource(source = "ejb-jar.xml", destination = "META-INF/ejb-jar.xml")})
@IntegrationTest
@SpecVersion(spec = "cdi", version = "20091101")
@Packaging(PackagingType.EAR)
public class ContainerEventTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertions({
   @SpecAssertion(section = "12.3", id = "ba"),
   @SpecAssertion(section = "11.5.6", id = "aaa")})
   public void testProcessInjectionTargetFiredForManagedBean() {
      assert ProcessInjectionTargetObserver.getManagedBeanEvent() != null;
      validateManagedBean(ProcessInjectionTargetObserver.getManagedBeanEvent().getAnnotatedType());
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "aab"),
      @SpecAssertion(section = "11.5.6", id = "abb"),
      @SpecAssertion(section = "12.3", id = "db")})
   public void testProcessInjectionTargetFiredForSessionBean() {
      assert ProcessInjectionTargetObserver.getStatelessSessionBeanEvent() != null;
      assert ProcessInjectionTargetObserver.getStatefulSessionBeanEvent() != null;
      validateStatelessSessionBean(ProcessInjectionTargetObserver.getStatelessSessionBeanEvent().getAnnotatedType());
      validateStatefulSessionBean(ProcessInjectionTargetObserver.getStatefulSessionBeanEvent().getAnnotatedType());
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "aaf"),
      @SpecAssertion(section = "11.5.6", id = "abf"),
      @SpecAssertion(section = "12.3", id = "dh")})
   public void testProcessInjectionTargetFiredForSessionBeanInterceptor() {
      assert ProcessInjectionTargetObserver.getSessionBeanInterceptorEvent() != null;
      validateSessionBeanInterceptor(ProcessInjectionTargetObserver.getSessionBeanInterceptorEvent().getAnnotatedType());
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section="11.5.6", id="aal"),
      @SpecAssertion(section="11.5.6", id="aam")
   })
   public void testTypeOfProcessInjectionTargetParameter() {
      assert ProcessInjectionTargetObserver.getEvent1Observed() == 0;
      assert ProcessInjectionTargetObserver.getEvent2Observed() == 0;
      assert ProcessInjectionTargetObserver.getEvent3Observed() == 0;
      assert ProcessInjectionTargetObserver.getEvent4Observed() == 0;
      assert ProcessInjectionTargetObserver.getEvent5Observed() == 1;
      assert ProcessInjectionTargetObserver.getEventWithTypeVariable() != null;
   }

   @Test
   @SpecAssertion(section = "12.3", id= "bb")
   public void testProcessAnnotatedTypeFiredForSessionBean() {
      assert ProcessAnnotatedTypeObserver.getStatelessSessionBeanEvent() != null;
      assert ProcessAnnotatedTypeObserver.getStatefulSessionBeanEvent() != null;
      validateStatelessSessionBean(ProcessAnnotatedTypeObserver.getStatelessSessionBeanEvent().getAnnotatedType());
      validateStatefulSessionBean(ProcessAnnotatedTypeObserver.getStatefulSessionBeanEvent().getAnnotatedType());
   }
   
   @Test
   @SpecAssertion(section = "12.3", id= "bh")
   public void testProcessAnnotatedTypeFiredForSessionBeanInterceptor() {
      assert ProcessAnnotatedTypeObserver.getSessionBeanInterceptorEvent() != null;
      validateSessionBeanInterceptor(ProcessAnnotatedTypeObserver.getSessionBeanInterceptorEvent().getAnnotatedType());
   }
   
   @Test
   @SpecAssertion(section = "11.5.8", id = "ba")
   public void testProcessManagedBeanFired()
   {
      assert ProcessBeanObserver.getProcessManagedBeanEvent() != null;
      validateManagedBean(ProcessBeanObserver.getProcessManagedBeanEvent().getAnnotatedBeanClass());
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section="11.5.8", id="c"),
      @SpecAssertion(section="12.3", id="fb")})
   public void testProcessSessionBeanFiredForStatelessSessionBean() {
      assert ProcessBeanObserver.getProcessStatelessSessionBeanEvent() != null;
      validateStatelessSessionBean(ProcessBeanObserver.getProcessStatelessSessionBeanEvent().getAnnotatedBeanClass());
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section="11.5.8", id="c"),
      @SpecAssertion(section="12.3", id="fb"),
      @SpecAssertion(section="12.3", id="g")})
      public void testProcessSessionBeanFiredForStatefulSessionBean() {
      assert ProcessBeanObserver.getProcessStatefulSessionBeanEvent() != null;
      validateStatefulSessionBean(ProcessBeanObserver.getProcessStatefulSessionBeanEvent().getAnnotatedBeanClass());
   }
   
   @Test
   @SpecAssertion(section="11.5.8", id="hb")
   public void testGetEJBName() {
      assert ProcessBeanObserver.getProcessStatelessSessionBeanEvent().getEjbName().equals("sheep");
      assert ProcessBeanObserver.getProcessStatefulSessionBeanEvent().getEjbName().equals("cow");
   }
   
   @Test
   @SpecAssertion(section="11.5.8", id="hc")
   public void testGetSessionBeanType() {
      assert ProcessBeanObserver.getProcessStatelessSessionBeanEvent().getSessionBeanType().equals(STATELESS);
      assert ProcessBeanObserver.getProcessStatefulSessionBeanEvent().getSessionBeanType().equals(STATEFUL);
   }
   
   private void validateStatelessSessionBean(Annotated type) {
      assert type.getBaseType().equals(Sheep.class);
      assert typeSetMatches(type.getTypeClosure(), Sheep.class, SheepLocal.class, Object.class);
      assert type.getAnnotations().size() == 2;
      assert annotationSetMatches(type.getAnnotations(), Tame.class, Stateless.class); //TODO Check
   }
   
   private void validateStatefulSessionBean(Annotated type) {
      assert type.getBaseType().equals(Cow.class);
      assert typeSetMatches(type.getTypeClosure(), Cow.class, CowLocal.class, Object.class);
      assert type.getAnnotations().size() == 0;
   }
   
   private void validateSessionBeanInterceptor(AnnotatedType<SheepInterceptor> type) {
      assert type.getBaseType().equals(SheepInterceptor.class);
      assert typeSetMatches(type.getTypeClosure(), SheepInterceptor.class, Object.class);
      assert type.getAnnotations().size() == 0;
      assert type.getFields().size() == 0;
      assert type.getMethods().size() == 1;
   }
   
   private void validateManagedBean(AnnotatedType<Farm> type) {
      assert type.getBaseType().equals(Farm.class);
      assert typeSetMatches(type.getTypeClosure(), Farm.class, Object.class);
      assert type.getFields().size() == 1;
      assert type.getFields().iterator().next().isAnnotationPresent(Produces.class);
      assert type.getMethods().size() == 1;
      assert type.getMethods().iterator().next().isAnnotationPresent(Produces.class);
   }
}
