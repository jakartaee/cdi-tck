package org.jboss.jsr299.tck.tests.decorators.definition.broken.decoratorListedTwiceInBeansXml;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

/**
 * 
 * @author Shane Bryzak
 */
@Artifact
@ExpectedDeploymentException(DeploymentError.class)
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="20091018")
public class DecoratorListedTwiceInBeansXmlTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section="8.2", id="bc")
   public void testDecoratorListedTwiceInBeansXmlNotOK()
   {
      assert false;
   }
}
