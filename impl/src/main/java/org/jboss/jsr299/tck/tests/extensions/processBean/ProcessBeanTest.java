package org.jboss.jsr299.tck.tests.extensions.processBean;

import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.jsr299.Extension;
import org.testng.annotations.Test;

/**
 * Producer extension tests.
 * 
 * @author David Allen
 *
 */
@Artifact
@Extension("javax.enterprise.inject.spi.Extension")
// Must be an integration test as it needs a resource copied to a folder
@IntegrationTest
@SpecVersion(spec="cdi", version="20091101")
public class ProcessBeanTest extends AbstractJSR299Test
{
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.5.8", id = "ba"),
      @SpecAssertion(section = "11.5.8", id = "eda"),
      @SpecAssertion(section = "11.5.8", id = "efa"),
      @SpecAssertion(section = "11.5.8", id = "fa"),
      @SpecAssertion(section = "11.5.8", id = "l"),
      @SpecAssertion(section = "12.3", id = "fa")
   })
   public void testProcessBeanEvent()
   {
      assert ProcessBeanObserver.getCatProcessManagedBean().getBean().getBeanClass().equals(Cat.class);
      assert ProcessBeanObserver.getCatProcessBeanCount() == 2;
      assert ProcessBeanObserver.getCatProcessManagedBean().getAnnotated() instanceof AnnotatedType<?>;
      assert ProcessBeanObserver.getCatProcessManagedBean().getAnnotatedBeanClass().getBaseType().equals(Cat.class);
   }
   
   @SpecAssertions({
      @SpecAssertion(section = "11.5.8", id = "eaa"),
      @SpecAssertion(section = "11.5.8", id = "edc"),
      @SpecAssertion(section = "11.5.8", id = "efc"),
      @SpecAssertion(section = "11.5.8", id = "fc"),
      @SpecAssertion(section = "11.5.8", id = "i"),
      @SpecAssertion(section = "11.5.8", id = "j"),
      @SpecAssertion(section = "12.3", id = "ha")
   })
   @Test
   public void testProcessProducerMethodEvent()
   {
      assert ProcessBeanObserver.getCowProcessProducerMethod().getBean().getTypes().contains(Cow.class);
      assert ProcessBeanObserver.getCowProcessProducerMethod().getBean().getBeanClass().equals(Cowshed.class);
      assert ProcessBeanObserver.getCowProcessProducerMethod().getAnnotatedProducerMethod().getBaseType().equals(Cow.class);
      assert ProcessBeanObserver.getCowProcessProducerMethod().getAnnotatedProducerMethod().getDeclaringType().getBaseType().equals(Cowshed.class);
      assert ProcessBeanObserver.getCowShedProcessBeanCount() == 2;
      assert ProcessBeanObserver.getCowProcessProducerMethod().getAnnotated() instanceof AnnotatedMethod<?>;
      assert ProcessBeanObserver.getCowProcessProducerMethod().getAnnotatedProducerMethod().getJavaMember().getName().equals("getDaisy");
      assert ProcessBeanObserver.getCowProcessProducerMethod().getAnnotatedProducerMethod().getJavaMember().getDeclaringClass().equals(Cowshed.class);
      assert ProcessBeanObserver.getCowProcessProducerMethod().getAnnotatedDisposedParameter().getDeclaringCallable().getJavaMember().getName().equals("disposeOfDaisy");
      assert ProcessBeanObserver.getCowProcessProducerMethod().getAnnotatedDisposedParameter().getDeclaringCallable().getJavaMember().getDeclaringClass().equals(Cowshed.class);
   }
   
   @SpecAssertions({
      @SpecAssertion(section = "11.5.8", id = "eb"),
      @SpecAssertion(section = "11.5.8", id = "edd"),
      @SpecAssertion(section = "11.5.8", id = "efd"),
      @SpecAssertion(section = "11.5.8", id = "fd"),
      @SpecAssertion(section = "11.5.8", id = "n"),
      @SpecAssertion(section = "12.3", id = "hb")
   })
   @Test
   public void testProcessProducerFieldEvent()
   {
      assert ProcessBeanObserver.getChickenProcessProducerField().getBean().getTypes().contains(Chicken.class);
      assert ProcessBeanObserver.getChickenProcessProducerField().getBean().getBeanClass().equals(ChickenHutch.class);
      assert ProcessBeanObserver.getChickenProcessProducerField().getAnnotatedProducerField().getBaseType().equals(Chicken.class);
      assert ProcessBeanObserver.getChickenProcessProducerField().getAnnotatedProducerField().getDeclaringType().getBaseType().equals(ChickenHutch.class);
      assert ProcessBeanObserver.getChickenHutchProcessBeanCount() == 2;
      assert ProcessBeanObserver.getChickenProcessProducerField().getAnnotated() instanceof AnnotatedField<?>;
      assert ProcessBeanObserver.getChickenProcessProducerField().getAnnotatedProducerField().getJavaMember().getName().equals("chicken");
      assert ProcessBeanObserver.getChickenProcessProducerField().getAnnotatedProducerField().getJavaMember().getDeclaringClass().equals(ChickenHutch.class);
   }
   
}
