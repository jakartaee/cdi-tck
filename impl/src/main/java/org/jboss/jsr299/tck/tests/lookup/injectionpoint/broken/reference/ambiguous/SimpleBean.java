package org.jboss.jsr299.tck.tests.lookup.injectionpoint.broken.reference.ambiguous;


class SimpleBean
{
   private InjectedBean injectedBean;

   public InjectedBean getInjectedBean()
   {
      return injectedBean;
   }

   public void setInjectedBean(InjectedBean injectedBean)
   {
      this.injectedBean = injectedBean;
   }
}
