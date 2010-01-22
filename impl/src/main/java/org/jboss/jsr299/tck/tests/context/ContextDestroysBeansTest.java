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
package org.jboss.jsr299.tck.tests.context;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.jsr299.Extension;
import org.testng.annotations.Test;

/**
 * 
 * @author Nicklas Karlsson
 * @author Pete Muir
 * @author David Allen
 */
@Artifact
@IntegrationTest
@SpecVersion(spec="cdi", version="20091101")
@Extension("javax.enterprise.inject.spi.Extension")
public class ContextDestroysBeansTest extends AbstractJSR299Test
{

   @Test(groups = { "contexts" })
   @SpecAssertions( {
      @SpecAssertion(section = "6.2", id = "p"),
      @SpecAssertion(section = "6.3", id = "d")
   })
   public void testContextDestroysBeansWhenDestroyed()
   {
      MyContextual bean = AfterBeanDiscoveryObserver.getBean();
      bean.setShouldReturnNullInstances(false);

      Context sessionContext = getCurrentManager().getContext(SessionScoped.class);
      CreationalContext<MySessionBean> creationalContext = getCurrentManager().createCreationalContext(bean);
      MySessionBean instance = sessionContext.get(bean, creationalContext);
      instance.ping();
      assert instance != null;
      assert bean.isCreateCalled();
      
      destroyContext(sessionContext);
      assert bean.isDestroyCalled();
   }
   
}