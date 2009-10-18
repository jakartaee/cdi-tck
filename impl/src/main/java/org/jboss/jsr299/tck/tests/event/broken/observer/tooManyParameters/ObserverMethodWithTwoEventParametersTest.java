package org.jboss.jsr299.tck.tests.event.broken.observer.tooManyParameters;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DefinitionError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DefinitionError.class)
@SpecVersion(spec="cdi", version="20091018")
public class ObserverMethodWithTwoEventParametersTest extends AbstractJSR299Test
{
   @Test(groups = { "events" })
   @SpecAssertions( { @SpecAssertion(section = "10.4.1", id = "a"), 
	   @SpecAssertion(section = "10.4.2", id = "b") })
   public void testObserverMethodMustHaveOnlyOneEventParameter()
   {
      assert false;
   }

}
