package org.jboss.jsr299.tck.tests.event.observer.abortProcessing;

import javax.enterprise.event.Observes;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class ObserverExceptionAbortsProcessingTest extends AbstractJSR299Test
{
   public static class AnEventType
   {
   }

   public static class AnObserverWithException
   {
      public static boolean wasNotified = false;
      public static final RuntimeException theException = new RuntimeException("RE1");

      public void observer(@Observes AnEventType event)
      {
         wasNotified = true;
         throw theException;
      }
   }

   public static class AnotherObserverWithException
   {
      public static boolean wasNotified = false;
      public static final RuntimeException theException = new RuntimeException("RE2");

      public void observer(@Observes AnEventType event)
      {
         wasNotified = true;
         throw theException;
      }
   }

   @Test(groups = { "events" })
   @SpecAssertion(section = "10.5", id = "cb")
   public void testObserverThrowsExceptionAbortsNotifications()
   {
      // Fire an event that will be delivered to the two above observers
      AnEventType anEvent = new AnEventType();
      boolean fireFailed = false;
      try
      {
         getCurrentManager().fireEvent(anEvent);
      }
      catch (Exception e)
      {
         if (e.equals(AnObserverWithException.theException))
         {
            fireFailed = true;
            assert AnObserverWithException.wasNotified;
            assert !AnotherObserverWithException.wasNotified;
         }
         else if (e.equals(AnotherObserverWithException.theException))
         {
            fireFailed = true;
            assert !AnObserverWithException.wasNotified;
            assert AnotherObserverWithException.wasNotified;
         }
      }
      assert fireFailed;
   }
}
