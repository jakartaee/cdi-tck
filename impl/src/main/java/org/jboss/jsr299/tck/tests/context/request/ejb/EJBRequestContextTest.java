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
package org.jboss.jsr299.tck.tests.context.request.ejb;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

/**
 * EJB and related tests with the built-in request context.
 * 
 * @author David Allen
 */
@Artifact
@IntegrationTest
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="20091101")
public class EJBRequestContextTest extends AbstractJSR299Test
{
   /**
    * The request scope is active during any remote method invocation of any EJB
    * bean, during any call to an EJB timeout method and during message delivery
    * to any EJB message driven bean.
    */
   // WELD-12
   @Test(groups = { "jboss-as-broken", "contexts", "ejb3.1", "integration" })
   @SpecAssertion(section = "6.7.1", id = "gc")
   public void testRequestScopeActiveDuringCallToEjbTimeoutMethod() throws Exception
   {
      FMS flightManagementSystem = getInstanceByType(FMS.class);
      flightManagementSystem.climb();
      Thread.sleep(250);
      assert flightManagementSystem.isRequestScopeActive();
   }

   /**
    * The request context is destroyed after the remote method invocation,
    * timeout or message delivery completes.
    */
   // WELD-12
   @Test(groups = { "jboss-as-broken", "contexts", "ejb3.1", "integration" })
   @SpecAssertion(section = "6.7.1", id = "hc")
   public void testRequestScopeDestroyedAfterCallToEjbTimeoutMethod() throws Exception
   {
      FMS flightManagementSystem = getInstanceByType(FMS.class);
      flightManagementSystem.climb();
      flightManagementSystem.descend();
      Thread.sleep(250);
      assert !flightManagementSystem.isSameBean();
      assert SimpleRequestBean.isBeanDestroyed();
   }

}
