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
package org.jboss.jsr299.tck.tests.lookup.injection.enterprise;

import javax.enterprise.inject.spi.ProcessInjectionTarget;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
import org.testng.annotations.Test;

/**
 * This test verifies that ProcessInjectionTarget event is fired for a session bean and EJB interceptor.
 * @author Jozef Hartinger
 *
 */

@Artifact
@Resource(source = "javax.enterprise.inject.spi.Extension", destination = "WEB-INF/classes/META-INF/services/javax.enterprise.inject.spi.Extension")
@IntegrationTest
@SpecVersion(spec="cdi", version="20091018")
public class ProcessInjectionTargetEventTest extends AbstractJSR299Test
{
   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "aab"),
      @SpecAssertion(section = "11.5.6", id = "abb")})
   public void testEventFiredForSessionBean()
   {
      ProcessInjectionTarget<Farm> event = ProcessInjectionTargetEventObserver.getSessionBeanEvent();
      assert event != null;
      assert event.getAnnotatedType().getBaseType().equals(Farm.class);
      assert event.getAnnotatedType().getFields().size() == 2;
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "aaf"),
      @SpecAssertion(section = "11.5.6", id = "abf")})
   public void testEventFiredForTagHandler()
   {
      ProcessInjectionTarget<FarmInterceptor> event = ProcessInjectionTargetEventObserver.getEjbInterceptorEvent();
      assert event != null;
      assert event.getAnnotatedType().getBaseType().equals(FarmInterceptor.class);
      assert event.getAnnotatedType().getFields().size() == 1;
   }
}
