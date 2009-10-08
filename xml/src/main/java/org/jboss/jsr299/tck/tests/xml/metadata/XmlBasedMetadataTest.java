package org.jboss.jsr299.tck.tests.xml.metadata;

import org.hibernate.tck.annotations.SpecAssertion;
import org.hibernate.tck.annotations.SpecAssertions;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.tests.xml.metadata.foo.Order;
import org.jboss.jsr299.tck.tests.xml.metadata.foo.another.AnotherOrder;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@Resources({
   @Resource(source="foo/schema.xsd", destination="WEB-INF/classes/org/jboss/jsr299/tck/tests/xml/metadata/schema.xsd")
})
@Classes({
   Order.class,
   AnotherOrder.class
})
@BeansXml("beans.xml")
public class XmlBasedMetadataTest extends AbstractJSR299Test
{
   @Test(groups = { "ri-broken", "xml" })
   @SpecAssertions({
      @SpecAssertion(section="9", id="b"),
      @SpecAssertion(section="9", id="c"),
      @SpecAssertion(section="9", id="d"),
      @SpecAssertion(section="9.1", id="a"),
      @SpecAssertion(section="9.1", id="b"),
      @SpecAssertion(section="9.1", id="c"),
      @SpecAssertion(section="9.1", id="d")
   })
   public void testXmlBasedMetadata()
   {
      assert getBeans(Order.class).size() == 1 : "There is no one or more than one registered beans with type '" + Order.class + "'";
      assert getBeans(AnotherOrder.class).size() == 1 : "There is no one or more than one registered beans with type '" + AnotherOrder.class + "'";
   }
}
