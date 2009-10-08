package org.jboss.jsr299.tck.tests.xml.resource.resource;

import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.spi.BeanManager;

import org.hibernate.tck.annotations.SpecAssertion;
import org.hibernate.tck.annotations.SpecAssertions;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@Packaging(PackagingType.WAR)
@IntegrationTest
@BeansXml("beans.xml")
public class DeclarationOfResourceTest extends AbstractJSR299Test
{
   @Test(groups = { "xml", "broken" })
   @SpecAssertions( { 
      @SpecAssertion(section = "6.9", id = "f"), 
      @SpecAssertion(section = "3.6", id = "a"),
      @SpecAssertion(section = "3.6.1", id = "a"),
      @SpecAssertion(section = "3.6.1", id = "f"),
      @SpecAssertion(section = "3.6.1", id = "l")
   })
   public void testXMLDeclarationOfResource()
   {
      BeanManager beanManager = getInstanceByType(BeanManager.class,new AnnotationLiteral<Another>() {});
      assert beanManager != null : "@Another Manager not found";
      assert beanManager.equals(getCurrentManager()): "Wrong manager found";
   }
}
