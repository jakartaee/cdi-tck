package org.jboss.jsr299.tck.tests.event.fires;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;

@RequestScoped class Billing
{
   private boolean active = false;
   
   private double charge = 0;
   
   private double miniBarValue = 0d;
   
   private Set<Item> itemsPurchased = new HashSet<Item>();
   
   public void billForItem(@Observes @Lifted Item item)
   {
      if (itemsPurchased.add(item))
      {
         charge += item.getPrice();
      }
   }
   
   public double getCharge()
   {
      return charge;
   }
   
   public double getMiniBarValue()
   {
      return miniBarValue;
   }
   
   public boolean isActive()
   {
      return active;
   }
   
   public void activate(@Observes @Any MiniBar minibar)
   {
      active = true;
      miniBarValue = 0;
      for (Item item : minibar.getItems())
      {
         miniBarValue += item.getPrice();
      }
   }
   
   public void reset()
   {
      active = false;
      itemsPurchased.clear();
      charge = 0;
   }
}
