package org.jboss.jsr299.tck.tests.event.fires;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;

@RequestScoped class Housekeeping
{
   private Set<Item> itemsTainted = new HashSet<Item>();
   
   private Set<Item> itemsMissing = new HashSet<Item>();
   
   private List<Item> itemActivity = new ArrayList<Item>();
   
   public void onItemRemoved(@Observes @Lifted Item item)
   {
      itemsMissing.add(item);
      itemsTainted.remove(item);
   }
   
   public void onItemRestored(@Observes @Restored Item item)
   {
      itemsMissing.remove(item);
      itemsTainted.add(item);
   }
   
   public void onItemActivity(@Observes @Any Item item)
   {
      itemActivity.add(item);
   }
   
   public Set<Item> getItemsTainted()
   {
      return itemsTainted;
   }
   
   public Set<Item> getItemsMissing()
   {
      return itemsMissing;
   }
   
   public List<Item> getItemActivity()
   {
      return itemActivity;
   }
   
   public void minibarStocked(@Observes @Any MiniBar minibar)
   {
      reset();
   }
   
   public void reset()
   {
      itemActivity.clear();
      itemsMissing.clear();
      itemsTainted.clear();
   }
}
