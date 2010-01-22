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
package org.jboss.jsr299.tck.tests.implementation.producer.method.definition.enterprise;

import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@IntegrationTest
@Artifact
@BeansXml("beans.xml")
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="20091101")
public class EnterpriseProducerMethodDefinitionTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "4.2", id = "dd")
   public void testNonStaticProducerMethodInheritedBySpecializingSubclass()
   {
      assert getBeans(Egg.class, new AnnotationLiteral<Yummy>() {}).size() == 1;
      assert getInstanceByType(Egg.class,new AnnotationLiteral<Yummy>() {}).getMother() instanceof AndalusianChickenLocal;
   }
   
   @Test
   @SpecAssertion(section = "4.2", id = "dd")
   public void testNonStaticProducerMethodNotInherited()
   {
      assert getBeans(Apple.class, new AnnotationLiteral<Yummy>() {}).size() == 1;
      assert getInstanceByType(Apple.class,new AnnotationLiteral<Yummy>() {}).getTree() instanceof AppleTreeLocal;      
   }
   
   @Test
   @SpecAssertion(section = "4.2", id = "dj")
   public void testNonStaticProducerMethodNotIndirectlyInherited()
   {
      Set<Bean<Pear>> beans = getBeans(Pear.class, new AnnotationLiteral<Yummy>() {});
      assert beans.size() == 2;
   }   
}

