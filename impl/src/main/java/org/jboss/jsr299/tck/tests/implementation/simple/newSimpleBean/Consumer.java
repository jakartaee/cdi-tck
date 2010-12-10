package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import javax.enterprise.inject.New;
import javax.inject.Inject;


public class Consumer
{
   
   @Inject
   private ExplicitContructorSimpleBean explicitConstructorBean;
   
   @Inject @New
   private ExplicitContructorSimpleBean newExplicitConstructorBean;
   
   @Inject 
   private InitializerSimpleBean initializerSimpleBean;
   
   @Inject @New
   private InitializerSimpleBean newInitializerSimpleBean;
   
   public ExplicitContructorSimpleBean getExplicitConstructorBean()
   {
      return explicitConstructorBean;
   }
   
   public ExplicitContructorSimpleBean getNewExplicitConstructorBean()
   {
      return newExplicitConstructorBean;
   }
   
   public InitializerSimpleBean getInitializerSimpleBean()
   {
      return initializerSimpleBean;
   }
   
   public InitializerSimpleBean getNewInitializerSimpleBean()
   {
      return newInitializerSimpleBean;
   }

}
