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
package org.jboss.jsr299.tck.tests.interceptors.definition.interceptorOrder;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091101")
@BeansXml("beans.xml")
public class InterceptorOrderTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "9.4", id = "b")
   })
   public void testInterceptorsCalledInOrderDefinedByBeansXml()
   {
      FirstInterceptor.calledFirst = false;
      SecondInterceptor.calledFirst = false;
      
      Foo foo = getInstanceByType(Foo.class);
      foo.bar();
      
      assert SecondInterceptor.calledFirst;
   }
   
   @Test
   @SpecAssertion(section = "9.4", id = "fa")
   public void testInterceptorsDeclaredUsingInterceptorsCalledBeforeInterceptorBinding()
   {
      TransactionalInterceptor.first = false;
      AnotherInterceptor.first = false;
      
      AccountTransaction transaction = getInstanceByType(AccountTransaction.class);
      transaction.execute();
      
      assert AnotherInterceptor.first;
   }
}
