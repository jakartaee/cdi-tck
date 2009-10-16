package org.jboss.jsr299.tck.tests.implementation.enterprise.lifecycle;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

/**
 * Sections
 * 
 * 6.5. Lifecycle of stateful session beans
 * 6.6. Lifecycle of stateless session and singleton beans
 * 6.11. Lifecycle of EJBs
 * 
 * Mostly overlapping with other tests...
 * 
 * @author Nicklas Karlsson
 * @author David Allen
 */
@Artifact
@Packaging(PackagingType.EAR)
@IntegrationTest
@SpecVersion(spec="cdi", version="PFD2")
public class EnterpriseBeanLifecycleTest extends AbstractJSR299Test
{

   @Test(groups = {"enterpriseBeans", "clientProxy", "lifecycle", "integration" })
   @SpecAssertions( {
      @SpecAssertion(section = "7.3.2", id = "aa"),
      @SpecAssertion(section = "7.3.2", id = "bb"),
      //@SpecAssertion(section = "7.3.3", id = "b"),
      @SpecAssertion(section = "6.5.3", id = "b"),
      @SpecAssertion(section = "12.1", id="bba")
   })
   public void testCreateSFSB() throws Exception
   {
      GrossStadt frankfurt = getInstanceByType(GrossStadt.class);
      Bean<KleinStadt> stadtBean = getBeans(KleinStadt.class).iterator().next();
      assert stadtBean != null : "Expected a bean for stateful session bean Kassel";
      KleinStadt stadtInstance = getInstanceByType(KleinStadt.class, new AnnotationLiteral<Important>() {});
      assert stadtInstance != null : "Expected instance to be created by container";
      assert frankfurt.isKleinStadtCreated() : "PostConstruct should be invoked when bean instance is created";
      frankfurt.resetCreatedFlags();
      
      // Create a second one to make sure create always does create a new session bean
      KleinStadt anotherStadtInstance = getInstanceByType(KleinStadt.class, new AnnotationLiteral<Important>() {});
      assert anotherStadtInstance != null : "Expected second instance of session bean";
      assert frankfurt.isKleinStadtCreated();
      assert anotherStadtInstance != stadtInstance : "create() should not return same bean as before";
      
      stadtInstance.setName("hometown");
      assert "hometown".equals(stadtInstance.getName());
      
      // Verify that the instance returned is a proxy by checking for all local interfaces
      assert getCurrentConfiguration().getBeans().isProxy(stadtInstance);
      Set<Class> interfaces = new HashSet<Class>(Arrays.asList(stadtInstance.getClass().getInterfaces()));
      assert interfaces.contains(KleinStadt.class);
      assert interfaces.contains(SchoeneStadt.class);
      assert interfaces.contains(Serializable.class);
   }
   
   @Test(groups = { "enterpriseBeans", "clientProxy", "lifecycle", "integration" })
   @SpecAssertions( {
      @SpecAssertion(section = "6.6.2", id = "a")
   })
   public void testSerializeSFSB() throws Exception
   {
      KleinStadt stadtInstance = getInstanceByType(KleinStadt.class, new AnnotationLiteral<Important>() {});

      byte[] bytes = serialize(stadtInstance);
      Object object = deserialize(bytes);
      stadtInstance = (KleinStadt) object;
      assert getCurrentConfiguration().getBeans().isProxy(stadtInstance);
      
   }

   @Test(groups = { "enterpriseBeans", "clientProxy", "lifecycle", "integration" })
   @SpecAssertions({
      @SpecAssertion(section = "7.3.2", id = "bc"),
      @SpecAssertion(section = "7.3.3", id = "c")
   })
   public void testDestroyRemovesSFSB() throws Exception
   {
      GrossStadt frankfurt = getInstanceByType(GrossStadt.class);
      Bean<KleinStadt> stadtBean = getBeans(KleinStadt.class).iterator().next();
      assert stadtBean != null : "Expected a bean for stateful session bean Kassel";
      Context requestContext = getCurrentManager().getContext(RequestScoped.class);
      CreationalContext<KleinStadt> creationalContext = getCurrentManager().createCreationalContext(stadtBean);
      KleinStadt kassel = stadtBean.create(creationalContext);
      assert requestContext.get(stadtBean) != null : "bean exists in request context";
      kassel.ping();
      stadtBean.destroy(kassel, creationalContext);
      
      assert frankfurt.isKleinStadtDestroyed() : "Expected SFSB bean to be destroyed";
      kassel = requestContext.get(stadtBean);
      assert kassel == null : "SFSB bean should not exist after being destroyed";
      //frankfurt.dispose();
   }

   @Test(groups = { "enterpriseBeans", "lifecycle", "integration" })
   @SpecAssertions({
      @SpecAssertion(section = "7.3.2", id = "bc"),
      @SpecAssertion(section = "3.2.1", id = "dba")
   })
   public void testRemovedEjbIgnored()
   {
      assert false;
      KleinStadt stadtInstance = getInstanceByType(KleinStadt.class, new AnnotationLiteral<Important>() {});
      assert stadtInstance != null : "Expected instance to be created by container";
      stadtInstance.setName("Kassel-Wilhelmshoehe");
      stadtInstance.zustandVergessen();
      
      // Now make sure that the container does not return this instance again
      KleinStadt newStadtInstance = getInstanceByType(KleinStadt.class);
      assert newStadtInstance != null : "Failed to get SFSB instance the second time";
      assert !"Kassel-Wilhelmshoehe".equals(newStadtInstance.getName()) : "The destroyed SFSB was not ignored";
   }

   @Test(groups = { "enterpriseBeans", "lifecycle", "integration" })
   @SpecAssertions({
       @SpecAssertion(section = "7.3.3", id = "b")})
   public void testCreateSLSB()
   {
      Bean<NeueStadt> stadtBean = getBeans(NeueStadt.class).iterator().next();
      assert stadtBean != null : "Expected a bean for stateful session bean Kassel";
      CreationalContext<NeueStadt> creationalContext = getCurrentManager().createCreationalContext(stadtBean);
      NeueStadt stadtInstance = stadtBean.create(creationalContext);
      assert stadtInstance != null : "Expected instance to be created by container";

      // Verify that the instance returned is a proxy by checking for all local interfaces
      Set<Class> interfaces = new HashSet<Class>(Arrays.asList(stadtInstance.getClass().getInterfaces()));
      assert interfaces.contains(NeueStadt.class);
      assert interfaces.contains(GeschichtslosStadt.class);
   }

   @Test(groups = { "enterpriseBeans", "lifecycle", "integration" })
   @SpecAssertion(section = "3.9.2", id = "b")
   public void testInitializerMethodsCalledWithCurrentParameterValues()
   {
      AlteStadt alteStadt = getInstanceByType(AlteStadt.class);
      assert alteStadt != null : "Could not find the AlteStadt bean";
      assert alteStadt.getAnotherPlaceOfInterest() != null;
   }

   @Test(groups = { "enterpriseBeans", "lifecycle" })
   @SpecAssertion(section = "5.6.5", id = "a")
   public void testDependentObjectsDestroyed()
   {
      Bean<UniStadt> uniStadtBean = getBeans(UniStadt.class).iterator().next();
      CreationalContext<UniStadt> creationalContext = getCurrentManager().createCreationalContext(uniStadtBean);
      UniStadt marburg = uniStadtBean.create(creationalContext);
      assert marburg != null : "Couldn't find the main SFSB";
      uniStadtBean.destroy(marburg, creationalContext);
      GrossStadt frankfurt = getInstanceByType(GrossStadt.class);
      assert frankfurt.isSchlossDestroyed();
   }

   @Test
   @SpecAssertion(section = "4.2", id = "bab")
   public void testDirectSubClassInheritsPostConstructOnSuperclass() throws Exception
   {
      OrderProcessor.postConstructCalled = false;
      assert getBeans(DirectOrderProcessorLocal.class).size() == 1;
      getInstanceByType(DirectOrderProcessorLocal.class).order();
      assert OrderProcessor.postConstructCalled;
   }
   
   @Test
   @SpecAssertion(section = "4.2", id = "bad")
   public void testIndirectSubClassInheritsPostConstructOnSuperclass() throws Exception
   {
      OrderProcessor.postConstructCalled = false;
      assert getBeans(OrderProcessorLocal.class).size() == 1;
      getInstanceByType(OrderProcessorLocal.class).order();
      assert OrderProcessor.postConstructCalled;
   }
   
   @Test
   @SpecAssertion(section = "4.2", id = "bbb")
   public void testSubClassInheritsPreDestroyOnSuperclass() throws Exception
   {
      OrderProcessor.preDestroyCalled = false;
      assert getBeans(DirectOrderProcessorLocal.class).size() == 1;
      Bean<DirectOrderProcessorLocal> bean = getBeans(DirectOrderProcessorLocal.class).iterator().next();
      CreationalContext<DirectOrderProcessorLocal> creationalContext = getCurrentManager().createCreationalContext(bean);
      DirectOrderProcessorLocal instance = bean.create(creationalContext);
      bean.destroy(instance, creationalContext);
      assert OrderProcessor.preDestroyCalled;
   }
   
   @Test
   @SpecAssertion(section = "4.2", id = "bbd")
   public void testIndirectSubClassInheritsPreDestroyOnSuperclass() throws Exception
   {
      OrderProcessor.preDestroyCalled = false;
      assert getBeans(OrderProcessorLocal.class).size() == 1;
      Bean<OrderProcessorLocal> bean = getBeans(OrderProcessorLocal.class).iterator().next();
      CreationalContext<OrderProcessorLocal> creationalContext = getCurrentManager().createCreationalContext(bean);
      OrderProcessorLocal instance = bean.create(creationalContext);
      bean.destroy(instance, creationalContext);
      assert OrderProcessor.preDestroyCalled;
   }
}
