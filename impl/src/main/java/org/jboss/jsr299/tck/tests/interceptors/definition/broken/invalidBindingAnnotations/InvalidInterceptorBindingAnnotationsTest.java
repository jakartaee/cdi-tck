package org.jboss.jsr299.tck.tests.interceptors.definition.broken.invalidBindingAnnotations;

import org.jboss.jsr299.tck.DeploymentError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DeploymentError.class)
@SpecVersion(spec="cdi", version="PFD2")
@BeansXml("beans.xml")
public class InvalidInterceptorBindingAnnotationsTest
{
   @Test(groups = "ri-broken")
   @SpecAssertion(section = "9.5.2", id = "d")
   public void testInterceptorBindingsWithConflictingAnnotationMembersNotOk()
   {
      assert false;
   }
}
