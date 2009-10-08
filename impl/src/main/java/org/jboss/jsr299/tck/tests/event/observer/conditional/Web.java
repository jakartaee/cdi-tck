package org.jboss.jsr299.tck.tests.event.observer.conditional;

@Spun class Web
{
   private int rings = 0;
   
   public void addRing()
   {
      this.rings++;
   }
   
   public void setRings(int rings)
   {
      this.rings = rings;
   }
   
   public int getRings()
   {
      return this.rings;
   }
}
