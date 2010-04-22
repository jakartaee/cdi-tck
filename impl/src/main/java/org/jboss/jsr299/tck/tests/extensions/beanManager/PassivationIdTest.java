/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat, Inc. and/or its affiliates, and individual contributors
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

package org.jboss.jsr299.tck.tests.extensions.beanManager;

import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.jsr299.Extension;
import org.testng.annotations.Test;

/**
 * Tests retrieving beans with the BeanManager by the ID for a passivation-capable
 * bean.
 * 
 * @author David Allen
 *
 */
@Artifact
@IntegrationTest
@Extension("javax.enterprise.inject.spi.Extension")
@SpecVersion(spec="cdi", version="20091101")
public class PassivationIdTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "11.3.6", id = "a")
   public void testGetPassivationCapableBeanById()
   {
      Bean<?> passivatingBean = getCurrentManager().getPassivationCapableBean(CowBean.PASSIVATION_ID);
      assert passivatingBean.getBeanClass().equals(Cow.class);
   }
}
