package org.jboss.jsr299.tck.tests.decorators.custom;

import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

public class AfterBeanDiscoveryObserver implements Extension
{

   private static CustomDecoratorImplementation decorator;

   @SuppressWarnings("unchecked")
   public void addInterceptors(@Observes AfterBeanDiscovery event,BeanManager beanManager)
   {
      AnnotatedType<VehicleDecorator> type = beanManager.createAnnotatedType(VehicleDecorator.class);
      Set<AnnotatedField<? super VehicleDecorator>> annotatedFields = type.getFields();
      AnnotatedField<? super VehicleDecorator> annotatedField = annotatedFields.iterator().next();
      decorator = new CustomDecoratorImplementation(annotatedField);

      event.addBean(decorator);
   }
   
   public void vetoVehicleDecorator(@Observes ProcessAnnotatedType<VehicleDecorator> event)
   {
      event.veto();
   }

   public static CustomDecoratorImplementation getDecorator()
   {
      return decorator;
   }
}
