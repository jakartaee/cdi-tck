package org.jboss.jsr299.tck;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.AmbiguousResolutionException;
import javax.enterprise.inject.TypeLiteral;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.jsr299.tck.api.JSR299Configuration;
import org.jboss.jsr299.tck.impl.OldSPIBridge;
import org.jboss.testharness.api.DeploymentException;
import org.jboss.testharness.impl.ConfigurationFactory;

public abstract class AbstractJSR299Test extends org.jboss.testharness.AbstractTest
{
   protected BeanManager getCurrentManager()
   {
      return getCurrentConfiguration().getManagers().getManager();
   }

   protected byte[] serialize(Object instance) throws IOException
   {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(bytes);
      out.writeObject(instance);
      return bytes.toByteArray();
   }

   protected Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException
   {
      ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
      return in.readObject();
   }

   protected void setContextActive(Context context)
   {
      getCurrentConfiguration().getContexts().setActive(context);
   }

   protected void setContextInactive(Context context)
   {
      getCurrentConfiguration().getContexts().setInactive(context);
   }

   protected void destroyContext(Context context)
   {
      getCurrentConfiguration().getContexts().destroyContext(context);
   }

   @Override
   protected JSR299Configuration getCurrentConfiguration()
   {
      return ConfigurationFactory.get(JSR299Configuration.class);
   }

   /**
    * Checks if all annotations are in a given set of annotations
    * 
    * @param annotations The annotation set
    * @param requiredAnnotationTypes The annotations to match
    * @return True if match, false otherwise
    */
   public boolean annotationSetMatches(Set<? extends Annotation> annotations, Class<? extends Annotation>... requiredAnnotationTypes)
   {
      List<Class<? extends Annotation>> annotationTypeList = new ArrayList<Class<? extends Annotation>>();
      annotationTypeList.addAll(Arrays.asList(requiredAnnotationTypes));
      for (Annotation annotation : annotations)
      {
         if (annotationTypeList.contains(annotation.annotationType()))
         {
            annotationTypeList.remove(annotation.annotationType());
         }
         else
         {
            return false;
         }
      }
      return annotationTypeList.size() == 0;
   }
   
   public boolean typeSetMatches(Set<Type> types, Class<?>... requiredTypes)
   {
      List<Class<?>> typeList = new ArrayList<Class<?>>();
      typeList.addAll(Arrays.asList(requiredTypes));
      for (Type type : types)
      {
         if (type instanceof Class<?>)
         {
            typeList.remove(type);
         }
         else if (type instanceof ParameterizedType)
         {
            typeList.remove(((ParameterizedType) type).getRawType());
         }
      }
      return typeList.size() == 0;
   }

   public <T> Bean<T> getUniqueBean(Class<T> type, Annotation... bindings)
   {
      Set<Bean<T>> beans = getBeans(type, bindings);
      return resolveUniqueBean(type, beans);
   }
   
   public <T> Bean<T> getUniqueBean(TypeLiteral<T> type, Annotation... bindings)
   {
      Set<Bean<T>> beans = getBeans(type, bindings);
      return resolveUniqueBean(type.getType(), beans);
   }
   
   public <T> Set<Bean<T>> getBeans(Class<T> type, Annotation... bindings)
   {
      return (Set) getCurrentManager().getBeans(type, bindings);
   }

   public <T> Set<Bean<T>> getBeans(TypeLiteral<T> type, Annotation... bindings)
   {
      return (Set)getCurrentManager().getBeans(type.getType(), bindings);
   }

   public <T> T getInstanceByType(Class<T> beanType, Annotation... bindings)
   {
      return OldSPIBridge.getInstanceByType(getCurrentManager(), beanType, bindings);
   }

   public <T> T getInstanceByType(TypeLiteral<T> beanType, Annotation... bindings)
   {
      return OldSPIBridge.getInstanceByType(getCurrentManager(), beanType, bindings);
   }

   public Object getInstanceByName(String name)
   {
      return OldSPIBridge.getInstanceByName(getCurrentManager(), name);
   }

   private <T> Bean<T> resolveUniqueBean(Type type, Set<Bean<T>> beans)
   {
      if (beans.size() == 0)
      {
         throw new UnsatisfiedResolutionException("Unable to resolve any beans of " + type);
      }
      else if (beans.size() > 1)
      {
         throw new AmbiguousResolutionException("More than one bean available (" + beans + ")");
      }
      return beans.iterator().next();
   }
   
   @Override
   protected DeploymentException handleDeploymentFailure(DeploymentException deploymentException)
   {
      if (getCurrentConfiguration().getManagers().isDefinitionError(deploymentException))
      {
         return new DeploymentException(deploymentException, new DefinitionError(deploymentException.getCause()));
      }
      else if (getCurrentConfiguration().getManagers().isDeploymentError(deploymentException))
      {
         return new DeploymentException(deploymentException, new DeploymentError(deploymentException.getCause()));
      }
      else
      {
         return deploymentException;
      }
   }
}
