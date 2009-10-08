package org.jboss.jsr299.tck.tests.implementation.disposal.method.definition;

import java.lang.annotation.Annotation;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.AnnotationLiteral;
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
public class DisposalMethodDefinitionTest extends AbstractJSR299Test
{
   private static final Annotation DEADLIEST_LITERAL = new AnnotationLiteral<Deadliest>() {};

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "2.3.5", id = "c"),
      @SpecAssertion(section = "3.3.5", id = "b"),
      @SpecAssertion(section = "3.3.5", id = "c"),
      @SpecAssertion(section = "3.3.5", id = "e"),
      @SpecAssertion(section = "3.3.6", id = "ba"),
      @SpecAssertion(section = "3.3.7", id = "a"),
      @SpecAssertion(section = "3.3.7", id = "b0"),
      @SpecAssertion(section = "3.3.9", id = "aa"),
      @SpecAssertion(section = "5.6.6", id = "b")
   })
   public void testBindingTypesAppliedToDisposalMethodParameters() throws Exception
   {
      Bean<Tarantula> tarantula = getBeans(Tarantula.class, DEADLIEST_LITERAL).iterator().next();
      CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantula);
      Tarantula instance = tarantula.create(creationalContext);
      tarantula.destroy(instance, creationalContext);
      assert SpiderProducer.isTameSpiderDestroyed();
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.3.5", id = "aa"),
      @SpecAssertion(section = "3.3.6", id = "ba")
   })
   public void testDisposalMethodOnNonBean() throws Exception
   {
      Bean<Tarantula> tarantula = getBeans(Tarantula.class, DEADLIEST_LITERAL).iterator().next();
      CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantula);
      Tarantula instance = getCurrentManager().getContext(tarantula.getScope()).get(tarantula);
      tarantula.destroy(instance, creationalContext);
      assert !DisposalNonBean.isSpiderDestroyed();
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
      @SpecAssertion(section = "3.3.8", id = "a"),
      @SpecAssertion(section = "3.10", id = "a")
   })
   public void testDisposalMethodParametersGetInjected() throws Exception
   {
      Bean<Tarantula> tarantula = getBeans(Tarantula.class, DEADLIEST_LITERAL).iterator().next();
      CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantula);
      Tarantula instance = getCurrentManager().getContext(tarantula.getScope()).get(tarantula);
      tarantula.destroy(instance, creationalContext);
      assert SpiderProducer.isDeadliestSpiderDestroyed();
   }

}
