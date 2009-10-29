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

package org.jboss.jsr299.tck.tests.extensions.beanManager;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.AmbiguousResolutionException;
import javax.enterprise.inject.InjectionException;
import javax.enterprise.inject.Specializes;
import javax.enterprise.inject.Stereotype;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.PassivationCapable;
import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.RetentionLiteral;
import org.jboss.jsr299.tck.literals.TargetLiteral;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

/**
 * Mostly tests for extensions specified in chapter 11 of the specification
 * and not already tested elsewhere.
 * 
 * @author David Allen
 *
 */
@Artifact
@SpecVersion(spec="cdi", version="20091018")
public class BeanManagerTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "11.3.6", id = "a")
   public void testGetPassivationCapableBeanById()
   {
      Bean<?> bean = getCurrentManager().getBeans(Cow.class).iterator().next();
      assert PassivationCapable.class.isAssignableFrom(bean.getClass());
      PassivationCapable passivationCapable = (PassivationCapable) bean;
      Bean<?> passivatingBean = getCurrentManager().getPassivationCapableBean(passivationCapable.getId());
      assert bean == passivatingBean;
   }

   @Test
   @SpecAssertion(section = "11.3.7", id = "a")
   public void testAmbiguousDependencyResolved()
   {
      Set<Bean<?>> beans = new HashSet<Bean<?>>();
      beans.addAll(getCurrentManager().getBeans(SimpleBean.class));
      beans.addAll(getCurrentManager().getBeans(DerivedBean.class));
      getCurrentManager().resolve(beans);
   }
   
   @Test(expectedExceptions = AmbiguousResolutionException.class)
   @SpecAssertion(section = "11.3.7", id = "b")
   public void testAmbiguousDependencyNotResolved()
   {
      Set<Bean<?>> beans = new HashSet<Bean<?>>();
      beans.addAll(getCurrentManager().getBeans(Dog.class));
      beans.addAll(getCurrentManager().getBeans(Terrier.class));
      getCurrentManager().resolve(beans);
   }
   
   @Test(expectedExceptions = InjectionException.class)
   @SpecAssertions({
      @SpecAssertion(section = "11.3.8", id = "a"),
      @SpecAssertion(section = "11.3.8", id = "b")
   })
   public void testValidateThrowsException()
   {
      DogHouse dogHouse = getInstanceByType(DogHouse.class);
      InjectionPoint injectionPoint = new InjectionPointDecorator(dogHouse.getDog().getInjectedMetadata());
      // Wrap the injection point to change the type to a more generalized class
      getCurrentManager().validate(injectionPoint);
   }
   
   @Test(groups="rewrite")
   @SpecAssertion(section = "11.3.13", id = "aa")
   // Should also check a custom bindingtype
   public void testDetermineBindingType()
   {
      assert getCurrentManager().isQualifier(Tame.class);
      assert !getCurrentManager().isQualifier(AnimalStereotype.class);
      assert !getCurrentManager().isQualifier(ApplicationScoped.class);
      assert !getCurrentManager().isQualifier(Transactional.class);
   }
   
   @Test(groups="rewrite")
   // Should also check a custom scope
   @SpecAssertion(section = "11.3.13", id = "ab")
   public void testDetermineScopeType()
   {
      assert getCurrentManager().isScope(ApplicationScoped.class);
      assert !getCurrentManager().isScope(Tame.class);
      assert !getCurrentManager().isScope(AnimalStereotype.class);
      assert !getCurrentManager().isScope(Transactional.class);
   }
   
   @Test(groups="rewrite")
   @SpecAssertion(section = "11.3.13", id = "ac")
   // Should also check a custom stereotype
   public void testDetermineStereotype()
   {
      assert getCurrentManager().isStereotype(AnimalStereotype.class);
      assert !getCurrentManager().isStereotype(Tame.class);
      assert !getCurrentManager().isStereotype(ApplicationScoped.class);
      assert !getCurrentManager().isStereotype(Transactional.class);
   }
   
   @Test(groups="rewrite")
   // WBRI-59
   // Should also check a custom interceptor binding type
   @SpecAssertion(section = "11.3.13", id = "ad")
   public void testDetermineInterceptorBindingType()
   {
      assert getCurrentManager().isInterceptorBindingType(Transactional.class);
      assert !getCurrentManager().isInterceptorBindingType(Tame.class);
      assert !getCurrentManager().isInterceptorBindingType(AnimalStereotype.class);
      assert !getCurrentManager().isInterceptorBindingType(ApplicationScoped.class);
   }
   
   @Test(groups = { "rewrite"})
   @SpecAssertion(section = "11.3.13", id = "ae")
   // Should also check a custom sterotype
   public void testGetMetaAnnotationsForStereotype()
   {
      Set<Annotation> stereotypeAnnotations = getCurrentManager().getStereotypeDefinition(AnimalStereotype.class);
      assert stereotypeAnnotations.size() == 5;
      assert stereotypeAnnotations.contains(new AnnotationLiteral<Stereotype>() {});
      assert stereotypeAnnotations.contains(new AnnotationLiteral<RequestScoped>() {});
      assert stereotypeAnnotations.contains(new AnnotationLiteral<Inherited>() {});
      assert stereotypeAnnotations.contains(new RetentionLiteral()
      {
         
         public RetentionPolicy value()
         {
            return RetentionPolicy.RUNTIME;
         }
         
      });
      assert stereotypeAnnotations.contains(new TargetLiteral()
      {
         
         public ElementType[] value()
         {
            ElementType[] value = {TYPE, METHOD, FIELD};
            return value;
         }
         
      });
   }

   @Test(groups = {"ri-broken", "rewrite"})
   @SpecAssertion(section = "11.3.13", id = "af")
   // WBRI-59
   // Should also check a custom defined interceptor binding
   public void testGetMetaAnnotationsForInterceptorBindingType()
   {
      Set<Annotation> metaAnnotations = getCurrentManager().getInterceptorBindingTypeDefinition(Transactional.class);
      assert metaAnnotations.size() == 4;
      assert metaAnnotations.contains(new AnnotationLiteral<Target>() {});
      assert metaAnnotations.contains(new AnnotationLiteral<Retention>() {});
      assert metaAnnotations.contains(new AnnotationLiteral<Documented>() {});
      assert metaAnnotations.contains(new AnnotationLiteral<Qualifier>() {});
   }

   @Test(groups = {"rewrite"})
   @SpecAssertion(section = "11.3.13", id = "ag")
   // Should also check a custom defined scope
   public void testgetScope()
   {
      assert getCurrentManager().isNormalScope(RequestScoped.class);
      assert !getCurrentManager().isPassivatingScope(RequestScoped.class);
      
      assert getCurrentManager().isNormalScope(SessionScoped.class);
      assert getCurrentManager().isPassivatingScope(SessionScoped.class);
   }

   @Test
   @SpecAssertion(section = "11.3.15", id = "a")
   public void testGetELResolver()
   {
      assert getCurrentManager().getELResolver() != null;
   }

   @Test
   @SpecAssertion(section = "11.3.17", id = "a")
   public void testObtainingAnnotatedType()
   {
      AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(DerivedBean.class);
      assert annotatedType.isAnnotationPresent(Specializes.class);
      assert annotatedType.isAnnotationPresent(Tame.class);
      assert annotatedType.getFields().size() == 1;
      assert annotatedType.getMethods().isEmpty();
      assert annotatedType.getTypeClosure().size() == 3;
   }

   @Test
   @SpecAssertion(section = "11.3.18", id = "aa")
   public void testObtainingInjectionTarget()
   {
      AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(DerivedBean.class);
      assert getCurrentManager().createInjectionTarget(annotatedType) != null;
   }
}
