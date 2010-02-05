/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.New;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.jsr299.tck.literals.DefaultLiteral;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091101")
@BeansXml("beans.xml")
public class NewSimpleBeanTest extends AbstractJSR299Test
{

   private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>()
   {
   };
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.12", id = "ya")
   })
   public void testNewBeanCreatedForFieldInjectionPoint()
   {
      assert getInstanceByType(Griffin.class).getList() instanceof ArrayList<?>;
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.12", id = "yc")
   })
   public void testNewBeanCreatedForInitializerInjectionPoint()
   {
      assert getInstanceByType(Dragon.class).getChildren() instanceof HashSet<?>;
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.12", id = "ye")
   })
   public void testNewBeanCreatedForConstructorInjectioAnPoint()
   {
      assert getInstanceByType(Hippogriff.class).getHomes() instanceof HashMap<?, ?>;
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.12", id = "yg")
   })
   public void testNewBeanCreatedForProducerMethod()
   {
      assert getInstanceByType(new TypeLiteral<Collection<Dragon>>() {}) instanceof ArrayList<?>;
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.12", id = "yi")
   })
   public void testNewBeanCreatedForObserverMethod()
   {
      getCurrentManager().fireEvent(new Griffin());
      assert getInstanceByType(Bestiary.class).getPossibleNames() instanceof TreeSet<?>;
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.12", id = "yk")
   })
   public void testNewBeanCreatedForDisposerMethod()
   {
      Bean<Collection<Dragon>> bean = getUniqueBean(new TypeLiteral<Collection<Dragon>>() {});
      CreationalContext<Collection<Dragon>> ctx = getCurrentManager().createCreationalContext(bean);
      Collection<Dragon> dragons = bean.create(ctx);
      bean.destroy(dragons, ctx);
      assert getInstanceByType(Bestiary.class).getKnightsWhichKilledTheDragons() instanceof LinkedHashSet<?>;
   }
   
   @Test(groups = { "new" })
   @SpecAssertions({
      @SpecAssertion(section = "3.12", id = "p")
   })
   public void testNewBeanIsDependentScoped()
   {
      Set<Bean<ExplicitContructorSimpleBean>> beans = getBeans(ExplicitContructorSimpleBean.class, ExplicitContructorSimpleBean.NEW);
      assert beans.size() == 1;
      Bean<ExplicitContructorSimpleBean> newSimpleBean = beans.iterator().next();
      assert Dependent.class.equals(newSimpleBean.getScope());
   }

   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "r")
   public void testNewBeanHasOnlyNewBinding()
   {
      Set<Bean<ExplicitContructorSimpleBean>> beans = getBeans(ExplicitContructorSimpleBean.class, ExplicitContructorSimpleBean.NEW);
      assert beans.size() == 1;
      Bean<ExplicitContructorSimpleBean> newSimpleBean = beans.iterator().next();
      assert newSimpleBean.getQualifiers().size() == 1;
      assert newSimpleBean.getQualifiers().iterator().next().annotationType().equals(New.class);
   }

   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "s")
   public void testNewBeanHasNoBeanELName()
   {
      Set<Bean<ExplicitContructorSimpleBean>> beans = getBeans(ExplicitContructorSimpleBean.class, ExplicitContructorSimpleBean.NEW);
      assert beans.size() == 1;
      Bean<ExplicitContructorSimpleBean> newSimpleBean = beans.iterator().next();
      assert newSimpleBean.getName() == null;
   }

   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "t")
   public void testNewBeanHasNoStereotypes()
   {
      Bean<Fox> foxBean = getBeans(Fox.class).iterator().next();
      assert foxBean.getScope().equals(RequestScoped.class);
      assert foxBean.getName().equals("fox");
      Bean<Fox> newFoxBean = getBeans(Fox.class, Fox.NEW).iterator().next();
      assert newFoxBean.getScope().equals(Dependent.class);
      assert newFoxBean.getName() == null;
   }

   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "u")
   public void testNewBeanHasNoObservers()
   {
      // As long as only one observer exists here, we know it is not from the @New bean
      assert getCurrentManager().resolveObserverMethods("event").size() == 1;
   }

   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "w")
   public void testNewBeanHasNoProducerFields() throws Exception
   {
      Fox fox = getInstanceByType(Fox.class);
      Fox newFox = getInstanceByType(Fox.class, Fox.NEW);
      newFox.setDen(new Den("NewFoxDen"));
      Den theOnlyDen = getInstanceByType(Den.class);
      assert theOnlyDen.getName().equals(fox.getDen().getName());
   }

   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "v")
   public void testNewBeanHasNoProducerMethods() throws Exception
   {
      Fox fox = getInstanceByType(Fox.class);
      Fox newFox = getInstanceByType(Fox.class, Fox.NEW);
      fox.setNextLitterSize(3);
      newFox.setNextLitterSize(5);
      Litter theOnlyLitter = getInstanceByType(Litter.class);
      assert theOnlyLitter.getQuantity() == fox.getNextLitterSize();
   }

   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "x")
   public void testNewBeanHasNoDisposerMethods() throws Exception
   {
      Fox fox = getInstanceByType(Fox.class);
      Fox newFox = getInstanceByType(Fox.class, Fox.NEW);
      Bean<Litter> litterBean = getBeans(Litter.class).iterator().next();
      CreationalContext<Litter> creationalContext = getCurrentManager().createCreationalContext(litterBean);
      Litter litter = getInstanceByType(Litter.class);
      litterBean.destroy(litter, creationalContext);
      assert fox.isLitterDisposed();
      assert !newFox.isLitterDisposed();
   }

   @Test
   @SpecAssertion(section = "3.12", id = "d")
   public void testForEachSimpleBeanANewBeanExists()
   {
      assert getBeans(Order.class).size() == 1;
      assert getUniqueBean(Order.class).getQualifiers().size() == 2;
      assert getUniqueBean(Order.class).getQualifiers().contains(new DefaultLiteral());

      assert getBeans(Order.class, Order.NEW).size() == 1;
      assert getUniqueBean(Order.class, Order.NEW).getQualifiers().size() == 1;
      assert getUniqueBean(Order.class, Order.NEW).getQualifiers().iterator().next().annotationType().equals(New.class);

      assert getBeans(Lion.class, TAME_LITERAL).size() == 1;
      assert getUniqueBean(Lion.class, TAME_LITERAL).getQualifiers().size() == 2;
      assert getUniqueBean(Lion.class, TAME_LITERAL).getQualifiers().contains(TAME_LITERAL);
      assert getUniqueBean(Lion.class, TAME_LITERAL).getQualifiers().contains(new AnyLiteral());

      assert getBeans(Lion.class, Lion.NEW).size() == 1;
      assert getUniqueBean(Lion.class, Lion.NEW).getQualifiers().size() == 1;
      assert annotationSetMatches(getUniqueBean(Lion.class, Lion.NEW).getQualifiers(), New.class);
   }
   
   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "d")
   public void testNewBeanHasSameBeanClass()
   {
      assert getUniqueBean(Order.class).getBeanClass().equals(Order.class);
      assert getUniqueBean(Order.class, Order.NEW).getBeanClass().equals(Order.class);
      
      assert getUniqueBean(Lion.class, TAME_LITERAL).getBeanClass().equals(Lion.class);
      assert getUniqueBean(Lion.class, Lion.NEW).getBeanClass().equals(Lion.class);
   }
   
   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "e")
   public void testNewBeanHasSameBeanTypes()
   {
      assert typeSetMatches(getUniqueBean(Order.class).getTypes(), Order.class, Serializable.class, Object.class);
      assert typeSetMatches(getUniqueBean(Order.class, Order.NEW).getTypes(), Order.class, Serializable.class, Object.class);
      
      assert typeSetMatches(getUniqueBean(Lion.class, TAME_LITERAL).getTypes(), Lion.class, Object.class);
      assert typeSetMatches(getUniqueBean(Lion.class, Lion.NEW).getTypes(), Lion.class, Object.class);
   }
   
   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "f")
   public void testNewBeanHasSameConstructor()
   {
      ExplicitContructorSimpleBean.setConstructorCalls(0);
      ExplicitContructorSimpleBean bean = getInstanceByType(ExplicitContructorSimpleBean.class);
      ExplicitContructorSimpleBean newBean = getInstanceByType(ExplicitContructorSimpleBean.class, ExplicitContructorSimpleBean.NEW);
      assert bean != newBean;
      assert ExplicitContructorSimpleBean.getConstructorCalls() == 2;
   }

   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "g")
   public void testNewBeanHasSameInitializers()
   {
      InitializerSimpleBean.setInitializerCalls(0);
      InitializerSimpleBean bean = getInstanceByType(InitializerSimpleBean.class);
      bean.businessMethod();  // Cause proxy to initialize the bean
      InitializerSimpleBean newBean = getInstanceByType(InitializerSimpleBean.class, InitializerSimpleBean.NEW);
      assert bean != newBean;
      assert InitializerSimpleBean.getInitializerCalls() == 2;
   }

   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "h")
   public void testNewBeanHasSameInjectedFields()
   {
      Bean<InitializerSimpleBean> simpleBean = getBeans(InitializerSimpleBean.class).iterator().next();
      Bean<InitializerSimpleBean> newSimpleBean = getBeans(InitializerSimpleBean.class, InitializerSimpleBean.NEW).iterator().next();
      assert !newSimpleBean.getInjectionPoints().isEmpty();
      assert simpleBean.getInjectionPoints().equals(newSimpleBean.getInjectionPoints());
   }
   
   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "i")
   public void testNewBeanHasSameInterceptorBindings()
   {
      assert getInstanceByType(Order.class).foo();
      assert getInstanceByType(Order.class, Order.NEW).foo();
   }
   
   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "xb")
   public void testNewBeanIsNotAlternative()
   {
      assert getUniqueBean(Tiger.class).isAlternative();
      assert !getUniqueBean(Tiger.class, Tiger.NEW).isAlternative();
   }
   
   @Test
   @SpecAssertion(section="3.12", id="z")
   public void testNewBeanWithNoMemberValue()
   {
      assert getInstanceByType(NewLionConsumer.class).getLion() instanceof Lion;
   }
}
