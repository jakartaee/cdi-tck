/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat, Inc. and/or its affiliates, and individual contributors
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

package org.jboss.jsr299.tck.tests.extensions.annotated.broken.processInjectionTargetThrowsException;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.testng.annotations.Test;

/**
 * Tests that an exception thrown by a ProcessAnnotatedType event observer
 * is treated as a deployment error
 * 
 *
 */
@Artifact
@ExpectedDeploymentException(DeploymentError.class)
@IntegrationTest
@SpecVersion(spec="cdi", version="PFD2")
public class ProcessInjectionTargetEventThrowsExceptionTest extends AbstractJSR299Test
{
   @Test(groups="ri-broken")
   @SpecAssertion(section = "11.5.6", id = "f")
   public void testProcessInjectionTargetEventThrowsExceptionNotOk()
   {
      assert false;
   }
}
