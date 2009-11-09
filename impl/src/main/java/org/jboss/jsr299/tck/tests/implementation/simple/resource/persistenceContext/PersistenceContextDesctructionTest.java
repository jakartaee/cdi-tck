package org.jboss.jsr299.tck.tests.implementation.simple.resource.persistenceContext;

import javax.enterprise.context.spi.CreationalContext;
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
@SpecVersion(spec="cdi", version="20091101")
public class PersistenceContextDesctructionTest extends AbstractJSR299Test
{
   
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
   
}
