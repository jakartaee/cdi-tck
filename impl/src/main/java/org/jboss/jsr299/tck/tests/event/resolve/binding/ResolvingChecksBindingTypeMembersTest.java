package org.jboss.jsr299.tck.tests.event.resolve.binding;

import javax.enterprise.event.Observes;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class ResolvingChecksBindingTypeMembersTest extends AbstractJSR299Test
{
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
   }
}
