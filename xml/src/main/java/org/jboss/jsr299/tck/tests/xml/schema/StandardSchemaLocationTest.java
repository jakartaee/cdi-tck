package org.jboss.jsr299.tck.tests.xml.schema;

import org.hibernate.tck.annotations.SpecAssertion;
import org.hibernate.tck.annotations.SpecAssertions;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@Resources({
   @Resource(source="schema.xsd", destination="WEB-INF/classes/org/jboss/jsr299/tck/tests/xml/schema/schema.xsd")
})
@BeansXml("standard-beans.xml")
public class StandardSchemaLocationTest extends AbstractJSR299Test
{
   @Test(groups = { "ri-broken", "xml" })
   @SpecAssertions({
      @SpecAssertion(section="9.3", id="b"),
      @SpecAssertion(section="9.3", id="c")
   })
   public void testStandardSchemaLocation()
   {
      Order order = getInstanceByType(Order.class);
      assert order.getShipToAddress() != null;
      assert order.getShipToAddress().equals("123 Main St., Anywhere, MA");
   }
}
