package org.jboss.jsr299.tck.tests.extensions.afterBeanDiscovery;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessBean;

import org.jboss.jsr299.tck.literals.DefaultLiteral;

public class AfterBeanDiscoveryObserver implements Extension
{
   
   private static boolean processBeanFiredForCockatooBean;
   
   public static boolean isProcessBeanFiredForCockatooBean()
   {
      return processBeanFiredForCockatooBean;
   }
   
   public void observeProcessBean(@Observes ProcessBean<Cockatoo> event)
   {
      AfterBeanDiscoveryObserver.processBeanFiredForCockatooBean = true;
      assert event.getBean().getName().equals("cockatoo");
   }
   
   public void addABean(@Observes AfterBeanDiscovery afterBeanDiscovery)
   {
      afterBeanDiscovery.addBean(new Bean<Cockatoo>()
      {
         
         private final Set<Annotation> qualifiers = new HashSet<Annotation>(Arrays.asList(new DefaultLiteral()));
         private final Set<Type> types = new HashSet<Type>(Arrays.<Type>asList(Cockatoo.class));

         public Class<?> getBeanClass()
         {
            return Cockatoo.class;
         }

         public Set<InjectionPoint> getInjectionPoints()
         {
            return Collections.emptySet();
         }

         public String getName()
         {
            return "cockatoo";
         }

         public Set<Annotation> getQualifiers()
         {
            return qualifiers;
         }

         public Class<? extends Annotation> getScope()
         {
            return Dependent.class;
         }

         public Set<Class<? extends Annotation>> getStereotypes()
         {
            return Collections.emptySet();
         }

         public Set<Type> getTypes()
         {
            return types;
         }

         public boolean isAlternative()
         {
            return false;
         }

         public boolean isNullable()
         {
            return true;
         }

         public Cockatoo create(CreationalContext<Cockatoo> creationalContext)
         {
            return new Cockatoo("Billy");
         }

         public void destroy(Cockatoo instance, CreationalContext<Cockatoo> creationalContext)
         {
            // No-op
         }
      });
   }

}
