package org.jboss.jsr299.tck.tests.lookup.injection.non.contextual;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
public class CreationalContextForNonContextualTest extends AbstractJSR299Test
{
   
   @Test
   @SpecAssertion(section = "11.3.3", id = "b")
   public void testCreationalContext()
   {
      assert getCurrentManager().createCreationalContext(null) != null;
   }

}
