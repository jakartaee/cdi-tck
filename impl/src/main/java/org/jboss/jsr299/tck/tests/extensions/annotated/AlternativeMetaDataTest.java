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

package org.jboss.jsr299.tck.tests.extensions.annotated;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.AnnotatedType;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

/**
 * Contains all the functioning tests for the extension which provides
 * alternative meta data sources.  Some of the assertions are really statements
 * of intent, but are tested here to make sure the container provides implementations
 * that meet that same intent as any third party extension would.
 * 
 * @author David Allen
 *
 */
@Artifact
@SpecVersion(spec="cdi", version="20091101")
public class AlternativeMetaDataTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "11.4", id = "c")
   public void testBaseType()
   {
      AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(DogHouse.class);
      assert annotatedType.getBaseType().equals(DogHouse.class);
   }

   @Test
   @SpecAssertion(section = "11.4", id = "d")
   public void testTypeClosure()
   {
      AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(ClassD.class);
      assert annotatedType.getTypeClosure().contains(Object.class);
      assert annotatedType.getTypeClosure().contains(InterfaceA.class);
      assert annotatedType.getTypeClosure().contains(InterfaceB.class);
      assert annotatedType.getTypeClosure().contains(AbstractC.class);
      assert annotatedType.getTypeClosure().contains(ClassD.class);
   }

   @Test
   @SpecAssertion(section = "11.4", id = "e")
   public void testGetAnnotation()
   {
      AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(ClassD.class);
      assert annotatedType.getAnnotation(RequestScoped.class) != null;
      assert annotatedType.getAnnotation(ApplicationScoped.class) == null;
   }

   @Test
   @SpecAssertion(section = "11.4", id = "f")
   public void testGetAnnotations()
   {
      AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(ClassD.class);
      assert annotatedType.getAnnotations().size() == 2;
      assert annotationSetMatches(annotatedType.getAnnotations(), RequestScoped.class, Tame.class);
   }

   @Test
   @SpecAssertion(section = "11.4", id = "g")
   public void testIsAnnotationPresent()
   {
      AnnotatedType<?> annotatedType = getCurrentManager().createAnnotatedType(ClassD.class);
      assert annotatedType.isAnnotationPresent(RequestScoped.class);
      assert !annotatedType.isAnnotationPresent(ApplicationScoped.class);
   }
}
