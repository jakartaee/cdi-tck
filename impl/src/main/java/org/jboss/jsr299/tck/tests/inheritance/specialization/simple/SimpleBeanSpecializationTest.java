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
package org.jboss.jsr299.tck.tests.inheritance.specialization.simple;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec="cdi", version="20091101")
public class SimpleBeanSpecializationTest extends AbstractJSR299Test
{

   private static Annotation LANDOWNER_LITERAL = new AnnotationLiteral<Landowner>() {};

   @Deployment
   public static WebArchive createTestArchive() 
	{
       return new WebArchiveBuilder()
           .withTestClassPackage(SimpleBeanSpecializationTest.class)
           .withBeansXml("beans.xml")
           .build();
   }
   
   @SuppressWarnings("unchecked")
   @Test
   @SpecAssertions( { 
      @SpecAssertion(section = "4.3.1", id = "ia"),
      @SpecAssertion(section = "4.3.1", id = "ib"),
      @SpecAssertion(section = "4.3.1", id = "j"), 
      @SpecAssertion(section = "3.1.4", id = "aa") 
   })
   public void testSpecializingBeanHasQualifiersOfSpecializedAndSpecializingBean()
   {
      assert getBeans(LazyFarmer.class, LANDOWNER_LITERAL).size() == 1;
      Bean<?> bean = getBeans(LazyFarmer.class, LANDOWNER_LITERAL).iterator().next();
      assert bean.getTypes().contains(Farmer.class);
      assert bean.getQualifiers().size() == 4;
      assert annotationSetMatches(bean.getQualifiers(), Landowner.class, Lazy.class, Any.class, Named.class);
   }

   @Test
   @SpecAssertions( { 
      @SpecAssertion(section = "4.3.1", id = "k"), 
      @SpecAssertion(section = "3.1.4", id = "ab") 
   })
   public void testSpecializingBeanHasNameOfSpecializedBean()
   {
      assert getBeans(LazyFarmer.class, LANDOWNER_LITERAL).size() == 1;
      assert "farmer".equals(getBeans(LazyFarmer.class, LANDOWNER_LITERAL).iterator().next().getName());
   }

   @Test
   @SpecAssertions( { 
     // @SpecAssertion(section = "4.3.2", id = "ab"), removed from spec
      @SpecAssertion(section="4.3", id="cb")
   })
   public void testProducerMethodOnSpecializedBeanCalledOnSpecializingBean()
   {
      assert getBeans(Waste.class).size() == 1;
      assert getInstanceByType(Waste.class).getFrom().equals(Office.class.getName());
   }
}
