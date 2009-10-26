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
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

/**
 * @author pmuir
 *
 */
@Artifact
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="20091018")
public class DecoratorInvocationTest extends AbstractJSR299Test
{

   @Test
   @SpecAssertions({
      @SpecAssertion(section="8.5", id="a"),
      @SpecAssertion(section="8.5", id="c"),
      @SpecAssertion(section="8.5", id="b"),
      @SpecAssertion(section="8.1.3", id="d"),
      @SpecAssertion(section="8.1.2", id="f"),
      @SpecAssertion(section="8.1.2", id="b"),
      @SpecAssertion(section="7.2", id="b")
   })
   public void testDecoratorInvocation()
   {
      TimestampLogger.reset();
      MockLogger.reset();
      getInstanceByType(CowShed.class).milk();
      assert TimestampLogger.getMessage().equals(CowShed.MESSAGE);
      assert MockLogger.getMessage().equals(TimestampLogger.PREFIX + CowShed.MESSAGE);
      assert MockLogger.isInitializeCalled();
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section="8.2", id="b"),
      @SpecAssertion(section="8.5", id="d"),
      @SpecAssertion(section="8.5", id="e"),
      @SpecAssertion(section="8.5", id="f"),
      @SpecAssertion(section="8.4", id="a"),
      @SpecAssertion(section="8.1.3", id="d"),
      @SpecAssertion(section="8.1.2", id="f"),
      @SpecAssertion(section="8.1.2", id="b"),
      @SpecAssertion(section="7.2", id="kb")
   })
   public void testChainedDecoratorInvocation()
   {
      FooDecorator1.reset();
      FooDecorator2.reset();
      FooImpl.reset();
      getInstanceByType(CowShed.class).washDown();
      assert FooDecorator1.getMessage().equals(CowShed.MESSAGE);
      assert FooDecorator1.getInjectionPoint().getBean().getBeanClass().equals(CowShed.class);
      assert !FooDecorator1.getInjectionPoint().isDelegate();
      assert FooDecorator2.getMessage().equals(CowShed.MESSAGE + FooDecorator1.SUFFIX);
      assert FooDecorator2.getInjectionPoint().getBean().getBeanClass().equals(FooDecorator1.class);
      assert FooDecorator2.getInjectionPoint().isDelegate();
      assert FooImpl.getMessage().equals(CowShed.MESSAGE + FooDecorator1.SUFFIX + FooDecorator2.SUFFIX);
      assert FooImpl.getInjectionPoint().getBean().getBeanClass().equals(FooDecorator2.class);
      assert FooImpl.getInjectionPoint().isDelegate();
   }

}
