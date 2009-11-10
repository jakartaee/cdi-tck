package org.jboss.jsr299.tck.tests.interceptors.definition.broken.nonInterceptorClassInBeansXml;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentFailure;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DeploymentFailure.class)
@SpecVersion(spec="cdi", version="20091101")
@BeansXml("beans.xml")
public class NonInterceptorClassInBeansXmlTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "9.4", id = "cab")
   public void testNonInterceptorClassInBeansXmlNotOk()
   {
      assert false;
   }
}
