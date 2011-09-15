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
package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution.parameterized;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec="cdi", version="20091101")
public class AssignabilityOfRawAndParameterizedTypesTest extends AbstractJSR299Test
{
    
    @Deployment
    public static WebArchive createTestArchive() 
	{
        return new WebArchiveBuilder()
            .withTestClassPackage(AssignabilityOfRawAndParameterizedTypesTest.class)
            .build();
    }

   @Test(groups = { "resolution" })
   @SpecAssertions({
      @SpecAssertion(section = "5.2", id = "kb"),
      @SpecAssertion(section = "2.2.1", id = "f"),
      @SpecAssertion(section = "2.2.1", id = "g")
   })
   public void testAssignabilityToRawType()
   {
      Set<Bean<Dao>> beans = getBeans(Dao.class);
      assert getBeans(Dao.class).size() == 4; // Dao, DaoProducer.getDao(), DaoProducer.getRawDao and ObjectDao
   }

   @Test(groups= {"resolution"})
   @SpecAssertion(section = "5.2.3", id = "ba")
   public void testAssignabilityOfParameterizedTypeWithActualTypesToParameterizedTypeWithActualTypes()
   {
      assert getBeans(new TypeLiteral<Map<Integer, Integer>>(){}).size() == 2;
      assert getBeans(new TypeLiteral<HashMap<Integer, Integer>>(){}).iterator().next().getTypes().contains(IntegerHashMap.class);
   }

   @Test(groups = { "resolution"})
   @SpecAssertions({
      @SpecAssertion(section = "5.2.3", id = "c")})
   public void testAssignabilityOfParameterizedTypeWithActualTypesToParameterizedTypeWithWildcards()
   {
      assert getBeans(new TypeLiteral<HashMap<? extends Number, ? super Integer>>() {}).size() == 1;
      assert getBeans(new TypeLiteral<HashMap<? extends Number, ? super Integer>>() {}).iterator().next().getTypes().contains(IntegerHashMap.class);
   }
   
   @Test(groups = { "resolution"})
   @SpecAssertions({
      @SpecAssertion(section = "5.2.2", id = "b")
   })
   public void testAssignabilityOfParameterizedTypeWithActualTypesToParameterizedTypeWithWildcardsAtInjectionPoint()
   {
      assert getInstanceByType(InjectedBean.class).getMap() instanceof IntegerHashMap;
   }

   @Test(groups = { "resolution" })
   @SpecAssertion(section = "5.2.3", id = "da")
   public void testAssignabilityOfParameterizedTypeWithTypeVariablesToParameterizedTypeWithWildcards()
   {
      Set<Bean<Result<? extends Throwable, ? super Exception>>> beans = getBeans(new TypeLiteral<Result<? extends Throwable, ? super Exception>>(){});
      assert beans.size() == 1;
      assert rawTypeSetMatches(beans.iterator().next().getTypes(), Result.class, Object.class);
   }
   
   @Test(groups = { "resolution" })
   @SpecAssertion(section = "5.2.3", id = "db")
   public void testAssignabilityOfParameterizedTypeWithTypeVariablesToParameterizedTypeWithWildcards2()
   {
      Set<Bean<Result<? extends Exception, ? super Exception>>> beans = getBeans(new TypeLiteral<Result<? extends Exception, ? super Exception>>(){});
      assert beans.size() == 1;
      assert rawTypeSetMatches(beans.iterator().next().getTypes(), Result.class, Object.class);
   }

   @Test(groups = { "resolution" })
   @SpecAssertion(section = "5.2.3", id = "e")
   public void testAssignabilityOfParameterizedTypeWithTypeVariablesToParameterizedTypeWithActualTypes()
   {
      Set<Bean<Result<Exception, Exception>>> beans = getBeans(new TypeLiteral<Result<Exception, Exception>>(){});
      assert beans.size() == 1;
      assert rawTypeSetMatches(beans.iterator().next().getTypes(), Result.class, Object.class);
   }

   @Test(groups = { "resolution" })
   @SpecAssertion(section = "5.2.3", id = "f")
   public <T1 extends Exception, T2 extends Exception> void testAssignabilityOfParameterizedTypeWithTypeVariablesToParameterizedTypeTypeVariable()
   {
      Set<Bean<Result<T1, T2>>> beans = getBeans(new TypeLiteral<Result<T1, T2>>(){});
      assert beans.size() == 1;
      assert rawTypeSetMatches(beans.iterator().next().getTypes(), Result.class, Object.class);
   }

}
