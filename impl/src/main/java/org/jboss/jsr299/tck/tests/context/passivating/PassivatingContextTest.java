package org.jboss.jsr299.tck.tests.context.passivating;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.IllegalProductException;
import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

/**
 * 
 * @author Nicklas Karlsson
 * @author David Allen
 */
@Artifact
@BeansXml("beans.xml")
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="20091018")
public class PassivatingContextTest extends AbstractJSR299Test
{
   
   @Test(groups = { "contexts", "passivation", "rewrite" })
   @SpecAssertion(section = "6.6.1", id = "ba")
   public void testManagedBeanWithSerializableImplementationClassOK()
   {
      Set<Bean<Jyvaskyla>> beans = getBeans(Jyvaskyla.class);
      assert !beans.isEmpty();
   }
   
   @Test(groups = { "contexts", "passivation"})
   @SpecAssertion(section = "6.6.1", id = "bb")
   public void testManagedBeanWithSerializableInterceptorClassOK()
   {
      Set<Bean<Kokkola>> beans = getBeans(Kokkola.class);
      assert !beans.isEmpty();
   }
   
   @Test(groups = { "contexts", "passivation" })
   @SpecAssertion(section = "6.6.1", id = "bc")
   public void testManagedBeanWithSerializableDecoratorOK()
   {
      Set<Bean<City>> beans = getBeans(City.class);
      assert !beans.isEmpty();
   }   
   
   @Test(groups = { "contexts", "passivation" })
   @SpecAssertion(section = "6.6.1", id = "ca")
   public void testPassivationCapableProducerMethodIsOK()
   {
      Set<Bean<Record>> beans = getBeans(Record.class);
      assert !beans.isEmpty();
   }
   
   @Test(groups = { "contexts", "passivation" })
   @SpecAssertion(section = "6.6.1", id = "da")
   public void testPassivationCapableProducerFieldIsOK()
   {
      Set<Bean<Wheat>> beans = getBeans(Wheat.class);
      assert !beans.isEmpty();
   }
   
   @Test @SpecAssertion(section="6.6.2", id = "c")
   public void testInjectionOfDependentPrimitiveProductIntoNormalBean()
   {
      getInstanceByType(NumberConsumer.class).ping();
   }
   
   @Test @SpecAssertion(section="6.6.2", id = "c")
   public void testInjectionOfDependentSerializableProductIntoNormalBean()
   {
      getInstanceByType(SerializableCityConsumer.class).ping();
   }

   @Test(groups = { "contexts", "passivation" })
   @SpecAssertions({
      @SpecAssertion(section = "6.6", id = "a")
   })
   public void testPassivationOccurs() throws IOException, ClassNotFoundException
   {
      Kajaani instance = getInstanceByType(Kajaani.class);
      instance.setTheNumber(100);
      Context sessionContext = getCurrentManager().getContext(SessionScoped.class);
      setContextInactive(sessionContext);
      setContextActive(sessionContext);
      instance = getInstanceByType(Kajaani.class);
      assert instance.getTheNumber() == 100;
   }

   @SuppressWarnings("unchecked")
   private <T> boolean testSerialize(Bean<T> bean) throws IOException, ClassNotFoundException
   {
      getCurrentManager().addBean(bean);
      T instance = getCurrentManager().getContext(bean.getScope()).get(bean);
      byte[] data = serialize(instance);
      T resurrected = (T) deserialize(data);
      assert resurrected != null;
      return resurrected.toString().equals(instance.toString());
   }

   @Test(groups = { "contexts", "passivation" })
   @SpecAssertion(section = "6.6.4", id = "aaba")
   public void testBeanWithNonSerializableImplementationInjectedIntoTransientFieldOK()
   {
      Set<Bean<Joensuu>> beans = getBeans(Joensuu.class);
      assert !beans.isEmpty();
   }
   
   @Test(expectedExceptions = IllegalProductException.class)
   @SpecAssertion(section = "6.6.4", id = "ea")
   public void testPassivatingScopeProducerMethodReturnsUnserializableObjectNotOk()
   {
      getInstanceByType(Television.class).turnOn();
   }
      
   @Test(expectedExceptions = IllegalProductException.class)
   @SpecAssertion(section = "6.6.4", id = "eb")
   public void testNonSerializableProducerFieldDeclaredPassivatingThrowsIllegalProductException()
   {
      getInstanceByType(HelsinkiNonSerializable.class).ping();
   }   
   
   public static boolean isSerializable(Class<?> clazz)
   {
      return clazz.isPrimitive() || Serializable.class.isAssignableFrom(clazz);
   }

}
