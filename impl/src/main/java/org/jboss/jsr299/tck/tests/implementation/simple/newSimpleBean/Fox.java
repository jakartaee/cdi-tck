package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;

import org.jboss.jsr299.tck.literals.NewLiteral;

@AnimalStereotype
class Fox
{
   
   public static final New NEW = new NewLiteral()
   {
      
      public Class<?> value()
      {
         return Fox.class;
      }
      
   };
   
   @Produces
   private Den den = new Den("FoxDen");
   
   private int nextLitterSize;
   
   private boolean litterDisposed = false;
   
   public void observeEvent(@Observes String event)
   {
      
   }

   public Den getDen()
   {
      return den;
   }

   public void setDen(Den den)
   {
      this.den = den;
   }

   public int getNextLitterSize()
   {
      return nextLitterSize;
   }

   public void setNextLitterSize(int nextLitterSize)
   {
      this.nextLitterSize = nextLitterSize;
   }
   
   @Produces
   public Litter produceLitter()
   {
      return new Litter(nextLitterSize);
   }
   
   public void disposeLitter(@Disposes Litter litter)
   {
      this.litterDisposed = true;
   }

   public boolean isLitterDisposed()
   {
      return litterDisposed;
   }
}
