package org.jboss.jsr299.tck.tests.event.eventTypes;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

/**
 * 
 * @author Dan Allen
 */
@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class EventTypesTest extends AbstractJSR299Test
{
   @Test(groups = "event")
   @SpecAssertions({
      @SpecAssertion(section = "10.1", id = "aa"),
      @SpecAssertion(section = "10.1", id = "j")
   })
   public void testEventTypeIsConcreteTypeWithNoTypeVariables()
   {
      Listener listener = getInstanceByType(Listener.class);
      listener.reset();
      // typical concrete type
      Song s = new Song();
      getInstanceByType(TuneSelect.class).songPlaying(s);
      assert listener.getObjectsFired().size() == 1;
      assert listener.getObjectsFired().get(0) == s;
      getCurrentManager().fireEvent(s);
      assert listener.getObjectsFired().size() == 2;
      assert listener.getObjectsFired().get(1) == s;
      // anonymous instance
      Broadcast b = new Broadcast() {};
      getInstanceByType(TuneSelect.class).broadcastPlaying(b);
      assert listener.getObjectsFired().size() == 3;
      assert listener.getObjectsFired().get(2) == b;
      // boxed primitive
      getCurrentManager().fireEvent(1);
      assert listener.getObjectsFired().size() == 4;
      assert listener.getObjectsFired().get(3).equals(1);
   }
   
   @Test(groups = { "events" })
   @SpecAssertion(section = "10.1", id = "c")
   public void testEventTypeIncludesAllSuperclassesAndInterfacesOfEventObject()
   {
      // we have to use a dependent-scoped observer here because we it is observing the Object event type
      // and a request-scoped object would get called outside of when the request scope is active in this situation
      EventTypeFamilyObserver observer = getInstanceByType(EventTypeFamilyObserver.class);
      getCurrentManager().fireEvent(new ComplexEvent());
      assert observer.getGeneralEventQuantity() == 1;
      assert observer.getAbstractEventQuantity() == 1;
      assert observer.getComplexEventQuantity() == 1;
      assert observer.getObjectEventQuantity() == 1;
      assert observer.getTotalEventsObserved() == 4;
   }
}
