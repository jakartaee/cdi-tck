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
package org.jboss.jsr299.tck.tests.implementation.enterprise.remove;

import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.EnterpriseArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author Nicklas Karlsson
 */
@SpecVersion(spec="cdi", version="20091101")
public class EnterpriseBeanRemoveMethodTest extends AbstractJSR299Test
{
   
    @Deployment
    public static EnterpriseArchive createTestArchive() 
	{
        return new EnterpriseArchiveBuilder()
            .withTestClassPackage(EnterpriseBeanRemoveMethodTest.class)
            .build();
    }
    
   @Test(groups = { "enterpriseBeans", "removeMethod", "lifecycle" })
   @SpecAssertion(section = "3.2.1", id = "a")
   public void testApplicationMayCallAnyRemoveMethodOnDependentScopedSessionEnterpriseBeans() throws Exception
   {
      Bean<?> bean = getCurrentManager().getBeans(StateKeeper.class).iterator().next();
      StateKeeper stateKeeper = (StateKeeper)
      getCurrentManager().getReference(bean,StateKeeper.class, getCurrentManager().createCreationalContext(bean));
      stateKeeper.setRemoveCalled(false);      
      
      DependentSessionInterface sessionBean = getInstanceByType(DependentSessionInterface.class);
      sessionBean.remove();
      assert stateKeeper.isRemoveCalled();
   }

   @Test(groups = { "enterpriseBeans", "removeMethod", "lifecycle" })
   @SpecAssertion(section = "3.2.1", id = "da")
   public void testApplicationMayCallRemoveMethodOnDependentScopedSessionEnterpriseBeansButNoParametersArePassed() throws Exception
   {
      DependentSessionInterface sessionBean = getInstanceByType(DependentSessionInterface.class);
      sessionBean.anotherRemoveWithParameters("required", null);
      StateKeeper stateKeeper = getInstanceByType(StateKeeper.class);
      assert stateKeeper.isRemoveCalled();
   }

   @Test(groups = { "enterpriseBeans", "removeMethod", "lifecycle" }, expectedExceptions = UnsupportedOperationException.class)
   @SpecAssertion(section = "3.2.1", id = "b")
   public void testApplicationCannotCallRemoveMethodOnNonDependentScopedSessionEnterpriseBean()
   {
      SessionScopedSessionInterface sessionBean = getInstanceByType(SessionScopedSessionInterface.class);
      sessionBean.remove();
      assert false : "Should never reach this assertion";
   }

}
