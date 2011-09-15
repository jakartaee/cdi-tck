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
package org.jboss.jsr299.tck.tests.implementation.producer.field.definition.enterprise;

import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.EnterpriseArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec="cdi", version="20091101")
public class EnterpriseProducerFieldDefinitionTest extends AbstractJSR299Test
{
   
    @Deployment
    public static EnterpriseArchive createTestArchive() 
	{
        return new EnterpriseArchiveBuilder()
            .withTestClassPackage(EnterpriseProducerFieldDefinitionTest.class)
            .withBeansXml("beans.xml")
            .build();
    }
    
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.4", id = "ab"),
      @SpecAssertion(section = "3.4", id = "ca")
   })
   public void testStaticProducerField()
   {
      assert getInstanceByType(Egg.class, new AnnotationLiteral<Foo>() {}) != null;
      assert getInstanceByType(Egg.class, new AnnotationLiteral<Foo>() {}).getSize() == Chicken.SIZE;
   }
   
}
