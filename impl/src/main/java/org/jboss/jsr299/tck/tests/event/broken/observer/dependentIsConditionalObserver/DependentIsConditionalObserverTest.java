package org.jboss.jsr299.tck.tests.event.broken.observer.dependentIsConditionalObserver;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DefinitionError;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DefinitionError.class)
@SpecVersion(spec="cdi", version="20091101")
public class DependentIsConditionalObserverTest extends AbstractJSR299Test
{
   // WBRI-315
   @Test(groups = { "events" })
   @SpecAssertion(section = "10.4.3", id = "b")
   public void testDependentBeanWithConditionalObserverMethodIsDefinitionError()
   {
      assert false;
   }
}
