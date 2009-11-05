package org.jboss.jsr299.tck.tests.event.resolve.nonbinding;

import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.ObserverMethod;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091101")
public class NonBindingTypesWhenResolvingTest extends AbstractJSR299Test
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
   @SpecAssertion(section = "11.3.11", id = "e")
   public void testNonBindingTypeAnnotationWhenResolvingFails()
   {
      Set<ObserverMethod<? super AnEventType>> resolvedObservers = getCurrentManager().resolveObserverMethods(new AnEventType(), new AnimalStereotypeAnnotationLiteral());
   }
}
