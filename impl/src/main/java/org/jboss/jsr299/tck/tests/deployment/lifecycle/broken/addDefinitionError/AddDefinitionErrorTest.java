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
package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.addDefinitionError;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentFailure;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.testng.annotations.Test;

/**
 * Tests that any definition error added by an observer of the AfterBeanDiscovery
 * event results in a definition error.
 * 
 * @author David Allen
 * @author Dan Allen
 */
@Artifact
@Resources({
   @Resource(source="javax.enterprise.inject.spi.Extension", destination="WEB-INF/classes/META-INF/services/javax.enterprise.inject.spi.Extension")
})
// Must be an integration test as it needs a resource copied to a folder
@IntegrationTest
@ExpectedDeploymentException(DeploymentFailure.class)
@SpecVersion(spec="cdi", version="20091101")
public class AddDefinitionErrorTest extends AbstractJSR299Test
{
   @Test(groups={"jboss-as-broken", "rewrite"})
   // WBRI-312
   @SpecAssertions({
      @SpecAssertion(section = "11.5.2", id = "ca"),
      @SpecAssertion(section = "12.2", id = "c")
   })
   public void testObserverDefinitionErrorTreatedAsDefinitionError()
   {
      assert false;
   }
   
   // FIXME need to communicate state of container at the time of failure
   //   @Override
   //   @AfterClass(alwaysRun = true, groups = "scaffold")
   //   public void afterClass() throws Exception
   //   {
   //      super.afterClass();
   //      assert BeanDiscoveryObserver.getInvocationCount() == 2 : "All AfterBeanDiscovery observer methods should have been called";
   //   }
}
