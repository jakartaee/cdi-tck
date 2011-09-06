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
package org.jboss.jsr299.tck.tests.context.passivating.broken.dependentScopedProducerMethodReturnsNonSerializableObjectForInjectionIntoStatefulSessionBean;

import javax.enterprise.inject.IllegalProductException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.EnterpriseArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec="cdi", version="20091101")
public class EnterpriseBeanWithIllegalDependencyTest extends AbstractJSR299Test
{
    
    @Deployment
    public static EnterpriseArchive createTestArchive() 
	{
        return new EnterpriseArchiveBuilder()
            .withTestClassPackage(EnterpriseBeanWithIllegalDependencyTest.class)
            .build();
    }
    
   @Test(groups = { "contexts", "passivation", "integration"}, expectedExceptions={IllegalProductException.class})
   @SpecAssertion(section = "6.6.4", id = "fab")
   public void test()
   {
      // try
      // {
         getInstanceByType(MaarianHaminaLocal_Broken.class).ping();
      // }
      // catch (Throwable t) 
      // {
       //  assert isThrowablePresent(IllegalProductException.class, t);
       //  return;
      // }
      // assert false;
   }
}
