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
package org.jboss.jsr299.tck.tests.definition.name;

import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec="cdi", version="20091101")
public class NameDefinitionTest extends AbstractJSR299Test
{
    
    @Deployment
    public static WebArchive createTestArchive() 
	{
        return new WebArchiveBuilder()
            .withTestClassPackage(NameDefinitionTest.class)
            .build();
    }
   
   @Test 
   @SpecAssertions({
      @SpecAssertion(section="2.5.1", id = "a"),
      @SpecAssertion(section="2", id="e"),
      @SpecAssertion(section = "3.1.3", id = "bb")
   })
   public void testNonDefaultNamed()
   {
      assert getBeans(Moose.class).size() == 1;
      Bean<Moose> moose =getBeans(Moose.class).iterator().next(); 
      assert moose.getName().equals("aMoose");
   }
   
   @Test 
   @SpecAssertions({
      @SpecAssertion(section= "2.5.2", id = "a"),
      @SpecAssertion(section = "3.1.5", id = "a"),
      @SpecAssertion(section = "2.5.1", id = "d")
   })
   public void testDefaultNamed()
   {
      assert getBeans(Haddock.class).size() == 1; 
      Bean<Haddock> haddock = getBeans(Haddock.class).iterator().next();
      assert haddock.getName() != null;
      assert haddock.getName().equals("haddock");
   }
   
   @Test 
   @SpecAssertions({ 
      @SpecAssertion(section = "2.7", id = "a"),
      @SpecAssertion(section = "2.7.1.3", id = "aaa")
   })
   public void testStereotypeDefaultsName()
   {
      assert getBeans(RedSnapper.class).size() == 1; 
      Bean<RedSnapper> bean = getBeans(RedSnapper.class).iterator().next();
      assert bean.getName().equals("redSnapper");
   }
   
   @Test 
   @SpecAssertions({
      @SpecAssertion(section="2.5.3", id = "a"),
      @SpecAssertion(section="2", id="e")
   })
   public void testNotNamedInJava()
   {
      assert getBeans(SeaBass.class).size() == 1; 
      Bean<SeaBass> bean = getBeans(SeaBass.class).iterator().next();
      assert bean.getName() == null;
   }
   
   @Test @SpecAssertion(section="2.5.3", id = "a")
   public void testNotNamedInStereotype()
   {
      assert getBeans(Minnow.class).size() == 1; 
      Bean<Minnow> bean = getBeans(Minnow.class).iterator().next();
      assert bean.getName() == null;
   }
   
}
