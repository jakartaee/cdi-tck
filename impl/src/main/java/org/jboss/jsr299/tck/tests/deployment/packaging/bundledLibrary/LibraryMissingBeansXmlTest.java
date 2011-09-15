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

package org.jboss.jsr299.tck.tests.deployment.packaging.bundledLibrary;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.jsr299.tck.tests.deployment.packaging.bundledLibrary.libraryBeans.Bar;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests related to the final deployment phase of the lifecycle.
 * 
 * @author David Allen
 */
@SpecVersion(spec="cdi", version="20091101")
public class LibraryMissingBeansXmlTest extends AbstractJSR299Test
{
    
   @Deployment
   public static WebArchive createTestArchive() 
	{
       // We put Foo in test archive, but Bar goes in the library
       return new WebArchiveBuilder()
           .withTestClass(LibraryMissingBeansXmlTest.class)
           .withClasses(LibraryMissingBeansXmlTest.class, Foo.class)
           .withBeanLibrary(true, Bar.class)
           .build();
   }

   @Test(groups = {})
   @SpecAssertions({
      @SpecAssertion(section = "12.1", id="bbc")
   })
   public void test()
   {
      assert getCurrentManager().getBeans(Foo.class).size() == 1;
      assert getCurrentManager().getBeans(Bar.class).isEmpty();
   }
}
