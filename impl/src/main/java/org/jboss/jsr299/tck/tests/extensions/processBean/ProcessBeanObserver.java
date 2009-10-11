package org.jboss.jsr299.tck.tests.extensions.processBean;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.ProcessManagedBean;
import javax.enterprise.inject.spi.ProcessProducerField;
import javax.enterprise.inject.spi.ProcessProducerMethod;
import javax.enterprise.inject.spi.ProcessSessionBean;

public class ProcessBeanObserver implements Extension
{
   private static ProcessManagedBean<Cat> catProcessManagedBean;
   private static int catProcessBeanCount;
   
   private static ProcessProducerMethod<Cowshed, Cow> cowProcessProducerMethod;
   private static int cowShedProcessBeanCount;
   
   private static ProcessSessionBean<Elephant> elephantProcessSessionBean;
   private static int elephantProcessBeanCount;
   private static ProcessProducerField<ChickenHutch, Chicken> chickenProcessProducerField;
   private static int chickenHutchProcessBeanCount;

   public void observeCatManagedBean(@Observes ProcessManagedBean<Cat> event)
   {
      ProcessBeanObserver.catProcessManagedBean = event;
      ProcessBeanObserver.catProcessBeanCount++;
   }
   
   public void observeCatBean(@Observes ProcessBean<Cat> event)
   {
      ProcessBeanObserver.catProcessBeanCount++;
   }
   
   public void observeElephantSessionBean(@Observes ProcessSessionBean<Elephant> event)
   {
      ProcessBeanObserver.elephantProcessSessionBean = event;
      ProcessBeanObserver.elephantProcessBeanCount++;
   }
   
   public void observeElephantBean(@Observes ProcessBean<Elephant> event)
   {
      ProcessBeanObserver.elephantProcessBeanCount++;
   }
   
   public void observeCowProcessProducerMethod(@Observes ProcessProducerMethod<Cowshed, Cow> event)
   {
      ProcessBeanObserver.cowProcessProducerMethod = event;
   }
   
   public void observeCowProccesBean(@Observes ProcessBean<Cowshed> event)
   {
      ProcessBeanObserver.cowShedProcessBeanCount++;
   }
   
   public void observeChickenProcessProducerField(@Observes ProcessProducerField<ChickenHutch, Chicken> event)
   {
      ProcessBeanObserver.chickenProcessProducerField = event;
   }
   
   public void observeChickenProccesBean(@Observes ProcessBean<ChickenHutch> event)
   {
      ProcessBeanObserver.chickenHutchProcessBeanCount++;
   }
   
   public static ProcessManagedBean<Cat> getCatProcessManagedBean()
   {
      return catProcessManagedBean;
   }
   
   public static ProcessProducerMethod<Cowshed, Cow> getCowProcessProducerMethod()
   {
      return cowProcessProducerMethod;
   }
   
   public static ProcessSessionBean<Elephant> getElephantProcessSessionBean()
   {
      return elephantProcessSessionBean;
   }
   
   public static int getCatProcessBeanCount()
   {
      return catProcessBeanCount;
   }
   
   public static int getCowShedProcessBeanCount()
   {
      return cowShedProcessBeanCount;
   }
   
   public static int getElephantProcessBeanCount()
   {
      return elephantProcessBeanCount;
   }
   
   public static int getChickenHutchProcessBeanCount()
   {
      return chickenHutchProcessBeanCount;
   }
   
   public static ProcessProducerField<ChickenHutch, Chicken> getChickenProcessProducerField()
   {
      return chickenProcessProducerField;
   }
   
}
