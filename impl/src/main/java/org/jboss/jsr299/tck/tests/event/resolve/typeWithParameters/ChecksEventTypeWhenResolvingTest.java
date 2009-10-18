package org.jboss.jsr299.tck.tests.event.resolve.typeWithParameters;

import javax.enterprise.event.Observes;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091018")
public class ChecksEventTypeWhenResolvingTest extends AbstractJSR299Test
{
   public static class AnEventType
   {
   }

   public static class AnObserver
   {
      public boolean wasNotified = false;

      public void observes(@Observes AnEventType event)
      {
         wasNotified = true;
      }
   }

   @Test(groups = { "events" })
   @SpecAssertion(section = "10.4.1", id = "a")
   public void testResolvingChecksEventType()
   {
      assert !getCurrentManager().resolveObserverMethods(new AnEventType()).isEmpty();
      assert getCurrentManager().resolveObserverMethods(new UnusedEventType("name")).isEmpty();
   }
}
