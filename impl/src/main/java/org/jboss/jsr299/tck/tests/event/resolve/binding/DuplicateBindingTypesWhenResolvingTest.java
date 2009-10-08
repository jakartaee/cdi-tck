package org.jboss.jsr299.tck.tests.event.resolve.binding;

import javax.enterprise.event.Observes;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class DuplicateBindingTypesWhenResolvingTest extends AbstractJSR299Test
{
   public static class AnEventType
   {
   }

   public static class AnObserver
   {
      public boolean wasNotified = false;

      public void observer(@Observes AnEventType event)
      {
         wasNotified = true;
      }
   }

   @Test(groups = { "events" }, expectedExceptions = { IllegalArgumentException.class })
   @SpecAssertion(section = "11.3.10", id = "d")
   public void testDuplicateBindingTypesWhenResolvingFails()
   {
      getCurrentManager().resolveObserverMethods(new AnEventType(), new BindingTypeABinding(), new BindingTypeABinding());
   }
}
