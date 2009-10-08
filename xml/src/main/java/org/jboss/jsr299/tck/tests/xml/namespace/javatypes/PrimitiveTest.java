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

package org.jboss.jsr299.tck.tests.xml.namespace.javatypes;

import javax.enterprise.inject.AnnotationLiteral;

import org.hibernate.tck.annotations.SpecAssertion;
import org.hibernate.tck.annotations.SpecAssertions;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

/**
 * Tests related to primitive Java types in XML.
 * 
 * @author David Allen
 *
 */
@Artifact
@BeansXml("primitive-beans.xml")
public class PrimitiveTest extends AbstractJSR299Test
{
   @Test(groups = { "broken", "xml" })
   @SpecAssertions({
      @SpecAssertion(section="9.2.1", id="e")
    })
    //TODO Test relies on section 9.5 behavior which is not supported
    //TODO Test requires bug WBRI-244 to be fixed
   public void testPrimitiveType() throws Exception
   {
      new RunInDependentContext()
      {

         @Override
         protected void execute() throws Exception
         {
            StringBuilder customerName = getInstanceByType(StringBuilder.class,new AnnotationLiteral<CurrentCustomerName>(){});
            StringBuilder customerAddress = getInstanceByType(StringBuilder.class,new AnnotationLiteral<CurrentCustomerAddress>(){});
            customerName.append("Customer");
            customerAddress.append("Address");
            ShipTo shipTo = getInstanceByType(ShipTo.class);
            assert shipTo.getName() != null;
            assert shipTo.getAddress() != null;
            assert shipTo.getName().equals("Customer");
            assert shipTo.getAddress().equals("Address");
         }
         
      }.run();
      
   }
}
