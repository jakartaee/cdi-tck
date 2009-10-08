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
package org.jboss.jsr299.tck.tests.lookup.byname;

import java.util.Set;

import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="PFD2")
public class ResolutionByNameTest extends AbstractJSR299Test
{

   @Test(groups = { "resolution"})
   @SpecAssertions({
      @SpecAssertion(section = "5.4.1", id = "ca"),
      @SpecAssertion(section = "11.3.5", id = "aa"),
      @SpecAssertion(section = "11.3.5", id = "b")
   })
   public void testAmbiguousELNamesResolved() throws Exception
   {
      // Cod, Plaice and AlaskaPlaice are named "whitefish" - Cod is a not-enabled policy, AlaskaPlaice specializes Plaice
      Set<Bean<?>> beans = getCurrentManager().getBeans("whitefish");
      assert beans.size() == 2;
      assert getCurrentManager().resolve(beans).getTypes().contains(AlaskaPlaice.class);
      // Both Salmon and Sole are named "fish" - Sole is an enabled policy
      beans = getCurrentManager().getBeans("fish");
      assert beans.size() == 2;
      assert getCurrentManager().resolve(beans).getTypes().contains(Sole.class);
   }
}
