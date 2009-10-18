package org.jboss.jsr299.tck.tests.definition.stereotype.defaultNamed;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091018")
public class DefaultNamedTest extends AbstractJSR299Test
{

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "2.7.1.3", id = "aaa"),
      @SpecAssertion(section = "2.5.2", id = "e")
   })
   public void testStereotypeWithEmptyNamed()
   {
      assert getBeans(FallowDeer.class).size() == 1;
      assert "fallowDeer".equals(getBeans(FallowDeer.class).iterator().next().getName());
   }
   
}
