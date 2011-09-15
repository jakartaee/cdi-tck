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
package org.jboss.jsr299.tck.tests.lookup.dynamic.builtin;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
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

/**
 * Tests for built-in Instance.
 */
@SpecVersion(spec="cdi", version="20091101")
public class BuiltinInstanceTest extends AbstractJSR299Test {

    @Deployment
   public static WebArchive createTestArchive() 
	{
       return new WebArchiveBuilder()
           .withTestClassPackage(BuiltinInstanceTest.class)
           .build();
   }
    
	@Test
	@SpecAssertion(section = "5.6.2", id = "d")
	public void testScopeOfBuiltinInstance() 
	{
		Bean<Instance<Cow>> bean = getBeans(new TypeLiteral<Instance<Cow>>() {}).iterator().next();
		assert Dependent.class.equals(bean.getScope());
	}
	
	@Test
	@SpecAssertion(section = "5.6.2", id = "e")
	public void testNameOfBuiltinInstance() 
	{
		Bean<Instance<Cow>> bean = getBeans(new TypeLiteral<Instance<Cow>>() {}).iterator().next();
		assert bean.getName() == null;
	}
	
	@Test
	@SpecAssertions({
	   @SpecAssertion(section = "5.6.2", id = "a"),
	   @SpecAssertion(section = "5.6.2", id = "f")
   })
	public void testInstanceProvidedForEveryLegalBeanType() 
	{
	   Farm farm = getInstanceByType(Farm.class);
	   assert farm.getAnimal() != null;
	   assert farm.getAbstractAnimal() != null;
	   assert farm.getCow() != null;
	}
	
	@Test
	@SpecAssertion(section = "5.6.2", id = "g")
	public void testInstanceIsPassivationCapable() throws Exception 
	{
	   Field field = getInstanceByType(Field.class);
	   Object object = deserialize(serialize(field));
	   assert field.getInstance().get() instanceof Cow;
	   assert object instanceof Field;
	   Field field2 = (Field) object;
	   assert field2.getInstance().get() instanceof Cow;
	}
}
