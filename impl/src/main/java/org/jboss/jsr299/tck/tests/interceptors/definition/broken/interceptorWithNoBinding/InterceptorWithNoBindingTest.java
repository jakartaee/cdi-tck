package org.jboss.jsr299.tck.tests.interceptors.definition.broken.interceptorWithNoBinding;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

/**
 * 
 * @author Shane Bryzak
 */
@Artifact
@ExpectedDeploymentException(DeploymentError.class)
@SpecVersion(spec="cdi", version="20091101")
public class InterceptorWithNoBindingTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "9.2", id = "c")
   public void testInterceptorWithNoBindingNotOk()
   {
      assert false;
   }
}
