package org.jboss.jsr299.tck.tests.xml.namespace.aggregation;


import org.hibernate.tck.annotations.SpecAssertion;
import org.hibernate.tck.annotations.SpecAssertions;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.tests.xml.namespace.aggregation.foo.AnotherDeploymentType;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@Resources({
   @Resource(source="namespace", destination="WEB-INF/classes/org/jboss/jsr299/tck/tests/xml/namespace/aggregation/namespace")
})
@Classes({AnotherDeploymentType.class})
@BeansXml("beans.xml")
public class NamespaceAggregationTest extends AbstractJSR299Test
{
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section="9.2", id="a"),
      @SpecAssertion(section="9.2", id="b"),
      @SpecAssertion(section="9.2", id="c"),
      @SpecAssertion(section="9.2", id="d")
   })
   public void testNamespaceAggregation()
   {
      assert getBeans(Cow.class).size() == 1;
   }
   
}
