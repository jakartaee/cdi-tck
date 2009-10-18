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

package org.jboss.jsr299.tck.tests.event.observer.runtimeException;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

/**
 * Tests that an unchecked exception thrown from a non-transactional
 * observer is rethrown.
 * 
 * @author David Allen
 */
@Artifact
@SpecVersion(spec="cdi", version="20091018")
public class ObserverExceptionRethrownTest extends AbstractJSR299Test
{
   @Test(groups = { "events" }, expectedExceptions = { TeaCupPomeranian.OversizedException.class })
   @SpecAssertion(section = "10.5", id = "cc")
   public void testNonTransactionalObserverThrowsNonCheckedExceptionIsRethrown()
   {
      getCurrentManager().fireEvent("string event");
   }

}
