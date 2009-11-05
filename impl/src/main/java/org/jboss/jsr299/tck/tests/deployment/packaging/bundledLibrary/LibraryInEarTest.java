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

package org.jboss.jsr299.tck.tests.deployment.packaging.bundledLibrary;

import java.io.IOException;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactDescriptor;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.ResourceDescriptorImpl;
import org.jboss.testharness.impl.packaging.TCKArtifact;
import org.jboss.testharness.impl.packaging.jsr299.JSR299ArtifactDescriptor;
import org.testng.annotations.Test;

/**
 * Tests related to the final deployment phase of the lifecycle.
 * 
 * @author David Allen
 */
@Artifact(addCurrentPackage=false)
// We put Foo in the ejb jar, but Bar goes in the library
@Classes({LibraryInEarTest.class, Foo.class})
@Test
@SpecVersion(spec="cdi", version="20091101")
@IntegrationTest
@Packaging(PackagingType.EAR)
public class LibraryInEarTest extends AbstractJSR299Test
{
   
   @Override
   protected TCKArtifact postCreate(TCKArtifact artifact) throws IOException
   {
      ArtifactDescriptor library = new ArtifactDescriptor(LibraryInEarTest.class);
      library.getClasses().add(Bar.class);
      library.getResources().add(new ResourceDescriptorImpl(JSR299ArtifactDescriptor.BEANS_XML_DESTINATION, JSR299ArtifactDescriptor.STANDARD_BEANS_XML_FILE_NAME));
      artifact.getLibraries().add(new ResourceDescriptorImpl("cdi-tck-beans.jar", library.getJarAsStream()));
      return artifact;
   }

   @Test(groups = {"jboss-as-broken"})
   @SpecAssertions({
      @SpecAssertion(section = "12.1", id="bbb")
   })
   public void test()
   {
      assert getCurrentManager().getBeans(Foo.class).size() == 1;
      assert getCurrentManager().getBeans(Bar.class).size() == 1;
   }
}
