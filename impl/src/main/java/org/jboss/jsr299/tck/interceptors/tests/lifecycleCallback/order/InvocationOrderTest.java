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
package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback.order;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec = "int", version = "3.1.PFD")
public class InvocationOrderTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.1", id = "c"),
      @SpecAssertion(section = "5.1", id = "d"),
      @SpecAssertion(section = "5.1", id = "e"),
      @SpecAssertion(section = "5.1", id = "f")
   })
   public void testInvocationOrder() {
      getInstanceByType(LakeCargoShip.class);
      int s = LakeCargoShip.getSequence();
      assert LakeCargoShip.getSequence() == 7;
   }
}
