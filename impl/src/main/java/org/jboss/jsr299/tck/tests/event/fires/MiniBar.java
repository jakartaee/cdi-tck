package org.jboss.jsr299.tck.tests.event.fires;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

class MiniBar
{
   private Set<Item> items = new HashSet<Item>();
   
   @Inject @Any Event<MiniBar> miniBarEvent;
   
   @Inject @Lifted Event<Item> itemLiftedEvent;
   
   @Inject @Any Event<Item> itemEvent;
   
   Event<Item> getItemEvent()
   {
      return itemEvent;
   }
   
   public Set<Item> getItems()
   {
      return items;
   }
   
   public Item getItemByName(String name)
   {
      for (Item item : items)
      {
         if (item.getName().equals(name))
         {
            return item;
         }
      }
      
      return null;
   }
   
   public Item liftItemByName(String name)
   {
      Item item = getItemByName(name);
      if (item != null)
      {
         liftItem(item);
      }
      return item;
   }
   
   public void liftItem(Item item)
   {
      if (!items.contains(item))
      {
         throw new IllegalArgumentException("No such item");
      }
      
      itemLiftedEvent.fire(item);
      items.remove(item);
   }
   
   public void restoreItem(Item item)
   {
      if (items.contains(item))
      {
         throw new IllegalArgumentException("Item already restored");
      }
      
      itemEvent.select(new AnnotationLiteral<Restored>() {}).fire(item);
   }
   
   public void stock()
   {
      stockNoNotify();
      miniBarEvent.fire(this);
   }
   
   public void stockNoNotify()
   {
      items.add(new Item("Chocolate", 5.00));
      items.add(new Item("16 oz Water", 1.00));
      items.add(new Item("Disposable Camera", 10.00));
   }
}
