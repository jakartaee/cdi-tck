package org.jboss.jsr299.tck.tests.extensions.processBean;

import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.SessionBeanType;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
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
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="20091018")
public class ProcessSessionBeanTest extends AbstractJSR299Test
{
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.5.8", id = "c"),
      @SpecAssertion(section = "11.5.8", id = "edb"),
      @SpecAssertion(section = "11.5.8", id = "efb"),
      @SpecAssertion(section = "11.5.8", id = "fb"),
      @SpecAssertion(section = "11.5.8", id = "hb"),
      @SpecAssertion(section = "11.5.8", id = "hc"),
      @SpecAssertion(section = "11.5.8", id = "m"),
      @SpecAssertion(section = "11.5.8", id = "k"),
      @SpecAssertion(section = "12.3", id = "fb")
   })
   public void testProcessSessionBeanEvent()
   {
      assert ProcessBeanObserver.getElephantProcessSessionBean().getBean().getBeanClass().equals(Elephant.class);
      assert ProcessBeanObserver.getElephantProcessBeanCount() == 2;
      assert ProcessBeanObserver.getElephantProcessSessionBean().getEjbName().equals("Rosie");
      assert ProcessBeanObserver.getElephantProcessSessionBean().getSessionBeanType().equals(SessionBeanType.STATELESS);
      assert ProcessBeanObserver.getElephantProcessSessionBean().getAnnotated() instanceof AnnotatedType<?>;
      assert ProcessBeanObserver.getElephantProcessSessionBean().getAnnotatedBeanClass().getBaseType().equals(Elephant.class);
      assert ProcessBeanObserver.getElephantProcessSessionBean().getAnnotatedBeanClass().getBaseType().equals(Elephant.class);
   }
   
}
