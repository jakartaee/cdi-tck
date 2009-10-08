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

package org.jboss.jsr299.tck.tests.xml.schema;

import javax.inject.DefinitionException;

import org.hibernate.tck.annotations.SpecAssertion;
import org.hibernate.tck.annotations.SpecAssertions;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

/**
 * This test is designed to produce an XML validation failure so
 * that we can detect that the namespace was validated with
 * the XML parser and the corresponding schema per the spec.
 * 
 * @author David Allen
 *
 */
@Artifact
@Resources({
   @Resource(source="schema.xsd", destination="WEB-INF/classes/org/jboss/jsr299/tck/tests/xml/schema/schema.xsd")
})
@BeansXml("invalid-beans.xml")
@ExpectedDeploymentException(DefinitionException.class)
public class NamespacesValidatedTest extends AbstractJSR299Test
{
   @Test(groups = { "ri-broken", "xml" })
   @SpecAssertions({
      @SpecAssertion(section="9.3", id="a")
   })
   public void testValidationFailure()
   {
      assert false;
   }
}
