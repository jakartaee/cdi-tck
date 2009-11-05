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
package org.jboss.jsr299.tck.tests.deployment.lifecycle;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.testng.annotations.Test;

/**
 * @author pmuir
 */
@Artifact
@Resources({
   @Resource(source="javax.enterprise.inject.spi.Extension.ExtensionsTest", destination="WEB-INF/classes/META-INF/services/javax.enterprise.inject.spi.Extension")
})
@IntegrationTest
@SpecVersion(spec="cdi", version="20091101")
public class ExtensionsTest extends AbstractJSR299Test
{
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section="11.5.1", id="a"),
      @SpecAssertion(section="12.2", id="b"),
      @SpecAssertion(section="12.2", id="c")
   })
   public void testBeforeBeanDiscoveryEventIsCalled()
   {
      assert BeforeBeanDiscoveryObserver.isObserved();
   }
   
   @Test
   @SpecAssertion(section="11.5.1", id="ab")
   public void testAddingBindingType()
   {
      assert BeforeBeanDiscoveryObserver.isObserved();
      assert getBeans(Alligator.class).size() == 0;
      assert getBeans(Alligator.class, new AnnotationLiteral<Tame>() {}).size() == 1;
   }
   
   @Test
   @SpecAssertion(section="11.5.1", id="ac")
   public void testAddingScopeType()
   {
      assert BeforeBeanDiscoveryObserver.isObserved();
      assert getBeans(RomanEmpire.class).size() == 1;
      Bean<RomanEmpire> bean = getBeans(RomanEmpire.class).iterator().next();
      assert bean.getScope().equals(EpochScoped.class);
   }

}
