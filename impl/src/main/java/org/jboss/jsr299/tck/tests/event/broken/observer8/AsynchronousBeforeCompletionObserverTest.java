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
package org.jboss.jsr299.tck.tests.event.broken.observer8;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * Tests an asynchronous observer method that is also a
 * transactional observer for the before completion phase.
 * 
 * Spec version: 20090519
 * 
 * @author David Allen
 * @author Martin Kouba
 */
public class AsynchronousBeforeCompletionObserverTest extends AbstractJSR299Test
{
    
    @ShouldThrowException(Exception.class)
    @Deployment
    public static WebArchive createTestArchive() 
	{
        return new WebArchiveBuilder()
            .withTestClassPackage(AsynchronousBeforeCompletionObserverTest.class)
            .build();
    }
    
//   @Test(groups = { "events" })
//   @SpecAssertion(section = "10.5.6", id = "b")
// Asynchronous events are not specified now
   public void testAsynchronousObserverAsBeforeCompletionObserverFails()
   {
   }
}