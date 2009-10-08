package org.jboss.jsr299.tck.tests.event;

import java.util.ArrayList;
import java.util.Set;

import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.spi.ObserverMethod;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

/**
 * Event bus tests
 * 
 * @author Nicklas Karlsson
 * @author David Allen
 */
@Artifact
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="PFD2")
public class EventTest extends AbstractJSR299Test
{
   @Test(groups = { "events" })
   @SpecAssertions( {
      @SpecAssertion(section = "10.4.3", id = "a"),
      @SpecAssertion(section = "5.6.8", id = "c"),
      @SpecAssertion(section = "2.3.5", id = "ca"),
      @SpecAssertion(section = "3.10", id = "a")
   })
   public void testObserverMethodReceivesInjectionsOnNonObservesParameters()
   {
      getCurrentManager().fireEvent("validate injected parameters");
   }

   /**
    * FIXME the spec doesn't follow this exactly because technically it isn't supposed to use
    * the bean resolution alorithm to obtain the instance, which it does.
    */
   @Test(groups = { "events" })
   @SpecAssertions({
      @SpecAssertion(section = "10.4", id = "c"),
      @SpecAssertion(section = "5.6.8", id = "a")
   })
   public void testStaticObserverMethodInvoked()
   {
      try
      {
         getCurrentConfiguration().getContexts().setInactive(getCurrentConfiguration().getContexts().getRequestContext());
         StaticObserver.reset();
         getCurrentManager().fireEvent(new Delivery());
         assert StaticObserver.isDeliveryReceived();
         StaticObserver.reset();
      }
      finally
      {
         getCurrentConfiguration().getContexts().setActive(getCurrentConfiguration().getContexts().getRequestContext());
      }
   }

   @Test(groups = { "events" })
   @SpecAssertions({
      //@SpecAssertion(section = "4.3.2", id = "d"),
      @SpecAssertion(section="4.3", id="cb"),
      @SpecAssertion(section = "5.6.8", id = "baa")
   })
   public void testObserverCalledOnMostSpecializedInstance()
   {
      Shop.deliveryObservedBy = null;
      getCurrentManager().fireEvent(new Delivery());
      assert Shop.deliveryObservedBy.equals(FarmShop.class.getName());
   }

   @Test(groups = { "events" }, expectedExceptions = IllegalArgumentException.class)
   @SpecAssertion(section = "11.3.10", id = "c")
   public <T> void testEventObjectContainsTypeVariablesWhenResolvingFails()
   {
      eventObjectContainsTypeVariables(new ArrayList<T>());
   }

   private <E> void eventObjectContainsTypeVariables(ArrayList<E> eventToFire)
   {
      getCurrentManager().resolveObserverMethods(eventToFire);
   }
   
   @Test(groups = { "events" })
   @SpecAssertions( {
      @SpecAssertion(section = "10.2.3", id = "b"),
      @SpecAssertion(section = "10.2.3", id = "c")
   } )
   public void testObserverMethodNotifiedWhenBindingsMatch()
   {
      getCurrentManager().fireEvent(new MultiBindingEvent(), new RoleBinding("Admin"), new TameAnnotationLiteral());
      assert BullTerrier.isMultiBindingEventObserved();
      assert BullTerrier.isSingleBindingEventObserved();
   }

   /**
    * By default, Java implementation reuse is assumed. In this case, the
    * producer, disposal and observer methods of the first bean are not
    * inherited by the second bean.
    * 
    * @throws Exception
    */
   @Test(groups = { "events", "inheritance" })
   @SpecAssertion(section = "4.2", id = "dc")
   public void testNonStaticObserverMethodNotInherited()
   {
      Egg egg = new Egg();
      Set<ObserverMethod<?, Egg>> observers = getCurrentManager().resolveObserverMethods(egg);
      assert observers.size() == 1;

      // Notify the observer so we can confirm that it
      // is a method only on Farmer, and not LazyFarmer
      observers.iterator().next().notify(egg);
      assert egg.getClassesVisited().size() == 1;
      assert egg.getClassesVisited().iterator().next().equals(Farmer.class);
   }
   
   @Test(groups = { "events", "inheritance" })
   @SpecAssertions({
      @SpecAssertion(section = "4.2", id = "di"),
      @SpecAssertion(section = "11.1.3", id = "f")
   })
   public void testNonStaticObserverMethodNotIndirectlyInherited()
   {
      StockPrice price = new StockPrice();
      Set<ObserverMethod<?, StockPrice>> observers = getCurrentManager().resolveObserverMethods(price);
      assert observers.size() == 1;

      // Notify the observer so we can confirm that it
      // is a method only on StockWatcher, and not IntermediateStockWatcher
      // or IndirectStockWatcher
      observers.iterator().next().notify(price);
      assert price.getClassesVisited().size() == 1;
      assert price.getClassesVisited().iterator().next().equals(StockWatcher.class);
   }

   @Test(groups = { "events" })
   @SpecAssertion(section = "11.1.3", id = "e")
   public void testGetTransactionPhaseOnObserverMethod()
   {
      assert getCurrentManager().resolveObserverMethods(new StockPrice()).iterator().next().getTransactionPhase().equals(TransactionPhase.IN_PROGRESS);
      assert getCurrentManager().resolveObserverMethods(new DisobedientDog()).iterator().next().getTransactionPhase().equals(TransactionPhase.BEFORE_COMPLETION);
      assert getCurrentManager().resolveObserverMethods(new ShowDog()).iterator().next().getTransactionPhase().equals(TransactionPhase.AFTER_COMPLETION);
      assert getCurrentManager().resolveObserverMethods(new SmallDog()).iterator().next().getTransactionPhase().equals(TransactionPhase.AFTER_FAILURE);
      assert getCurrentManager().resolveObserverMethods(new LargeDog()).iterator().next().getTransactionPhase().equals(TransactionPhase.AFTER_SUCCESS);
   }

   @Test(groups = { "events" })
   @SpecAssertion(section = "11.1.3", id = "ga")
   public void testInstanceOfBeanForEveryEnabledObserverMethod()
   {
      assert !getCurrentManager().resolveObserverMethods(new StockPrice()).isEmpty();
      assert !getCurrentManager().resolveObserverMethods(new DisobedientDog()).isEmpty();
      assert !getCurrentManager().resolveObserverMethods(new ShowDog()).isEmpty();
      assert !getCurrentManager().resolveObserverMethods(new SmallDog()).isEmpty();
      assert !getCurrentManager().resolveObserverMethods(new LargeDog()).isEmpty();
   }
}
