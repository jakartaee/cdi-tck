package org.jboss.jsr299.tck.tests.definition.stereotype.broken.withBindingType;


import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;

@Artifact
//@ExpectedDeploymentException(DefinitionError.class)
@SpecVersion(spec="cdi", version="20091018")
public class StereoTypeWithBindingTypesTest extends AbstractJSR299Test
{
   
   // @Test
   //@SpecAssertion(section = "2.7.1.3", id = "b")  now untestable
   public void testStereotypeWithBindingTypes()
   {
      assert false;
   }
   
}
