package org.jboss.jsr299.tck.tests.interceptors.definition.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.inject.spi.Interceptor;
import javax.enterprise.util.AnnotationLiteral;
import javax.interceptor.InvocationContext;

class CustomInterceptorImplementation implements Interceptor<SimpleInterceptorWithoutAnnotations>
{

   private Set<Annotation> interceptorBindingTypes = new HashSet<Annotation>();
   private InterceptionType type;
   private boolean getInterceptorBindingsCalled = false;
   private boolean interceptsCalled = false;

   public CustomInterceptorImplementation(InterceptionType type)
   {
      this.type = type;
      interceptorBindingTypes.add(new AnnotationLiteral<Secure>()
      {
      });
      interceptorBindingTypes.add(new AnnotationLiteral<Transactional>()
      {
      });
   }

   public Set<InjectionPoint> getInjectionPoints()
   {
      return Collections.emptySet();
   }

   public String getName()
   {
      return null;
   }

   public Set<Annotation> getQualifiers()
   {
      return Collections.emptySet();
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
      Set<Type> types = new HashSet<Type>();
      types.add(Object.class);
      types.add(getBeanClass());
      return types;
   }

   public boolean isAlternative()
   {
      return false;
   }

   public boolean isNullable()
   {
      return false;
   }

   public Object intercept(InterceptionType type, SimpleInterceptorWithoutAnnotations instance, InvocationContext ctx)
   {
      try {
         return instance.intercept(ctx);
      } catch (Exception e) {
         return null;
      }
   }

   public boolean intercepts(InterceptionType type)
   {
      interceptsCalled = true;
      return this.type.equals(type);
   }

   public Set<Annotation> getInterceptorBindings()
   {
      return Collections.unmodifiableSet(interceptorBindingTypes);
   }
   
   public Class<?> getBeanClass()
   {
      return SimpleInterceptorWithoutAnnotations.class;
   }

   public SimpleInterceptorWithoutAnnotations create(CreationalContext<SimpleInterceptorWithoutAnnotations> creationalContext)
   {
      return new SimpleInterceptorWithoutAnnotations();
   }

   public void destroy(SimpleInterceptorWithoutAnnotations instance, CreationalContext<SimpleInterceptorWithoutAnnotations> creationalContext)
   {
      creationalContext.release();
   }


   public boolean isGetInterceptorBindingsCalled()
   {
      return getInterceptorBindingsCalled;
   }


   public boolean isInterceptsCalled()
   {
      return interceptsCalled;
   }
}
