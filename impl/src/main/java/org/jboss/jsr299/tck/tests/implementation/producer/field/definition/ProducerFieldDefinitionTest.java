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
package org.jboss.jsr299.tck.tests.implementation.producer.field.definition;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.DefaultLiteral;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="20091101")
public class ProducerFieldDefinitionTest extends AbstractJSR299Test
{   
   private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {};
   private static final Annotation PET_LITERAL = new AnnotationLiteral<Pet>() {};
   private static final Annotation FOO_LITERAL = new AnnotationLiteral<Foo>() {};
   private static final Annotation STATIC_LITERAL = new AnnotationLiteral<Static>() {};

   @Test(groups = "producerField")
   @SpecAssertion(section = "3.4", id = "fa")
   public void testParameterizedReturnType() throws Exception
   {
      FunnelWeaverSpiderConsumer spiderConsumer = getInstanceByType(FunnelWeaverSpiderConsumer.class);
      assert spiderConsumer != null;
      assert spiderConsumer.getInjectedSpider() != null;
      assert spiderConsumer.getInjectedSpider().equals(FunnelWeaverSpiderProducer.getSpider());
   }

   @Test(groups = { "producerField", "deployment" })
   @SpecAssertions({
      @SpecAssertion(section = "3.4", id = "j"),
      @SpecAssertion(section = "3.4", id = "c"),
      @SpecAssertion(section = "3.4.2", id ="a")
   })
   public void testBeanDeclaresMultipleProducerFields()
   {
      assert getBeans(Tarantula.class, TAME_LITERAL).size() == 1;
      assert getInstanceByType(WolfSpider.class,PET_LITERAL).equals(OtherSpiderProducer.WOLF_SPIDER) ;
      assert getBeans(BlackWidow.class, TAME_LITERAL).size() == 1;
      assert getInstanceByType(BlackWidow.class,TAME_LITERAL).equals(OtherSpiderProducer.BLACK_WIDOW) ;
   }

   @Test(groups = "producerField")
   @SpecAssertions({
     @SpecAssertion(section = "2.3.1", id = "aa")
   })
   public void testDefaultBindingType()
   {
      Set<Bean<Tarantula>> tarantulaBeans = getBeans(Tarantula.class);
      assert tarantulaBeans.size() == 2;
      assert tarantulaBeans.iterator().next().getQualifiers().contains(new DefaultLiteral());
   }

   @Test(groups = "producerField")
   @SpecAssertion(section = "3.4.1", id = "c")
   public void testApiTypeForClassReturn()
   {
      Set<Bean<Tarantula>> tarantulaBeans = getBeans(Tarantula.class, PET_LITERAL);
      assert tarantulaBeans.size() == 1;
      Bean<Tarantula> tarantulaBean = tarantulaBeans.iterator().next();
      assert tarantulaBean.getTypes().size() == 6;
      assert tarantulaBean.getTypes().contains(Tarantula.class);
      assert tarantulaBean.getTypes().contains(DeadlySpider.class);
      assert tarantulaBean.getTypes().contains(Spider.class);
      assert tarantulaBean.getTypes().contains(Animal.class);
      assert tarantulaBean.getTypes().contains(DeadlyAnimal.class);
      assert tarantulaBean.getTypes().contains(Object.class);
   }

   @Test(groups = { "producerField" })
   @SpecAssertion(section = "3.4.1", id = "a")
   public void testApiTypeForInterfaceReturn()
   {
      Set<Bean<Animal>> animalBeans = getBeans(Animal.class, new AnnotationLiteral<AsAnimal>() {});
      assert animalBeans.size() == 1;
      Bean<Animal> animalModel = animalBeans.iterator().next();
      assert animalModel.getTypes().size() == 2;
      assert animalModel.getTypes().contains(Animal.class);
      assert animalModel.getTypes().contains(Object.class);
   }

   @Test(groups = { "producerField" })
   @SpecAssertion(section = "3.4.1", id = "ba")
   public void testApiTypeForPrimitiveReturn()
   {
      Set<Bean<?>> beans = getCurrentManager().getBeans("SpiderSize");
      assert beans.size() == 1;
      Bean<?> intModel = beans.iterator().next();
      assert intModel.getTypes().size() == 2;
      assert intModel.getTypes().contains(int.class);
      assert intModel.getTypes().contains(Object.class);
   }

   @Test(groups = { "producerField" })
   @SpecAssertions({
     @SpecAssertion(section = "3.4.1", id = "bb"),
     @SpecAssertion(section = "2.2.1", id = "i")
   })
   public void testApiTypeForArrayTypeReturn()
   {
      Set<Bean<Spider[]>> spidersBeans = getBeans(Spider[].class);
      assert spidersBeans.size() == 1;
      Bean<Spider[]> spidersModel = spidersBeans.iterator().next();
      assert spidersModel.getTypes().size() == 2;
      assert spidersModel.getTypes().contains(Spider[].class);
      assert spidersModel.getTypes().contains(Object.class);
   }

   @Test(groups = "producerField")
   @SpecAssertions({
      @SpecAssertion(section = "3.4.2", id = "f"),
      @SpecAssertion(section="2.3.3", id="c")
   })
   public void testBindingType()
   {
      Set<Bean<Tarantula>> tarantulaBeans = getBeans(Tarantula.class, TAME_LITERAL);
      assert tarantulaBeans.size() == 1;
      Bean<Tarantula> tarantulaModel = tarantulaBeans.iterator().next();
      assert tarantulaModel.getQualifiers().size() == 3;
      assert tarantulaModel.getQualifiers().contains(TAME_LITERAL);
   }

   @Test(groups = "producerField")
   @SpecAssertion(section = "3.4.2", id = "b")
   public void testScopeType()
   {
      Set<Bean<Tarantula>> tarantulaBeans = getBeans(Tarantula.class, TAME_LITERAL, FOO_LITERAL);
      assert !tarantulaBeans.isEmpty();
      Bean<Tarantula> tarantulaModel = tarantulaBeans.iterator().next();
      assert tarantulaModel.getScope().equals(RequestScoped.class);
   }

   @Test(groups = "producerField")
   @SpecAssertions({
      @SpecAssertion(section = "3.4.2", id = "c"),
      @SpecAssertion(section = "2.5.1", id = "c")
   })
   public void testNamedField()
   {
      Set<Bean<?>> beans = getCurrentManager().getBeans("blackWidow");
      assert beans.size() == 1;

      @SuppressWarnings("unchecked")
      Bean<BlackWidow> blackWidowModel = (Bean<BlackWidow>) beans.iterator().next();
      assert blackWidowModel.getName().equals("blackWidow");
   }
   
   @Test(groups = { "producerField" })
   @SpecAssertions({
     @SpecAssertion(section = "2.5.2", id = "c"),
     @SpecAssertion(section = "2.5.3", id = "a"),
     @SpecAssertion(section = "3.4.3", id = "a"),
     @SpecAssertion(section = "2.5.1", id = "d")
   })
   public void testDefaultNamedField()
   {
      Set<Bean<Tarantula>> tarantulaBeans = getBeans(Tarantula.class, STATIC_LITERAL);
      assert tarantulaBeans.size() == 1;
      Bean<Tarantula> tarantulaModel = tarantulaBeans.iterator().next();
      assert tarantulaModel.getName().equals("produceTarantula");
   }

   // review 2.2
   @Test(groups = "producerField")
   @SpecAssertions({
     @SpecAssertion(section = "2.7.2", id = "c"),
     @SpecAssertion(section = "3.4.2", id = "e")
   })
   public void testStereotype()
   {
      Set<Bean<Tarantula>> tarantulaBeans = getBeans(Tarantula.class, STATIC_LITERAL);
      assert !tarantulaBeans.isEmpty();
      Bean<Tarantula> tarantulaModel = tarantulaBeans.iterator().next();
      assert tarantulaModel.getScope().equals(RequestScoped.class);
   }

   @Test(groups = "producerField")
   @SpecAssertion(section = "4.2", id = "ea")
   public void testNonStaticProducerFieldNotInherited()
   {
      assert !(getInstanceByType(Egg.class,FOO_LITERAL).getMother() instanceof InfertileChicken);
   }

   @Test(groups = "producerField")
   @SpecAssertion(section = "4.2", id = "ec")
   public void testNonStaticProducerFieldNotIndirectlyInherited()
   {
      assert !(getInstanceByType(Egg.class,FOO_LITERAL).getMother() instanceof LameInfertileChicken);
   }
   
   @SuppressWarnings("serial")
   @Test(groups = "producerField")
   @SpecAssertion(section = "3.4", id = "fb")
   public void testProducerFieldWithTypeVariable()
   {
      assert (getInstanceByType(new TypeLiteral<List<Spider>>(){})) != null;
   }
}