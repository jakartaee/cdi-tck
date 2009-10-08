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
package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.TypeLiteral;
import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.jsr299.tck.literals.DefaultLiteral;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class ResolutionByTypeTest extends AbstractJSR299Test
{

   @Test(groups = "resolution")
   @SpecAssertion(section = "5.3", id = "lb")
   public void testDefaultBindingTypeAssumed() throws Exception
   {
      Set<Bean<Tuna>> possibleTargets = getBeans(Tuna.class);
      assert possibleTargets.size() == 1;
      assert possibleTargets.iterator().next().getTypes().contains(Tuna.class);
   }

   @Test(groups = "resolution")
   @SpecAssertion(section = "5.3", id = "hc")
   public void testResolveByType() throws Exception
   {

      assert getBeans(Tuna.class, new DefaultLiteral()).size() == 1;

      assert getBeans(Tuna.class).size() == 1;

      Set<Bean<Animal>> beans = getBeans(Animal.class, new AnnotationLiteral<FishILike>()
      {
      });
      assert beans.size() == 3;
      List<Class<? extends Animal>> classes = new ArrayList<Class<? extends Animal>>();
      for (Bean<Animal> bean : beans)
      {
         if (bean.getTypes().contains(Salmon.class))
         {
            classes.add(Salmon.class);
         }
         else if (bean.getTypes().contains(SeaBass.class))
         {
            classes.add(SeaBass.class);
         }
         else if (bean.getTypes().contains(Haddock.class))
         {
            classes.add(Haddock.class);
         }
      }
      assert classes.contains(Salmon.class);
      assert classes.contains(SeaBass.class);
      assert classes.contains(Haddock.class);
   }

   @Test(groups = "injection")
   @SpecAssertions( { 
      @SpecAssertion(section = "2.3.4", id = "b"),
      @SpecAssertion(section = "5.3", id = "lc"),
      @SpecAssertion(section = "2.3.3", id = "d"),
      @SpecAssertion(section = "5.3", id = "la"),
      @SpecAssertion(section = "5.3.6", id = "a"),
      @SpecAssertion(section = "5.3.6", id = "d") })
   public void testAllBindingTypesSpecifiedForResolutionMustAppearOnBean()
   {
      assert getBeans(Animal.class, new ChunkyLiteral(), new AnnotationLiteral<Whitefish>()
      {
      }).size() == 1;
      assert getBeans(Animal.class, new ChunkyLiteral(), new AnnotationLiteral<Whitefish>()
      {
      }).iterator().next().getTypes().contains(Cod.class);

      assert getBeans(ScottishFish.class, new AnnotationLiteral<Whitefish>()
      {
      }).size() == 2;
      List<Class<? extends Animal>> classes = new ArrayList<Class<? extends Animal>>();
      for (Bean<ScottishFish> bean : getBeans(ScottishFish.class, new AnnotationLiteral<Whitefish>()
      {
      }))
      {
         if (bean.getTypes().contains(Cod.class))
         {
            classes.add(Cod.class);
         }
         else if (bean.getTypes().contains(Sole.class))
         {
            classes.add(Sole.class);
         }
      }
      assert classes.contains(Cod.class);
      assert classes.contains(Sole.class);
      
   }

   @Test(groups = { "resolution" })
   @SpecAssertions( { 
      @SpecAssertion(section = "5.3", id = "ka")
   })
   public void testResolveByTypeWithTypeParameter() throws Exception
   {
      assert getBeans(new TypeLiteral<Farmer<ScottishFish>>(){}).size() == 1;
      assert getBeans(new TypeLiteral<Farmer<ScottishFish>>(){}
      ).iterator().next().getTypes().contains(ScottishFishFarmer.class);
   }

   @Test(groups = { "resolution", "producerMethod" })
   @SpecAssertions( {
   	@SpecAssertion(section = "5.3", id = "j"),
   	@SpecAssertion(section="2.2.1", id = "i") } )
   public void testResolveByTypeWithArray() throws Exception
   {
      assert getBeans(Spider[].class).size() == 1;
   }

   @Test(groups = { "resolution" })
   @SpecAssertions( { 
      @SpecAssertion(section = "5.3", id = "i"),
      @SpecAssertion(section = "5.3.4", id = "aa"),
      @SpecAssertion(section = "5.3.4", id = "ab"),
      @SpecAssertion(section = "5.3.6", id = "b"),
      @SpecAssertion(section = "5.3.6", id = "c") })
   public void testResolveByTypeWithPrimitives()
   {
      assert getBeans(Double.class, new AnyLiteral()).size() == 2;
      assert getBeans(double.class, new AnyLiteral()).size() == 2;

      Double min = getInstanceByType(Double.class, new AnnotationLiteral<Min>(){});
      double max = getInstanceByType(double.class, new AnnotationLiteral<Max>(){});

      assert min.equals(NumberProducer.min);
      assert NumberProducer.max.equals(max);
   }
   
   @Test(groups = "resolution")
   @SpecAssertions( {
      @SpecAssertion(section = "5.3", id = "ld"),
      @SpecAssertion(section = "5.3.5", id = "b")
   })
   public void testResolveByTypeWithNonBindingMembers() throws Exception
   {

      Set<Bean<Animal>> beans = getBeans(Animal.class, new ExpensiveLiteral()
      {
         public int cost()
         {
            return 60;
         }

         public boolean veryExpensive()
         {
            return true;
         }

      }, new AnnotationLiteral<Whitefish>(){});
      assert beans.size() == 2;

      Set<Type> classes = new HashSet<Type>();
      for (Bean<Animal> bean : beans)
      {
         classes.addAll(bean.getTypes());
      }
      assert classes.contains(Halibut.class);
      assert classes.contains(RoundWhitefish.class);
      assert !classes.contains(Sole.class);
   }

   @Test(groups = { "policy" })
   @SpecAssertion(section = "5.2", id = "a")
   public void testPolicyNotAvailableInNonDeploymentArchive() throws Exception
   {
      Set<Bean<Spider>> spiders = getBeans(Spider.class);
      Set<Type> types = new HashSet<Type>();
      for (Bean<Spider> spider : spiders)
      {
         types.addAll(spider.getTypes());
      }
      assert !types.contains(CrabSpider.class);
      assert !types.contains(DaddyLongLegs.class);

      assert getCurrentManager().getBeans("crabSpider").size() == 0;
   }
}
