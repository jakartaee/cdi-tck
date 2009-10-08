package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;

@Stateful
@SessionScoped
@Named("Charlie")
@Default
public class InitializerSimpleBean implements Serializable, InitializerSimpleBeanLocal
{
   private static final long serialVersionUID = 1L;
   private static int        initializerCalls = 0;

   @Inject
   public void initializer()
   {
      initializerCalls++;
   }

   public void businessMethod()
   {
      
   }

   public int getInitializerCalls()
   {
      return initializerCalls;
   }

   public void setInitializerCalls(int initializerCalls)
   {
      InitializerSimpleBean.initializerCalls = initializerCalls;
   }
}
