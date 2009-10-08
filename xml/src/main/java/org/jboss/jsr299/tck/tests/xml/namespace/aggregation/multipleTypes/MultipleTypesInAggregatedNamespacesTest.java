package org.jboss.jsr299.tck.tests.xml.namespace.aggregation.multipleTypes;

import javax.inject.DefinitionException;

import org.hibernate.tck.annotations.SpecAssertion;
import org.hibernate.tck.annotations.SpecAssertions;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.tests.xml.namespace.aggregation.Cow;
import org.jboss.jsr299.tck.tests.xml.namespace.aggregation.foo.AnotherDeploymentType;
import org.jboss.jsr299.tck.tests.xml.namespace.aggregation.foo.TestDeploymentType;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@Resources({
   @Resource(source="namespace", destination="WEB-INF/classes/org/jboss/jsr299/tck/tests/xml/namespace/aggregation/multipleTypes/namespace")
})
@Classes({AnotherDeploymentType.class, TestDeploymentType.class, org.jboss.jsr299.tck.tests.xml.namespace.aggregation.foo.copy.AnotherDeploymentType.class})
@BeansXml("beans.xml")
@ExpectedDeploymentException(DefinitionException.class)
public class MultipleTypesInAggregatedNamespacesTest extends AbstractJSR299Test
{   
   @Test
   @SpecAssertions({
      @SpecAssertion(section="9.2", id="e")
   })
   public void testNamespaceAggregation()
   {
      assert getBeans(Cow.class).size() == 1;
   }
   
}
