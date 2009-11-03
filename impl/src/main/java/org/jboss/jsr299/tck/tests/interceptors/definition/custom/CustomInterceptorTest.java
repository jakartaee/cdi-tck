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
package org.jboss.jsr299.tck.tests.interceptors.definition.custom;

import static javax.enterprise.inject.spi.InterceptionType.*;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec = "cdi", version = "20091018")
@Resource(source = "javax.enterprise.inject.spi.Extension", destination = "WEB-INF/classes/META-INF/services/javax.enterprise.inject.spi.Extension")
@IntegrationTest
@BeansXml("beans.xml")
public class CustomInterceptorTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "9.5", id = "fa")
   // WELD-238
   public void testCustomPostConstructInterceptor()
   {
      assert !getCurrentManager().resolveInterceptors(POST_CONSTRUCT, new SecureLiteral(), new TransactionalLiteral()).isEmpty();
      assert AfterBeanDiscoveryObserver.POST_CONSTRUCT_INTERCEPTOR.isGetInterceptorBindingsCalled();
      assert AfterBeanDiscoveryObserver.POST_CONSTRUCT_INTERCEPTOR.isInterceptsCalled();
   }
   
   @Test
   @SpecAssertion(section = "9.5", id = "fb")
   // WELD-238
   public void testCustomPreDestroyInterceptor()
   {
      assert !getCurrentManager().resolveInterceptors(PRE_DESTROY, new SecureLiteral(), new TransactionalLiteral()).isEmpty();
      assert AfterBeanDiscoveryObserver.PRE_DESTROY_INTERCEPTOR.isGetInterceptorBindingsCalled();
      assert AfterBeanDiscoveryObserver.PRE_DESTROY_INTERCEPTOR.isInterceptsCalled();
   }
   
   @Test
   @SpecAssertion(section = "9.5", id = "fc")
   // WELD-238
   public void testCustomPostActivateInterceptor()
   {
      assert !getCurrentManager().resolveInterceptors(POST_ACTIVATE, new SecureLiteral(), new TransactionalLiteral()).isEmpty();
      assert AfterBeanDiscoveryObserver.POST_ACTIVATE_INTERCEPTOR.isGetInterceptorBindingsCalled();
      assert AfterBeanDiscoveryObserver.POST_ACTIVATE_INTERCEPTOR.isInterceptsCalled();
   }
   
   @Test
   @SpecAssertion(section = "9.5", id = "fd")
   // WELD-238
   public void testCustomPrePassivateInterceptor()
   {
      assert !getCurrentManager().resolveInterceptors(PRE_PASSIVATE, new SecureLiteral(), new TransactionalLiteral()).isEmpty();
      assert AfterBeanDiscoveryObserver.PRE_PASSIVATE_INTERCEPTOR.isGetInterceptorBindingsCalled();
      assert AfterBeanDiscoveryObserver.PRE_PASSIVATE_INTERCEPTOR.isInterceptsCalled();
   }
   
   @Test
   @SpecAssertion(section = "9.5", id = "fe")
   // WELD-238
   public void testCustomAroundInvokeInterceptor()
   {
      assert !getCurrentManager().resolveInterceptors(AROUND_INVOKE, new SecureLiteral(), new TransactionalLiteral()).isEmpty();
      assert AfterBeanDiscoveryObserver.AROUND_INVOKE_INTERCEPTOR.isGetInterceptorBindingsCalled();
      assert AfterBeanDiscoveryObserver.AROUND_INVOKE_INTERCEPTOR.isInterceptsCalled();
   }
   
   @Test
   @SpecAssertion(section = "9.5", id = "ff")
   // WELD-238
   public void testCustomAroundTimeoutInterceptor()
   {
      assert !getCurrentManager().resolveInterceptors(AROUND_TIMEOUT, new SecureLiteral(), new TransactionalLiteral()).isEmpty();
      assert AfterBeanDiscoveryObserver.AROUND_TIMEOUT_INTERCEPTOR.isGetInterceptorBindingsCalled();
      assert AfterBeanDiscoveryObserver.AROUND_TIMEOUT_INTERCEPTOR.isInterceptsCalled();
   }
}
