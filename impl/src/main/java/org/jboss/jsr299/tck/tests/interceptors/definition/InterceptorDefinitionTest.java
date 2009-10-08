/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.jsr299.tck.tests.interceptors.definition;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.inject.spi.Interceptor;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

/**
 * Tests related to the definition of interceptors, but not necessarily
 * their execution.
 *
 * @author David Allen
 *
 */
@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class InterceptorDefinitionTest extends AbstractJSR299Test
{
   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.1.2", id = "a")
   })
   // WBRI-59
   public void testInterceptorsImplementInterceptorInterface()
   {
      boolean interfaceFound = false;
      for (Type type : getInterfacesImplemented(getBeans(TransactionalInterceptor.class).iterator().next().getClass()))
      {
         if (type.equals(Interceptor.class))
         {
            interfaceFound = true;
            break;
         }
      }
      assert interfaceFound;
   }
   
   private Set<Type> getInterfacesImplemented(Class<?> clazz)
   {
      Set<Type> interfaces = new HashSet<Type>();
      interfaces.addAll(new HierarchyDiscovery(clazz).getFlattenedTypes());
      return interfaces;
   }
   
   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.1.2", id = "b")
   })
   // WBRI-59
   public void testInterceptorBindingTypes()
   {
      Interceptor<?> interceptorBean = (Interceptor<?>) getBeans(TransactionalInterceptor.class).iterator().next();
      assert interceptorBean.getInterceptorBindingTypes().size() == 1;
      assert interceptorBean.getInterceptorBindingTypes().contains(Transactional.class);
   }

   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.1.2", id = "c"),
      @SpecAssertion(section = "11.1.2", id = "e")
   })
   // WBRI-59
   public void testInterceptionType()
   {
      Interceptor<?> interceptorBean = (Interceptor<?>) getBeans(TransactionalInterceptor.class).iterator().next();
      assert interceptorBean.intercepts(InterceptionType.AROUND_INVOKE);
      assert !interceptorBean.intercepts(InterceptionType.POST_ACTIVATE);
      assert !interceptorBean.intercepts(InterceptionType.POST_CONSTRUCT);
      assert !interceptorBean.intercepts(InterceptionType.PRE_DESTROY);
      assert !interceptorBean.intercepts(InterceptionType.PRE_PASSIVATE);
   }

   @Test(groups = "ri-broken")
   @SpecAssertion(section = "11.1.2", id = "f")
   // WBRI-59
   public void testInstanceOfInterceptorForEveryEnabledInterceptor()
   {
      assert !getBeans(TransactionalInterceptor.class).isEmpty();
      assert !getBeans(SecureInterceptor.class).isEmpty();
   }

   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.3.12", id = "a")
   })
   // WBRI-59
   public void testResolveInterceptorsReturnsOrderedList()
   {
      Annotation transactionalBinding = new AnnotationLiteral<Transactional>() {};
      Annotation secureBinding = new AnnotationLiteral<Secure>() {};
      List<Interceptor<?>> interceptors = getCurrentManager().resolveInterceptors(
            InterceptionType.AROUND_INVOKE,
            transactionalBinding,
            secureBinding
      );
      assert interceptors.size() == 2;
      assert interceptors.get(0).getInterceptorBindingTypes().size() == 1;
      assert interceptors.get(0).getInterceptorBindingTypes().contains(transactionalBinding);
      assert interceptors.get(1).getInterceptorBindingTypes().size() == 1;
      assert interceptors.get(1).getInterceptorBindingTypes().contains(secureBinding);
   }
   
   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.3.12", id = "b")
   })
   // WBRI-59
   public void testSameBindingTypesToResolveInterceptorsFails()
   {
      Annotation transactionalBinding = new AnnotationLiteral<Transactional>() {};
      getCurrentManager().resolveInterceptors(InterceptionType.AROUND_INVOKE, transactionalBinding, transactionalBinding);
   }
   
   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.3.12", id = "c")
   })
   // WBRI-59
   public void testNoBindingTypesToResolveInterceptorsFails()
   {
      getCurrentManager().resolveInterceptors(InterceptionType.AROUND_INVOKE);
   }
   
   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "11.3.12", id = "d")
   })
   // WBRI-59
   public void testNonBindingTypeToResolveInterceptorsFails()
   {
      Annotation nonBinding = new AnnotationLiteral<NonBindingType>() {};
      getCurrentManager().resolveInterceptors(InterceptionType.AROUND_INVOKE, nonBinding);
   }
   
   @Test(groups = "ri-broken")
   @SpecAssertions({
     @SpecAssertion(section = "7.2", id = "a"),
     @SpecAssertion(section = "9.2", id = "a"),
     @SpecAssertion(section = "12.3", id = "kb")
   })
   public void testManagedBeanIsIntercepted()
   {
      MissileInterceptor.intercepted = false;
      
      Missile missile = getInstanceByType(Missile.class);
      missile.fire();
      
      assert MissileInterceptor.intercepted;
   }
   
   @Test
   @SpecAssertion(section = "7.2", id = "a1")
   public void testInitializerMethodsNotIntercepted()
   {
      MissileInterceptor.intercepted = false;
      
      getInstanceByType(Missile.class);
      
      assert !MissileInterceptor.intercepted;
   }
   
   @Test(groups = "ri-broken")
   @SpecAssertion(section = "7.2", id = "ia")
   public void testProducerMethodsAreIntercepted()
   {
      MissileInterceptor.intercepted = false;
      
      getInstanceByType(Wheat.class);
      
      assert MissileInterceptor.intercepted;
   }
   
   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "9.1", id = "a"),
      @SpecAssertion(section = "9.1", id = "b"),
      @SpecAssertion(section = "9.1", id = "c"),
      @SpecAssertion(section = "9.3", id = "a")
   })
   public void testInterceptorBindingAnnotation()
   {      
      Interceptor<?> interceptorBean = (Interceptor<?>) getBeans(FileLogger.class).iterator().next();
      assert interceptorBean.getInterceptorBindingTypes().size() == 1;
      assert interceptorBean.getInterceptorBindingTypes().contains(Logged.class);            

      Target target = ((AnnotatedElement) interceptorBean.getInterceptorBindingTypes().iterator().next()).getAnnotation(Target.class);
      List<ElementType> elements = Arrays.asList(target.value());
      assert elements.contains(ElementType.TYPE);
      assert elements.contains(ElementType.METHOD);          
   }
   
   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "9.1.2", id = "a"),
      @SpecAssertion(section = "9.1.2", id = "b"),
      @SpecAssertion(section = "2.7.1.2", id = "b")
   })
   public void testStereotypeInterceptorBindings()
   {      
      Bean<SecureTransaction> bean = getBeans(SecureTransaction.class).iterator().next();
      assert bean.getQualifiers().contains(Logged.class);            
   }
 
   @Test(groups = "ri-broken")
   @SpecAssertions({
      @SpecAssertion(section = "9.1.1", id = "a"),
      @SpecAssertion(section = "9.1.1", id = "b")
   })
   public void testInterceptorBindingsCanDeclareOtherInterceptorBindings()
   {
      AtomicInterceptor.intercepted = false;
      MissileInterceptor.intercepted = false;
      
      AtomicFoo foo = getInstanceByType(AtomicFoo.class);
      foo.doAction();
      
      assert AtomicInterceptor.intercepted;
      assert MissileInterceptor.intercepted;
   }
}
