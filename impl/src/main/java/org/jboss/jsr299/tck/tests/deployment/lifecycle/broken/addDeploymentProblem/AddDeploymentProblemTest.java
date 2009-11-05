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

package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.addDeploymentProblem;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

/**
 * Tests that a deployment error registered by an observer of the AfterDeploymentValidation
 * event results in a deployment error, but that all observers that observe this event
 * are still called.
 * 
 * @author David Allen
 * @author Dan Allen
 */
@Artifact
@ExpectedDeploymentException(DeploymentError.class)
@SpecVersion(spec="cdi", version="20091101")
public class AddDeploymentProblemTest extends AbstractJSR299Test
{
   @Test(groups={"rewrite", "jboss-as-broken"})
   // TODO Needs Extension stuff adding
   // WBRI-312
   @SpecAssertion(section = "11.5.3", id = "b")
   public void testObserverDeploymentProblemTreatedAsDeploymentError()
   {
      assert false;
   }

   // FIXME need to communicate state of container at the time of failure
   //   @Override
   //   @AfterClass(alwaysRun = true, groups = "scaffold")
   //   public void afterClass() throws Exception
   //   {
   //      super.afterClass();
   //      assert BeanDiscoveryObserver.getInvocationCount() == 2 : "All AfterDeploymentValidation observer methods should have been called";
   //   }

}
