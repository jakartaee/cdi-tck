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
package org.jboss.jsr299.tck.tests.definition.qualifier.enterprise;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

/**
 * TODO This test specifically tests session beans even though the assertions are true
 * for all managed beans. So likely there should be a parallel set of tests for session
 * and other managed beans.
 * 
 */
@Artifact
@IntegrationTest
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="20091101")
public class EnterpriseQualifierDefinitionTest extends AbstractJSR299Test
{
   @SuppressWarnings("unchecked")
   @Test
   @SpecAssertion(section = "4.1", id = "al")
   public void testQualifierDeclaredInheritedIsInherited() throws Exception
   {
      Set<? extends Annotation> qualifiers = getBeans(BorderCollieLocal.class, new HairyQualifier(false)).iterator().next().getQualifiers();
      assert annotationSetMatches(qualifiers, Any.class, Hairy.class);
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "ala")
   public void testQualifierNotDeclaredInheritedIsNotInherited() throws Exception
   {      
      assert getBeans(TameSkinnyHairlessCatLocal.class, new SkinnyQualifier()).size() == 0;
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "ap")
   public void testQualifierDeclaredInheritedIsIndirectlyInherited()
   {
      Set<? extends Annotation> qualifiers = getBeans(EnglishBorderCollieLocal.class, new HairyQualifier(false)).iterator().next().getQualifiers();
      assert annotationSetMatches(qualifiers, Any.class, Hairy.class);
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "apa")
   public void testQualifierNotDeclaredInheritedIsNotIndirectlyInherited()
   {          
      Set<Bean<FamousCatLocal>> beans = getBeans(FamousCatLocal.class, new SkinnyQualifier());
      assert beans.size() == 0;
   }
}
