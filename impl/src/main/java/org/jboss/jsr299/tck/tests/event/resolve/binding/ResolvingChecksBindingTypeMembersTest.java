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
package org.jboss.jsr299.tck.tests.event.resolve.binding;

import javax.enterprise.event.Observes;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec="cdi", version="20091101")
public class ResolvingChecksBindingTypeMembersTest extends AbstractJSR299Test
{
    
    @Deployment
    public static WebArchive createTestArchive() 
	{
        return new WebArchiveBuilder()
            .withTestClassPackage(ResolvingChecksBindingTypeMembersTest.class)
            .build();
    }
    
   public static class AnEventType
   {
   }

   public static class AnObserver
   {
      public boolean wasNotified = false;

      public void observer(@Observes @BindingTypeC("first-observer") AnEventType event)
      {
         wasNotified = true;
      }
   }

   public static class AnotherObserver
   {
      public boolean wasNotified = false;

      public void observer(@Observes @BindingTypeC("second-observer") AnEventType event)
      {
         wasNotified = true;
      }
   }

   @Test(groups = { "events" })
   @SpecAssertions({
      @SpecAssertion(section = "10.4.1", id = "a"),
      @SpecAssertion(section = "10.2.2", id = "a")
   })
   public void testResolvingChecksBindingTypeMembers()
   {
      assert getCurrentManager().resolveObserverMethods(new AnEventType(), new BindingTypeCBinding("first-observer")).size() == 1;
      assert getCurrentManager().resolveObserverMethods(new AnEventType(), new BindingTypeCBinding("second-observer")).size() == 1;
      assert getCurrentManager().resolveObserverMethods(new AnEventType(), new BindingTypeCBinding("third-observer")).size() == 0;

   }
}
