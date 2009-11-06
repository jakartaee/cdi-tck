package org.jboss.jsr299.tck.tests.implementation.producer.field.definition.enterprise;

import javax.enterprise.util.AnnotationLiteral;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@IntegrationTest
@Packaging(PackagingType.EAR)
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="20091101")
public class EnterpriseProducerFieldDefinitionTest extends AbstractJSR299Test
{
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.4", id = "ab"),
      @SpecAssertion(section = "3.4", id = "ca")
   })
   public void testStaticProducerField()
   {
      assert getInstanceByType(Egg.class, new AnnotationLiteral<Foo>() {}) != null;
      assert getInstanceByType(Egg.class, new AnnotationLiteral<Foo>() {}).getSize() == Chicken.SIZE;
   }
   
   
   
}
