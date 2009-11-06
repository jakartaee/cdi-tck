package org.jboss.jsr299.tck.tests.implementation.disposal.method.definition.broken.multiParams;


import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DefinitionError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DefinitionError.class)
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="20091101")
public class MultipleDisposeParametersDefinitionTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.3.5", id = "a"),
      @SpecAssertion(section = "3.3.6", id = "ba")
   })
   public void testMultipleDisposeParameters()
   {
      assert false;
   }

}
