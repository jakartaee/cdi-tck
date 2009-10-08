package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

@Stateful
@SessionScoped
public class Fox implements FoxLocal
{
   @Produces @Tame
   private Den den = new Den("FoxDen");
   
   private int nextLitterSize;
   
   private boolean litterDisposed = false;
   
   /* (non-Javadoc)
    * @see org.jboss.jsr299.tck.tests.implementation.enterprise.newBean.FoxLocal#getDen()
    */
   public Den getDen()
   {
      return den;
   }

   /* (non-Javadoc)
    * @see org.jboss.jsr299.tck.tests.implementation.enterprise.newBean.FoxLocal#setDen(org.jboss.jsr299.tck.tests.implementation.enterprise.newBean.Den)
    */
   public void setDen(Den den)
   {
      this.den = den;
   }

   /* (non-Javadoc)
    * @see org.jboss.jsr299.tck.tests.implementation.enterprise.newBean.FoxLocal#getNextLitterSize()
    */
   public int getNextLitterSize()
   {
      return nextLitterSize;
   }

   /* (non-Javadoc)
    * @see org.jboss.jsr299.tck.tests.implementation.enterprise.newBean.FoxLocal#setNextLitterSize(int)
    */
   public void setNextLitterSize(int nextLitterSize)
   {
      this.nextLitterSize = nextLitterSize;
   }
   
   @Produces @Tame
   public Litter produceLitter()
   {
      return new Litter(nextLitterSize);
   }
   
   public void disposeLitter(@Disposes @Tame Litter litter)
   {
      this.litterDisposed = true;
   }

   /* (non-Javadoc)
    * @see org.jboss.jsr299.tck.tests.implementation.enterprise.newBean.FoxLocal#isLitterDisposed()
    */
   public boolean isLitterDisposed()
   {
      return litterDisposed;
   }
}
