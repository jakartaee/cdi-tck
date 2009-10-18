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
package org.jboss.jsr299.tck.tests.decorators.invocation;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

/**
 * @author pmuir
 *
 */
@Artifact
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="20091018")
@Packaging(PackagingType.EAR)
@IntegrationTest
public class EJBDecoratorInvocationTest extends AbstractJSR299Test
{
   
   @Test(groups="ri-broken")
   @SpecAssertions({
      @SpecAssertion(section="7.2", id="d")
   })
   public void testEJBDecoratorInvocation() {
       // testDecoratorInvocation tests decorators of normal beans called from an EJB
       // it doesn't test actual decoration of the EJB
       PigStyDecorator.reset();
       PigStyImpl.reset();
       getInstanceByType(PigSty.class).clean();
       assert PigStyDecorator.isDecoratorCalled();
       assert PigStyImpl.isBeanCalled();
   }

}
