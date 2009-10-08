package org.jboss.jsr299.tck.tests.event.eventTypes;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

class TuneSelect<T>
{
   @Inject @Any Event<Artist<T>> soloArtistEvent;
   
   @Inject @Any Event<Song> songEvent;
   
   @Inject @Any Event<Broadcast> broadcastEvent;
   
   public void songPlaying(Song s)
   {
      songEvent.fire(s);
   }
   
   public void broadcastPlaying(Broadcast b)
   {
      broadcastEvent.fire(b);
   }
   
   public void soloArtistPlaying(Artist<T> soloArtist)
   {
      soloArtistEvent.fire(soloArtist);
   }
}
