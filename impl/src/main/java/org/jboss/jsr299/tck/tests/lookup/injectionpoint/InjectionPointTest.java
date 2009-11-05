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
package org.jboss.jsr299.tck.tests.lookup.injectionpoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.DefaultLiteral;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

/**
 * Injection point metadata tests
 * 
 * @author David Allen
 * @author Jozef Hartinger
 */
@Artifact
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="20091101")
public class InjectionPointTest extends AbstractJSR299Test
{

   @Test(groups = { "injectionPoint" })
   @SpecAssertions({
      @SpecAssertion(section = "6.5.3", id="d"),
      @SpecAssertion(section = "5.5.7", id = "aa")
   })
   public void testGetBean()
   {

      // Get an instance of the bean which has another bean injected into it
      FieldInjectionPointBean beanWithInjectedBean = getInstanceByType(FieldInjectionPointBean.class);
      BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
      assert beanWithInjectionPoint.getInjectedMetadata() != null;

      Set<Bean<FieldInjectionPointBean>> resolvedBeans = getBeans(FieldInjectionPointBean.class);
      assert resolvedBeans.size() == 1;
      assert beanWithInjectionPoint.getInjectedMetadata().getBean().equals(resolvedBeans.iterator().next());
   }

   @Test(groups = { "injectionPoint" })
   @SpecAssertion(section = "5.5.7", id = "ba")
   public void testGetType()
   {
      // Get an instance of the bean which has another bean injected into it
      FieldInjectionPointBean beanWithInjectedBean = getInstanceByType(FieldInjectionPointBean.class);
      BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
      assert beanWithInjectionPoint.getInjectedMetadata() != null;
      assert beanWithInjectionPoint.getInjectedMetadata().getType().equals(BeanWithInjectionPointMetadata.class);
   }

   @Test(groups = { "injectionPoint" })
   @SpecAssertion(section = "5.5.7", id = "bc")
   public void testGetBindingTypes()
   {
      // Get an instance of the bean which has another bean injected into it
      FieldInjectionPointBean beanWithInjectedBean = getInstanceByType(FieldInjectionPointBean.class);
      BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
      assert beanWithInjectionPoint.getInjectedMetadata() != null;
      Set<Annotation> bindingTypes = beanWithInjectionPoint.getInjectedMetadata().getQualifiers();
      assert bindingTypes.size() == 1;
      assert Default.class.isAssignableFrom(bindingTypes.iterator().next().annotationType());
   }

   @Test(groups = { "injectionPoint" })
   @SpecAssertion(section = "5.5.7", id = "ca")
   public void testGetMemberField()
   {
      // Get an instance of the bean which has another bean injected into it
      FieldInjectionPointBean beanWithInjectedBean = getInstanceByType(FieldInjectionPointBean.class);
      BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
      assert beanWithInjectionPoint.getInjectedMetadata() != null;
      assert Field.class.isAssignableFrom(beanWithInjectionPoint.getInjectedMetadata().getMember().getClass());
   }

   @Test(groups = { "injectionPoint" })
   @SpecAssertion(section = "5.5.7", id = "cb")
   public void testGetMemberMethod()
   {
      // Get an instance of the bean which has another bean injected into it
      MethodInjectionPointBean beanWithInjectedBean = getInstanceByType(MethodInjectionPointBean.class);
      BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
      assert beanWithInjectionPoint.getInjectedMetadata() != null;
      assert Method.class.isAssignableFrom(beanWithInjectionPoint.getInjectedMetadata().getMember().getClass());

      // Since the type and bindings must correspond to the parameter, check
      // them
      assert beanWithInjectionPoint.getInjectedMetadata().getType().equals(BeanWithInjectionPointMetadata.class);
      assert beanWithInjectionPoint.getInjectedMetadata().getQualifiers().contains(new DefaultLiteral());
   }

   @Test(groups = { "injectionPoint" })
   @SpecAssertion(section = "5.5.7", id = "cc")
   public void testGetMemberConstructor()
   {
      // Get an instance of the bean which has another bean injected into it
      ConstructorInjectionPointBean beanWithInjectedBean = getInstanceByType(ConstructorInjectionPointBean.class);
      BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
      assert beanWithInjectionPoint.getInjectedMetadata() != null;
      assert Constructor.class.isAssignableFrom(beanWithInjectionPoint.getInjectedMetadata().getMember().getClass());

      // Since the type and bindings must correspond to the parameter, check
      // them
      assert beanWithInjectionPoint.getInjectedMetadata().getType().equals(BeanWithInjectionPointMetadata.class);
      assert beanWithInjectionPoint.getInjectedMetadata().getQualifiers().contains(new DefaultLiteral());
   }

   @Test(groups = { "injectionPoint" })
   @SpecAssertion(section = "5.5.7", id = "daa")
   public void testGetAnnotatedField()
   {
      // Get an instance of the bean which has another bean injected into it
      FieldInjectionPointBean beanWithInjectedBean = getInstanceByType(FieldInjectionPointBean.class);
      BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
      assert beanWithInjectionPoint.getInjectedMetadata() != null;
      assert beanWithInjectionPoint.getInjectedMetadata().getAnnotated() instanceof AnnotatedField;
      assert beanWithInjectionPoint.getInjectedMetadata().getAnnotated().isAnnotationPresent(AnimalStereotype.class);
   }

   @Test(groups = { "injectionPoint" })
   @SpecAssertion(section = "5.5.7", id = "daa")
   public void testGetAnnotatedParameter()
   {
      // Get an instance of the bean which has another bean injected into it
      MethodInjectionPointBean beanWithInjectedBean = getInstanceByType(MethodInjectionPointBean.class);
      BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
      assert beanWithInjectionPoint.getInjectedMetadata() != null;
      assert beanWithInjectionPoint.getInjectedMetadata().getAnnotated() instanceof AnnotatedParameter;
      assert annotationSetMatches(beanWithInjectionPoint.getInjectedMetadata().getQualifiers(), Default.class);
   }

   @Test(groups = { "injectionPoint" })
   @SpecAssertion(section = "5.5.7", id = "ea")
   public void testDependentScope()
   {
      assert getBeans(InjectionPoint.class).size() == 1;
      assert getBeans(InjectionPoint.class).iterator().next().getScope().equals(Dependent.class);
   }
   
   @Test(groups = { "injectionPoint", "ri-broken" })
   @SpecAssertion(section = "5.5.7", id = "eb")
   //WELD-227
   public void testPassivationCapability() throws Exception
   {
      InjectionPoint ip1 = getInstanceByType(FieldInjectionPointBean.class).getInjectedBean().getInjectedMetadata();
      InjectionPoint ip2 = getInstanceByType(MethodInjectionPointBean.class).getInjectedBean().getInjectedMetadata();
      InjectionPoint ip3 = getInstanceByType(ConstructorInjectionPointBean.class).getInjectedBean().getInjectedMetadata();
      
      ip1 = (InjectionPoint) deserialize(serialize(ip1));
      ip2 = (InjectionPoint) deserialize(serialize(ip2));
      ip3 = (InjectionPoint) deserialize(serialize(ip3));
      
      assert ip1.getType().equals(BeanWithInjectionPointMetadata.class);
      assert ip2.getType().equals(BeanWithInjectionPointMetadata.class);
      assert ip3.getType().equals(BeanWithInjectionPointMetadata.class);
   }
   
   @Test(groups = { "injectionPoint" })
   @SpecAssertion(section = "5.5.7", id = "ea")
   public void testApiTypeInjectionPoint()
   {
      // Get an instance of the bean which has another bean injected into it
      FieldInjectionPointBean beanWithInjectedBean = getInstanceByType(FieldInjectionPointBean.class);
      BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
      assert beanWithInjectionPoint.getInjectedMetadata() != null;
      assert InjectionPoint.class.isAssignableFrom(beanWithInjectionPoint.getInjectedMetadata().getClass());
   }

   @Test(groups = { "injectionPoint" })
   @SpecAssertion(section = "5.5.7", id = "ea")
   public void testCurrentBinding()
   {
      // Get an instance of the bean which has another bean injected into it
      FieldInjectionPointBean beanWithInjectedBean = getInstanceByType(FieldInjectionPointBean.class);
      BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
      assert beanWithInjectionPoint.getInjectedMetadata() != null;
      assert beanWithInjectionPoint.getInjectedMetadata().getQualifiers().contains(new DefaultLiteral());
   }

   @Test(groups = { "injectionPoint" })
   @SpecAssertion(section = "5.5.7", id = "dca")
   public void testIsTransient()
   {
      FieldInjectionPointBean bean1 = getInstanceByType(FieldInjectionPointBean.class);
      TransientFieldInjectionPointBean bean2 = getInstanceByType(TransientFieldInjectionPointBean.class);
      InjectionPoint ip1 = bean1.getInjectedBean().getInjectedMetadata();
      InjectionPoint ip2 = bean2.getInjectedBean().getInjectedMetadata();
      assert !ip1.isTransient();
      assert ip2.isTransient();
   }

   @Test(groups = { "injectionPoint" })
   @SpecAssertion(section = "5.5.7", id="dba")
   public void testIsDelegate()
   {
      assert !getInstanceByType(FieldInjectionPointBean.class).getInjectedBean().getInjectedMetadata().isDelegate();

      Cat cat = getInstanceByType(Cat.class, new DefaultLiteral());
      assert cat.hello().equals("hello world!");
      assert cat.getBeanManager() != null;
      assert cat.getInjectionPoint() != null;
      assert cat.getInjectionPoint().isDelegate();
   }
}
