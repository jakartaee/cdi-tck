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
package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution;

import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091101")
@Packaging(PackagingType.EAR)
public class EnterpriseResolutionByTypeTest extends AbstractJSR299Test
{
   
   @Test
   @SpecAssertion(section = "2.2.2", id = "b")
   public void testBeanTypesOnSessionBean()
   {
      assert getBeans(CapercaillieLocal.class).size() == 1;
      assert getBeans(ScottishBirdLocal.class).isEmpty();
      Bean<CapercaillieLocal> bean = getUniqueBean(CapercaillieLocal.class);
      assert rawTypeSetMatches(bean.getTypes(), CapercaillieLocal.class, Object.class);
   }
   
}
