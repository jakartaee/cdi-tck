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

class CustomInterceptorImplementation implements Interceptor<InterceptorClass>
{

   private Set<Annotation> interceptorBindingTypes = new HashSet<Annotation>();

   public CustomInterceptorImplementation()
   {
      interceptorBindingTypes.add(new AnnotationLiteral<Secure>()
      {
      });
      interceptorBindingTypes.add(new AnnotationLiteral<Transactional>()
      {
      });
   }

   public Set<Annotation> getInterceptorBindings()
   {
      return Collections.unmodifiableSet(interceptorBindingTypes);
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

   public Object intercept(InterceptionType type, InterceptorClass instance, InvocationContext ctx)
   {
      try {
         return instance.intercept(ctx);
      } catch (Exception e) {
         return null;
      }
   }

   public boolean intercepts(InterceptionType type)
   {
      return type.equals(InterceptionType.AROUND_INVOKE);
   }

   public Class<?> getBeanClass()
   {
      return InterceptorClass.class;
   }

   public InterceptorClass create(CreationalContext<InterceptorClass> creationalContext)
   {
      return new InterceptorClass();
   }

   public void destroy(InterceptorClass instance, CreationalContext<InterceptorClass> creationalContext)
   {
      creationalContext.release();
   }
}
