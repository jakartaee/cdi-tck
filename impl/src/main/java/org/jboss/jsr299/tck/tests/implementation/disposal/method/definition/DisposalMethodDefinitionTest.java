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
package org.jboss.jsr299.tck.tests.implementation.disposal.method.definition;

import java.lang.annotation.Annotation;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="20091101")
public class DisposalMethodDefinitionTest extends AbstractJSR299Test
{
   private static final Annotation DEADLIEST_LITERAL = new AnnotationLiteral<Deadliest>() {};

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "2.3.5", id = "c"),
      @SpecAssertion(section = "3.3.4", id = "b"),
      @SpecAssertion(section = "3.3.4", id = "c"),
      @SpecAssertion(section = "3.3.4", id = "e"),
      @SpecAssertion(section = "3.3.5", id = "ba"),
      @SpecAssertion(section = "3.3.6", id = "a"),
      @SpecAssertion(section = "3.3.6", id = "b0"),
      @SpecAssertion(section = "3.3.7", id = "aa"),
      @SpecAssertion(section = "5.5.4", id = "b")
   })
   public void testBindingTypesAppliedToDisposalMethodParameters() throws Exception
   {
      assert !SpiderProducer.isTameSpiderDestroyed();
      assert !SpiderProducer.isDeadliestTarantulaDestroyed();
      Bean<Tarantula> tarantula = getBeans(Tarantula.class, DEADLIEST_LITERAL).iterator().next();
      CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantula);
      Tarantula instance = tarantula.create(creationalContext);
      tarantula.destroy(instance, creationalContext);
      assert SpiderProducer.isTameSpiderDestroyed();
      assert SpiderProducer.isDeadliestTarantulaDestroyed();
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.3.4", id = "aa"),
      @SpecAssertion(section = "3.3.5", id = "ba")
   })
   public void testDisposalMethodOnNonBean() throws Exception
   {
      Bean<WebSpider> webSpider = getBeans(WebSpider.class, DEADLIEST_LITERAL).iterator().next();
      CreationalContext<WebSpider> creationalContext = getCurrentManager().createCreationalContext(webSpider);
      WebSpider instance = getCurrentManager().getContext(webSpider.getScope()).get(webSpider);
      webSpider.destroy(instance, creationalContext);
      assert !DisposalNonBean.isWebSpiderdestroyed();
   }

   /**
    * In addition to the disposed parameter, a disposal method may declare
    * additional parameters, which may also specify bindings. The container
    * calls Manager.getInstanceToInject() to determine a value for each
    * parameter of a disposal method and calls the disposal method with those
    * parameter values
    * @throws Exception
    */
   @Test(groups = { "disposalMethod" })
   @SpecAssertions({
      @SpecAssertion(section = "3.3.6", id = "h"),
      @SpecAssertion(section = "3.10", id = "a")
   })
   public void testDisposalMethodParametersGetInjected() throws Exception
   {
      Bean<SandSpider> sandSpider = getBeans(SandSpider.class, DEADLIEST_LITERAL).iterator().next();
      CreationalContext<SandSpider> creationalContext = getCurrentManager().createCreationalContext(sandSpider);
      SandSpider instance = getCurrentManager().getContext(sandSpider.getScope()).get(sandSpider);
      sandSpider.destroy(instance, creationalContext);
      assert SpiderProducer.isDeadliestSandSpiderDestroyed();
   }

}
