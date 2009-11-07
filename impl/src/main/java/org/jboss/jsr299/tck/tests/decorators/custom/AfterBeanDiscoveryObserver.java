package org.jboss.jsr299.tck.tests.decorators.custom;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;


public class AfterBeanDiscoveryObserver implements Extension
{
   @Inject
   private BeanManager manager;
   
   private static CustomDecoratorImplementation decorator;
   
   @SuppressWarnings("unchecked")
   public void addInterceptors(@Observes AfterBeanDiscovery event) {
      AnnotatedField<VehicleDecorator> annotatedField = (AnnotatedField<VehicleDecorator>) manager.createAnnotatedType(VehicleDecorator.class).getFields().iterator().next();
      decorator = new CustomDecoratorImplementation(annotatedField);
      
      event.addBean(decorator);
   }

   public static CustomDecoratorImplementation getDecorator()
   {
      return decorator;
   }
}
