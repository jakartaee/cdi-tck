package org.jboss.jsr299.tck.tests.event.observer;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.event.Reception;
import javax.enterprise.inject.spi.ObserverMethod;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.DefaultLiteral;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091018")
public class ObserverTest extends AbstractJSR299Test
{
   @Test(groups = { "events" })
   @SpecAssertions( {
      @SpecAssertion(section = "10.2", id = "h"),
      @SpecAssertion(section = "10.5", id = "aa")
   })
   public void testObserverNotifiedWhenEventTypeAndAllBindingsMatch()
   {
      Annotation roleBinding = new RoleBinding("Admin");

      // Fire an event that will be delivered to the two above observers
      AnEventType anEvent = new AnEventType();
      getCurrentManager().fireEvent(anEvent, roleBinding);
      
      assert AnObserver.wasNotified;
      assert AnotherObserver.wasNotified;
      AnObserver.wasNotified = false;
      AnotherObserver.wasNotified = false;
      
      // Fire an event that will be delivered to only one
      getCurrentManager().fireEvent(anEvent);
      assert AnObserver.wasNotified;
      assert !AnotherObserver.wasNotified;
      AnObserver.wasNotified = false;
      AnotherObserver.wasNotified = false;
      
      // Also make sure the binding value is considered
      getCurrentManager().fireEvent(anEvent, new RoleBinding("user"));
      assert AnObserver.wasNotified;
      assert !AnotherObserver.wasNotified;
   }
   @Test(groups = { "events" })
   @SpecAssertion(section = "11.1.3", id = "b")
   public void testGetBeanOnObserverMethod()
   {
      Set<ObserverMethod<?, StockPrice>> observers = getCurrentManager().resolveObserverMethods(new StockPrice());
      assert observers.size() == 1;
      ObserverMethod<?, StockPrice> observerMethod = observers.iterator().next();
      assert observerMethod.getBean().getBeanClass().equals(StockWatcher.class);
   }

   @Test(groups = { "events" })
   @SpecAssertion(section = "11.1.3", id = "c")
   public void testGetObservedTypeOnObserverMethod()
   {
      Set<ObserverMethod<?, StockPrice>> observers = getCurrentManager().resolveObserverMethods(new StockPrice());
      assert observers.size() == 1;
      ObserverMethod<?, ?> observerMethod = observers.iterator().next();
      assert observerMethod.getObservedType().equals(StockPrice.class);
   }

   @Test(groups = { "events" })
   @SpecAssertion(section = "11.1.3", id = "c")
   public void testGetObservedBindingsOnObserverMethod()
   {
      Set<ObserverMethod<?, StockPrice>> observers = getCurrentManager().resolveObserverMethods(new StockPrice());
      assert observers.size() == 1;
      ObserverMethod<?, ?> observerMethod = observers.iterator().next();
      assert observerMethod.getObservedQualifiers().size() == 1;
      assert observerMethod.getObservedQualifiers().contains(new DefaultLiteral());
   }

   @Test(groups = { "events" })
   @SpecAssertion(section = "11.1.3", id = "d")
   public void testGetNotifyOnObserverMethod()
   {
      Set<ObserverMethod<?, StockPrice>> observers = getCurrentManager().resolveObserverMethods(new StockPrice());
      assert observers.size() == 1;
      assert observers.iterator().next().getReception().equals(Reception.ALWAYS);

      Set<ObserverMethod<?, ConditionalEvent>> conditionalObservers = getCurrentManager().resolveObserverMethods(new ConditionalEvent());
      assert !conditionalObservers.isEmpty();
      assert conditionalObservers.iterator().next().getReception().equals(Reception.IF_EXISTS);
   }

}
