package org.jboss.jsr299.tck.tests.lookup.manager.jndi;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.testng.annotations.Test;

@Artifact
@IntegrationTest
@SpecVersion(spec="cdi", version="20091101")
public class ManagerTest extends AbstractJSR299Test
{
   @Test(groups = { "manager", "ejb3", "integration", "jboss-as-broken" })
   @SpecAssertion(section = "11.3", id = "da")
   // Requires new injection framework carlo is working on I think PLM
   public void testManagerLookupInJndi() throws Exception
   {
      BeanManager beanManager = getInstanceByType(JndiBeanManagerInjected.class).getManagerFromJndi();
      assert beanManager != null;
      assert beanManager.equals(getCurrentManager());
   }
}
