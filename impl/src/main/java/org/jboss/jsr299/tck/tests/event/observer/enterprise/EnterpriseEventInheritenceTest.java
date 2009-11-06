package org.jboss.jsr299.tck.tests.event.observer.enterprise;

import java.util.Set;

import javax.enterprise.inject.spi.ObserverMethod;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

/**
 * Tests for event inheritence with enterprise beans
 * 
 * @author Shane Bryzak
 */
@Artifact
@Packaging(PackagingType.EAR)
@IntegrationTest
@SpecVersion(spec="cdi", version="20091101")
public class EnterpriseEventInheritenceTest extends AbstractJSR299Test
{
   @Test(groups = { "events", "inheritance" })
   @SpecAssertion(section = "4.2", id = "df")
   public void testNonStaticObserverMethodNotInherited() throws Exception
   {
      Egg egg = new Egg();
      Set<ObserverMethod<? super Egg>> observers = getCurrentManager().resolveObserverMethods(egg);
      assert observers.size() == 1;

      // Reception the observer so we can confirm that it
      // is a method only on Farmer, and not LazyFarmer
      observers.iterator().next().notify(egg);
      assert egg.getClassesVisited().size() == 1;
      assert egg.getClassesVisited().iterator().next().equals(Farmer.class);
   }
   
   @Test(groups = { "events", "inheritance" })
   @SpecAssertion(section = "4.2", id = "dl")
   public void testNonStaticObserverMethodNotIndirectlyInherited() throws Exception
   {
      StockPrice stockPrice = new StockPrice();
      Set<ObserverMethod<? super StockPrice>> observers = getCurrentManager().resolveObserverMethods(stockPrice);
      assert observers.size() == 1;

      // Reception the observer so we can confirm that it
      // is a method only on StockWatcher, and not IndirectStockWatcher
      observers.iterator().next().notify(stockPrice);
      assert stockPrice.getClassesVisited().size() == 1;
      assert stockPrice.getClassesVisited().iterator().next().equals(StockWatcher.class);
   }
}
