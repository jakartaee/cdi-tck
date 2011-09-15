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

package org.jboss.jsr299.tck.tests.extensions.annotated;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for the extensions provided by the ProcessAnnotatedType events.
 * 
 * @author David Allen
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec="cdi", version="20091101")
public class ProcessAnnotatedTypeTest extends AbstractJSR299Test
{
    
    @Deployment
    public static WebArchive createTestArchive() 
	{
        return new WebArchiveBuilder()
            .withTestClassPackage(ProcessAnnotatedTypeTest.class)
            .withExtension("javax.enterprise.inject.spi.Extension")
            .build();
    }
    
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.5.5", id = "a"),
      @SpecAssertion(section = "12.3", id = "ba")
   })
   public void testProcessAnnotatedTypeEventsSent()
   {
      // Randomly test some of the classes and interfaces that should have
      // been discovered and sent via the event
      assert ProcessAnnotatedTypeObserver.getAnnotatedclasses().contains(AbstractC.class);
      assert ProcessAnnotatedTypeObserver.getAnnotatedclasses().contains(ClassD.class);
      assert ProcessAnnotatedTypeObserver.getAnnotatedclasses().contains(Dog.class);
      assert ProcessAnnotatedTypeObserver.getAnnotatedclasses().contains(InterfaceA.class);
      //assert !ProcessAnnotatedTypeObserver.getAnnotatedclasses().contains(Tame.class);
   }
   
   @Test
   @SpecAssertion(section = "11.5.5", id = "ba")
   public void testGetAnnotatedType()
   {
      assert ProcessAnnotatedTypeObserver.getDogAnnotatedType().getBaseType().equals(Dog.class);
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.5.5", id = "bb"),
      @SpecAssertion(section = "11.5.5", id = "ca")
   })
   public void testSetAnnotatedType()
   {
      assert TestAnnotatedType.isGetConstructorsUsed();
      assert TestAnnotatedType.isGetFieldsUsed();
      assert TestAnnotatedType.isGetMethodsUsed();
   }
   
   @Test
   @SpecAssertion(section = "11.5.5", id = "bc")
   public void testVeto()
   {
      assert getCurrentManager().getBeans(VetoedBean.class).isEmpty();
   }
}
