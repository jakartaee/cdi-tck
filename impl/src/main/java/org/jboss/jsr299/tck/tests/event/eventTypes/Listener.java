package org.jboss.jsr299.tck.tests.event.eventTypes;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;

@RequestScoped class Listener
{
   List<Object> objectsFired = new ArrayList<Object>();
   
   public void registerNumberFired(@Observes @Any Integer i)
   {
      objectsFired.add(i);
   }
   
   public void registerSongFired(@Observes @Any Song s)
   {
      objectsFired.add(s);
   }
   
   public void registerBroadcastFired(@Observes @Any Broadcast b)
   {
      objectsFired.add(b);
   }
   
   public List<Object> getObjectsFired()
   {
      return objectsFired;
   }
   
   public void reset()
   {
      objectsFired.clear();
   }
   
}
