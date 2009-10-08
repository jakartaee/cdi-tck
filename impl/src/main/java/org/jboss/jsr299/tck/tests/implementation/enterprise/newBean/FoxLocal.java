package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;

import javax.ejb.Local;
import javax.enterprise.inject.New;

import org.jboss.jsr299.tck.literals.NewLiteral;

@Local
public interface FoxLocal
{

   public Den getDen();

   public void setDen(Den den);

   public int getNextLitterSize();

   public void setNextLitterSize(int nextLitterSize);

   public Litter produceLitter();

   public void disposeLitter(Litter litter);

   public boolean isLitterDisposed();
   
   public static final New NEW = new NewLiteral()
   {
      
      public Class<?> value()
      {
         return Fox.class;
      }
   };

}