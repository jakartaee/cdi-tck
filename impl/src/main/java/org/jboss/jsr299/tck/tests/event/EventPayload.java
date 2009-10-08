package org.jboss.jsr299.tck.tests.event;

import java.util.ArrayList;
import java.util.List;

class EventPayload
{
   private List<Class<?>> classesVisited = new ArrayList<Class<?>>();
   
   public List<Class<?>> getClassesVisited()
   {
      return classesVisited;
   }
   
   public void recordVisit(Object o)
   {
      classesVisited.add(o.getClass());
   }
}
