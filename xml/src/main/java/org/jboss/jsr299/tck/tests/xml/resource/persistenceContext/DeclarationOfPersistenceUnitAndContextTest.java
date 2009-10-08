package org.jboss.jsr299.tck.tests.xml.resource.persistenceContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.tck.annotations.SpecAssertion;
import org.hibernate.tck.annotations.SpecAssertions;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@Packaging(PackagingType.WAR)
@IntegrationTest
@Resources({
   @Resource(source="persistence.xml", destination="WEB-INF/persistence.xml")
})
@BeansXml("beans.xml")
public class DeclarationOfPersistenceUnitAndContextTest extends AbstractJSR299Test
{
   @Test(groups = { "xml" })
   @SpecAssertions( { 
      @SpecAssertion(section = "6.9", id = "g"), 
      @SpecAssertion(section = "3.6", id = "b"),
      @SpecAssertion(section = "3.6.1", id = "b"),
      @SpecAssertion(section = "3.6.1", id = "g")
   })
   public void testXMLDeclarationOfPersistenceContext()
   {
      EntityManager entityManager = getInstanceByType(EntityManager.class);
      assert entityManager != null : "Persistence context was not injected into bean";
      assert entityManager.isOpen() : "persistence context not open injected into bean";
   }
   
   @Test(groups = { "xml" })
   @SpecAssertions( { 
      @SpecAssertion(section = "6.9", id = "h"), 
      @SpecAssertion(section = "3.6", id = "c"),
      @SpecAssertion(section = "3.6.1", id = "c"),
      @SpecAssertion(section = "3.6.1", id = "h")
   })
   public void testDeclarationOfPersistenceUnit()
   {
      EntityManagerFactory entityManagerFactory = getInstanceByType(EntityManagerFactory.class);
      assert entityManagerFactory != null : "Persistence unit was not injected into bean";
      assert entityManagerFactory.isOpen() : "persistence unit not open injected into bean";
   }
}
