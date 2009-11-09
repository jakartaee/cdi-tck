package org.jboss.jsr299.tck.tests.context.passivating.broken.interceptorWithNonPassivatingInjectedField;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DeploymentError.class)
@SpecVersion(spec="cdi", version="20091101")
@BeansXml("beans.xml")
public class PassivationCapableBeanWithNonPassivatingInterceptorTest extends AbstractJSR299Test
{
   @Test(groups = { "contexts", "passivation", "ri-broken" })
   @SpecAssertion(section = "6.6.4", id = "aac")
   public void testPassivationCapableBeanWithNonPassivatingInterceptorFails()
   {
      assert false;
   }
}