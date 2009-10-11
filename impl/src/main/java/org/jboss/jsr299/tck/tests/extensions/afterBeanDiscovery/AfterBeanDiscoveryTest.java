package org.jboss.jsr299.tck.tests.extensions.afterBeanDiscovery;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.jsr299.Extension;
import org.testng.annotations.Test;

@Artifact
@IntegrationTest
@Extension("javax.enterprise.inject.spi.Extension")
@SpecVersion(spec="cdi", version="PFD2")
public class AfterBeanDiscoveryTest extends AbstractJSR299Test
{
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.5.2", id="db")
   })
   public void testBeanIsAdded()
   {
      assert getBeans(Cockatoo.class).size() == 1;
      assert getInstanceByType(Cockatoo.class).getName().equals("Billy");
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.5.2", id="da")
   })
   public void testProcessBeanIsFired()
   {
      assert AfterBeanDiscoveryObserver.isProcessBeanFiredForCockatooBean();
   }

}
