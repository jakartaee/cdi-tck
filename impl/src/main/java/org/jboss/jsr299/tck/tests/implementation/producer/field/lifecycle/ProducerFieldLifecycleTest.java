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
package org.jboss.jsr299.tck.tests.implementation.producer.field.lifecycle;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.IllegalProductException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec="cdi", version="20091101")
public class ProducerFieldLifecycleTest extends AbstractJSR299Test
{
   
    private AnnotationLiteral<Null>   NULL_LITERAL   = new AnnotationLiteral<Null>()   {};
    private AnnotationLiteral<Broken> BROKEN_LITERAL = new AnnotationLiteral<Broken>() {};
    
    @Deployment
    public static WebArchive createTestArchive() 
	{
       return new WebArchiveBuilder()
           .withTestClassPackage(ProducerFieldLifecycleTest.class)
           .withBeansXml("beans.xml")
           .build();
    }
   
   @Test(groups = { "producerField" })
   @SpecAssertions({
      @SpecAssertion(section = "3.4", id = "aa") // removed from spec
   })
   public void testProducerFieldNotAnotherBean()
   {
      assert getInstanceByType(BrownRecluse.class) != null;
   }

   @Test(groups = { "producerField" })
   @SpecAssertions({
      @SpecAssertion(section = "5.5.5", id = "a"),
      @SpecAssertion(section = "3.4", id = "b")
   })
   public void testProducerStaticFieldBean()
   {
      StaticTarantulaConsumer tarantulaConsumer = getInstanceByType(StaticTarantulaConsumer.class);
      assert tarantulaConsumer.getConsumedTarantula().equals(StaticTarantulaProducer.produceTarantula);
   }

   @Test(groups = { "producerField" })
   @SpecAssertions({
     @SpecAssertion(section = "5.5.5", id = "b"),
     @SpecAssertion(section = "7.3.5", id = "ga")
   })
   public void testProducerFieldBeanCreate() throws Exception
   {
      BlackWidowConsumer spiderConsumer = getInstanceByType(BlackWidowConsumer.class);
      assert spiderConsumer.getInjectedSpider().equals(BlackWidowProducer.blackWidow);
   }

   @Test(groups = { "producerField", "specialization" })
   @SpecAssertions({
      // @SpecAssertion(section = "4.3.2", id = "b"), removed
      @SpecAssertion(section="4.3", id="cb")
   })
   public void testSpecializedBeanAlwaysUsed() throws Exception
   {
      TarantulaConsumer spiderConsumer = getInstanceByType(TarantulaConsumer.class);
      assert spiderConsumer.getConsumedTarantula() != null;
      assert spiderConsumer.getConsumedTarantula() instanceof DefangedTarantula;
   }

   @Test(groups = { "producerField" })
   @SpecAssertions({
      @SpecAssertion(section = "3.4", id = "d"),
      @SpecAssertion(section = "7.3.5", id = "m")
    })    
   public void testProducerFieldReturnsNullIsDependent() throws Exception
   {
      NullSpiderConsumer consumerBean = getInstanceByType(NullSpiderConsumer.class);
      assert consumerBean.getInjectedSpider() == null;
   }
   
   @Test(groups = { "producerField" }, expectedExceptions = IllegalProductException.class)  
   @SpecAssertions({
     @SpecAssertion(section = "7.3.5", id = "n")
   })   
   public void testProducerFieldForNullValueNotDependent() throws Exception {
       Bean<BlackWidow> spiderBean = getBeans(BlackWidow.class, NULL_LITERAL, BROKEN_LITERAL).iterator().next();

       CreationalContext<BlackWidow> spiderContext = getCurrentManager().createCreationalContext(spiderBean);
       spiderBean.create(spiderContext);
             
       assert false;
   }

   @Test(groups = { "producerField" }, expectedExceptions = IllegalProductException.class)  
   @SpecAssertions({
     @SpecAssertion(section = "3.4", id = "e"),
     @SpecAssertion(section = "7.3.5", id = "n")
   })   
   public void testProducerFieldReturnsNullIsNotDependent() throws Exception
   {
      NullSpiderConsumerForBrokenProducer consumer = getInstanceByType(NullSpiderConsumerForBrokenProducer.class);
      // The injected spider is proxied since it is in the request scope.
      // So to actually create the bean instance, we need to invoke
      // some method on the proxy.
      if (consumer.getInjectedSpider() == null)
      {
         // No proxy in this impl, and no exception = fail
      }
      else
      {
         // Proxy is assumed, so invoke some method
         consumer.getInjectedSpider().bite();
      }
   }

}
