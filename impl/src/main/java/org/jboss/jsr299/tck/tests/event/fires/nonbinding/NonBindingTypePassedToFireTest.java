package org.jboss.jsr299.tck.tests.event.fires.nonbinding;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class NonBindingTypePassedToFireTest extends AbstractJSR299Test
{
   @Test(groups = { "events" }, expectedExceptions = { IllegalArgumentException.class })
   @SpecAssertion(section = "11.3.9", id = "e")
   public void testExceptionThrownIfNonBindingTypePassedToFire() throws Exception
   {
      OwlFinch_Broken bean = getInstanceByType(OwlFinch_Broken.class);
      bean.methodThatFiresEvent();
   }
}
