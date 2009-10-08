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

package org.jboss.jsr299.tck.tests.xml.resource.ejb;

import org.hibernate.tck.annotations.SpecAssertion;
import org.hibernate.tck.annotations.SpecAssertions;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.jboss.testharness.impl.packaging.ear.EjbJarXml;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

/**
 * Simple lifecycle test with XML declaring injection of EJB.
 * 
 * @author David Allen
 *
 */
@Artifact
@Packaging(PackagingType.EAR)
@IntegrationTest
@Resources({
   @Resource(source="web.xml", destination="WEB-INF/web.xml")
})
@BeansXml("beans.xml")
@EjbJarXml("ejb-jar.xml")
public class DeclarationOfEjbTest extends AbstractJSR299Test
{
   @Test(groups = { "xml" })
   @SpecAssertions( { 
      @SpecAssertion(section = "6.9", id = "i"), 
      @SpecAssertion(section = "3.6", id = "d"),
      @SpecAssertion(section = "3.6.1", id = "d"),
      @SpecAssertion(section = "3.6.1", id = "i"),
      @SpecAssertion(section = "11.2", id = "e"),
      @SpecAssertion(section = "11.2", id = "g")
   })
   public void testXMLDeclarationOfEjb()
   {
      RemoteEjbInterface remoteEjbInterface = getInstanceByType(RemoteEjbInterface.class);
      assert remoteEjbInterface.hello().equals("hi!");
   }
}
