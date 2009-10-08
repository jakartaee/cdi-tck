package org.jboss.jsr299.tck.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.inject.AmbiguousResolutionException;
import javax.enterprise.inject.TypeLiteral;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;


public class OldSPIBridge
{

   /**
    * This used to be an spi method of BeanManager, but it has been removed.
    * Replicate it here using the new spi, and deprecate its usage
    *
    */
   @Deprecated
   public static <T> T getInstanceByType(BeanManager manager, Class<T> beanType, Annotation... bindings) 
   {
      Bean<T> bean = (Bean<T>) ensureUniqueBean(beanType, manager.getBeans(beanType, bindings));
      return (T)manager.getReference(bean ,beanType, manager.createCreationalContext(bean));
   }
   
   @Deprecated
   public static <T> T getInstanceByType(BeanManager manager, TypeLiteral<T> beanType, Annotation... bindings) 
   {
      Bean<T> bean = (Bean<T>) ensureUniqueBean(beanType.getType(), manager.getBeans(beanType.getType(), bindings));
      return (T) manager.getReference(bean,beanType.getType(), manager.createCreationalContext(bean));
   }

   public static Bean<?> ensureUniqueBean(Type type, Set<Bean<?>> beans)
   {
      if (beans.size() == 0) 
      {
         throw new UnsatisfiedResolutionException("Unable to resolve any Web Beans of " + type);
      }
      else if (beans.size() > 1) 
      {
         throw new AmbiguousResolutionException("More than one bean available for type " + type);
      }
      return beans.iterator().next();
   }

   
   @Deprecated
   public static Object getInstanceByName(BeanManager manager, String name)
   {
      Set<Bean<?>> beans = manager.getBeans(name);
      if (beans.size() == 0)
      {
         return null;
      }
      else if (beans.size() > 1)
      {
         throw new AmbiguousResolutionException("Resolved multiple Web Beans with " + name);
      }
      else
      {
         Bean<?> bean = beans.iterator().next();
         return manager.getReference(bean, bean.getTypes().iterator().next(), manager.createCreationalContext(bean));
      }
   }

}
