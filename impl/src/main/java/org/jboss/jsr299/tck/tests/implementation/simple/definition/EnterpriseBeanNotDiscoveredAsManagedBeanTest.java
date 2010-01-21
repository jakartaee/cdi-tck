package org.jboss.jsr299.tck.tests.implementation.simple.definition;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.jsr299.Extension;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091101")
@Extension("javax.enterprise.inject.spi.Extension")
@IntegrationTest
public class EnterpriseBeanNotDiscoveredAsManagedBeanTest extends AbstractJSR299Test
{

   @Test
   @SpecAssertion(section="3.1.1", id="f")
   public void testClassesImplementingEnterpriseBeanInterfaceNotDiscoveredAsSimpleBean()
   {
      assert !EnterpriseBeanObserver.observedEnterpriseBean;
      assert EnterpriseBeanObserver.observedAnotherBean;
   }

}
