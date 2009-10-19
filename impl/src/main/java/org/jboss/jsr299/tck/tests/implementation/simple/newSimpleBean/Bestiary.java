package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;

@RequestScoped
public class Bestiary
{
   
   private Set<String> possibleNames;
   private Set<String> knightsWhichKilledTheDragons;
   
   @Produces Collection<Dragon> getDragons(@New ArrayList<Dragon> dragons)
   {
      return dragons;
   }
   
   public void observeBirth(@Observes Griffin griffin, @New TreeSet<String> possibleNames)
   {
      this.possibleNames = possibleNames;
   }
   
   public void destroyDragons(@Disposes Collection<Dragon> dragons, @New LinkedHashSet<String> knights)
   {
      this.knightsWhichKilledTheDragons = knights;
   }
   
   public Set<String> getPossibleNames()
   {
      return possibleNames;
   }
   
   public Set<String> getKnightsWhichKilledTheDragons()
   {
      return knightsWhichKilledTheDragons;
   }

}
