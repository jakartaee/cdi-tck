package org.jboss.jsr299.tck.tests.implementation.simple.resource.persistenceContext;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.persistence.EntityManager;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

/**
 * Injection of persistence related objects.
 *
 * @author David Allen
 */
@Artifact
@Packaging(PackagingType.WAR)
@IntegrationTest
@Resource(source = "persistence.xml", destination = "WEB-INF/classes/META-INF/persistence.xml")
//@PersistenceXml("persistence.xml")
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="20091018")
public class PersistenceContextInjectionTest extends AbstractJSR299Test
{
   @Test(groups = { "beanLifecycle", "commonAnnotations", "integration" })
   @SpecAssertions( {
      @SpecAssertion(section = "3.5.1", id = "cc"),
      @SpecAssertion(section = "7.3.6", id = "lb"),
      @SpecAssertion(section = "7.3.6", id = "mc")
   })
   public void testInjectionOfPersistenceContext()
   {
      Bean<ManagedBean> managedBeanBean = getBeans(ManagedBean.class).iterator().next();
      CreationalContext<ManagedBean> managedBeanCc = getCurrentManager().createCreationalContext(managedBeanBean);
      ManagedBean managedBean = managedBeanBean.create(managedBeanCc);
      assert managedBean.getPersistenceContext() != null : "Persistence context was not injected into bean";
      assert managedBean.getPersistenceContext().isOpen() : "Persistence context not open injected into bean";
   }
   
   @Test(groups = { "beanLifecycle", "commonAnnotations", "integration" })
   @SpecAssertions( {
      @SpecAssertion(section = "3.5.1", id = "dd"),
      @SpecAssertion(section = "7.3.6", id = "lc"),
      @SpecAssertion(section = "7.3.6", id = "me")
   })
   public void testInjectionOfPersistenceUnit()
   {
      Bean<ManagedBean> managedBeanBean = getBeans(ManagedBean.class).iterator().next();
      CreationalContext<ManagedBean> managedBeanCc = getCurrentManager().createCreationalContext(managedBeanBean);
      ManagedBean managedBean = managedBeanBean.create(managedBeanCc);
      assert managedBean.getPersistenceUnit() != null : "Persistence unit was not injected into bean";
      assert managedBean.getPersistenceUnit().isOpen() : "Persistence unit not open injected into bean";
   }
   
   @Test(groups = { "beanLifecycle", "commonAnnotations", "integration" })
   @SpecAssertions( {
      @SpecAssertion(section = "7.3.6", id = "md")
   })
   public void testPassivationOfPersistenceContext() throws Exception
   {
      Bean<ManagedBean> managedBeanBean = getBeans(ManagedBean.class).iterator().next();
      CreationalContext<ManagedBean> managedBeanCc = getCurrentManager().createCreationalContext(managedBeanBean);
      ManagedBean managedBean = managedBeanBean.create(managedBeanCc);
      managedBean = (ManagedBean) deserialize(serialize(managedBean));
      assert managedBean.getPersistenceContext() != null : "Persistence context was not injected into bean";
      assert managedBean.getPersistenceContext().isOpen() : "Persistence context not open injected into bean";
   }
   
   @Test(groups = { "beanLifecycle", "commonAnnotations", "integration" })
   @SpecAssertions( {
      @SpecAssertion(section="7.3.6", id = "nb")
   })
   public void testDestructionOfPersistenceContext() throws Exception
   {
      Bean<ManagedBean> managedBean = getBeans(ManagedBean.class).iterator().next();
      CreationalContext<ManagedBean> creationalContext = getCurrentManager().createCreationalContext(managedBean);
      ManagedBean instance = managedBean.create(creationalContext);
      EntityManager em = instance.getPersistenceContext();
      assert em.isOpen();
      managedBean.destroy(instance, creationalContext);
      assert !em.isOpen();
   }
   
   @Test(groups = { "beanLifecycle", "commonAnnotations", "integration" })
   @SpecAssertions( {
      @SpecAssertion(section = "7.3.6", id = "lc"),
      @SpecAssertion(section = "7.3.6", id = "mf")
   })
   public void testPassivationOfPersistenceUnit() throws Exception
   {
      Bean<ManagedBean> managedBeanBean = getBeans(ManagedBean.class).iterator().next();
      CreationalContext<ManagedBean> managedBeanCc = getCurrentManager().createCreationalContext(managedBeanBean);
      ManagedBean managedBean = managedBeanBean.create(managedBeanCc);
      managedBean = (ManagedBean) deserialize(serialize(managedBean));
      assert managedBean.getPersistenceUnit() != null : "Persistence unit was not injected into bean";
      assert managedBean.getPersistenceUnit().isOpen() : "Persistence unit not open injected into bean";
   }
   
   @Test(groups = { "beanLifecycle", "commonAnnotations", "integration" })
   @SpecAssertions( {
      @SpecAssertion(section = "3.5.1", id = "hh")
   })
   public void testBeanTypesAndBindingTypesOfPersistenceContext()
   {
      Bean<EntityManager> managedBeanBean = getBeans(EntityManager.class, new AnnotationLiteral<Database>() {}).iterator().next();
      assert managedBeanBean.getTypes().size() == 2;
      assert managedBeanBean.getTypes().contains(Object.class);
      assert managedBeanBean.getTypes().contains(EntityManager.class);
      assert managedBeanBean.getQualifiers().size() == 2;
      assert annotationSetMatches(managedBeanBean.getQualifiers(), Any.class, Database.class);
   }
}
