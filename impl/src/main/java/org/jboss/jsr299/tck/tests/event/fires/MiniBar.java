/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
