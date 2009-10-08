package org.jboss.jsr299.tck;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.context.Context;
import javax.inject.manager.Manager;

import org.jboss.jsr299.tck.api.JSR299Configuration;
import org.jboss.jsr299.tck.impl.JSR299ConfigurationImpl;

public abstract class AbstractJSR299Test extends org.jboss.testharness.AbstractTest
{
   
   protected abstract static class RunInDependentContext
   {
   
      protected void setup()
      {
         getCurrentConfiguration().getContexts().setActive(getCurrentConfiguration().getContexts().getDependentContext());
      }
   
      protected void cleanup()
      {
         getCurrentConfiguration().getContexts().setInactive(getCurrentConfiguration().getContexts().getDependentContext());
      }
   
      public final void run() throws Exception
      {
         try
         {
            setup();
            execute();
         }
         finally
         {
            cleanup();
         }
      }
      
      protected JSR299Configuration getCurrentConfiguration()
      {
         return JSR299ConfigurationImpl.get();
      }
   
      protected abstract void execute() throws Exception;
   
   }
   
   private Manager currentManager;
   
   protected void setCurrentManager(Manager currentManager)
   {
      this.currentManager = currentManager;
   }
   

   protected Manager getCurrentManager()
   {
      return currentManager;
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
   
   protected JSR299Configuration getCurrentConfiguration()
   {
      return JSR299ConfigurationImpl.get();
   }
   
   @Override
   public void beforeMethod()
   {
      super.beforeMethod();
      setCurrentManager(getCurrentConfiguration().getManagers().getManager());
   }
   
   @Override
   public void afterMethod()
   {
      super.afterMethod();
      setCurrentManager(null);
   }
   
   /**
    * Checks if all annotations are in a given set of annotations
    * 
    * @param annotations The annotation set
    * @param annotationTypes The annotations to match
    * @return True if match, false otherwise
    */
   public boolean annotationSetMatches(Set<Annotation> annotations, Class<? extends Annotation>... annotationTypes)
   {
      List<Class<? extends Annotation>> annotationTypeList = new ArrayList<Class<? extends Annotation>>();
      annotationTypeList.addAll(Arrays.asList(annotationTypes));
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
   
}