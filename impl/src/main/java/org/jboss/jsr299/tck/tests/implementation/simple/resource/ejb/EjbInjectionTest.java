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

package org.jboss.jsr299.tck.tests.implementation.simple.resource.ejb;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

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
 * EJB injection tests for simple beans.
 * 
 * @author David Allen
 */
@Artifact
@Packaging(PackagingType.EAR)
@IntegrationTest
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="20091101")
public class EjbInjectionTest extends AbstractJSR299Test
{
   
   @Test(groups = { "beanLifecycle", "commonAnnotations", "integration" })
   @SpecAssertions({
      @SpecAssertion(section = "3.5.1", id = "ee"),
      @SpecAssertion(section = "7.3.6", id = "ld"),
      @SpecAssertion(section = "7.3.6", id = "mg")
   })
   public void testInjectionOfEjbs()
   {
      Bean<ManagedBean> managedBean = getBeans(ManagedBean.class).iterator().next();
      CreationalContext<ManagedBean> creationalContext = getCurrentManager().createCreationalContext(managedBean);
      ManagedBean instance = managedBean.create(creationalContext);
      assert instance.getMyEjb() != null : "EJB reference was not produced and injected into bean";
      assert instance.getMyEjb().knockKnock().equals("We're home");
   }
   
   @Test(groups = { "beanLifecycle", "commonAnnotations", "integration" })
   @SpecAssertions({
      @SpecAssertion(section = "7.3.6", id = "mh")
   })
   public void testPassivationOfEjbs() throws Exception
   {
      Bean<ManagedBean> managedBean = getBeans(ManagedBean.class).iterator().next();
      CreationalContext<ManagedBean> creationalContext = getCurrentManager().createCreationalContext(managedBean);
      ManagedBean instance = managedBean.create(creationalContext);
      instance = (ManagedBean) deserialize(serialize(instance));
      assert instance.getMyEjb() != null : "EJB reference was not produced and injected into bean";
      assert instance.getMyEjb().knockKnock().equals("We're home");
   }

}
