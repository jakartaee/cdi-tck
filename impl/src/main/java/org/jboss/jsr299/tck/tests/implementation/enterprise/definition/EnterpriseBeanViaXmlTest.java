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

package org.jboss.jsr299.tck.tests.implementation.enterprise.definition;

import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.ear.EjbJarXml;
import org.testng.annotations.Test;

/**
 * These tests are any that involve ejb-jar.xml resources.
 * 
 * @author David Allen
 */
@Artifact
@Packaging(PackagingType.EAR)
@IntegrationTest
@EjbJarXml("ejb-jar.xml")
@SpecVersion(spec="cdi", version="PFD2")
public class EnterpriseBeanViaXmlTest extends AbstractJSR299Test
{

   @Test(groups = { "enterpriseBeans", "ejbjarxml" })
   @SpecAssertions( { 
      @SpecAssertion(section = "3.1.1", id = "n"),
      @SpecAssertion(section = "3.2.2", id = "ba") 
   })
   public void testEjbDeclaredInXmlNotSimpleBean()
   {
      Bean<ElephantLocal> elephantBean = getBeans(ElephantLocal.class).iterator().next();
      // The interface is a known type but the class should not be
      assert elephantBean.getTypes().contains(ElephantLocal.class);
      assert !elephantBean.getTypes().contains(Elephant.class);
   }
   
}
