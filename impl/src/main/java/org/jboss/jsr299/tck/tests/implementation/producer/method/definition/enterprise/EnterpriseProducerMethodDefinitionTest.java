package org.jboss.jsr299.tck.tests.implementation.producer.method.definition.enterprise;

import java.util.Set;

import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@IntegrationTest
@Artifact
@BeansXml("beans.xml")
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="PFD2")
public class EnterpriseProducerMethodDefinitionTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "4.2", id = "dd")
   public void testNonStaticProducerMethodInheritedBySpecializingSubclass()
   {
      assert getBeans(Egg.class, new AnnotationLiteral<Yummy>() {}).size() == 1;
      assert getInstanceByType(Egg.class,new AnnotationLiteral<Yummy>() {}).getMother().getClass().equals(AndalusianChicken.class);
   }
   
   @Test
   @SpecAssertion(section = "4.2", id = "dd")
   public void testNonStaticProducerMethodNotInherited()
   {
      assert getBeans(Apple.class, new AnnotationLiteral<Yummy>() {}).size() == 1;
      assert getInstanceByType(Apple.class,new AnnotationLiteral<Yummy>() {}).getTree().getClass().equals(AppleTree.class);      
   }
   
   @Test
   @SpecAssertion(section = "4.2", id = "dj")
   public void testNonStaticProducerMethodNotIndirectlyInherited()
   {
      Set<Bean<Pear>> beans = getBeans(Pear.class, new AnnotationLiteral<Yummy>() {});
      assert beans.size() == 2;
   }   
}

