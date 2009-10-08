package org.jboss.jsr299.tck.tests.xml.namespace.javaee.pkg;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Set;

import javax.ejb.TimerHandle;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.event.Event;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.xml.ws.Binding;

import org.hibernate.tck.annotations.SpecAssertion;
import org.hibernate.tck.annotations.SpecAssertions;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@Classes({Order.class})
@BeansXml("beans.xml")
public class JavaEePkgTest extends AbstractJSR299Test
{
   private static final Annotation TEST_BINDING_TYPE = new AnnotationLiteral<TestBindingType>() {};
   
   private static final Annotation[] bindings = {TEST_BINDING_TYPE};
   
   @Test(groups = { "broken", "xml" })
   @SpecAssertions({
     @SpecAssertion(section="9.2.1", id="a")
   })
   public void testJavaEePkg()
   {
      BeanManager beanManager = getCurrentManager();
      Set<Bean<Order>> beans = getBeans(Order.class, bindings);
      
      assert beans.size() == 1;
      
      for (Bean<Order> bean : beans)
      {
         assert bean.getScopeType().equals(RequestScoped.class);
         
         Object[] beanBindings = bean.getBindings().toArray();         
         assert beanBindings.length == 1;
         for (int i = 0; i < beanBindings.length; i++)
         {
            Annotation beanBinding = (Annotation) beanBindings[i];
            assert beanBinding.annotationType().equals(bindings[i].annotationType());
         }
         
         Set<? extends Type> beanTypes = bean.getTypes();
         assert beanTypes.contains(Order.class);

         Class<?> clazz = Order.class;
         try
         {
            assert clazz.getDeclaredConstructor(Integer.class, Date.class, DataSource.class, InvocationContext.class, Event.class, 
                  TimerHandle.class, EntityManager.class, Binding.class) != null;
         }
         catch (SecurityException e)
         {
            assert false : "SecurityException while getting constructor with 'int' parameter from '" + clazz + "'";
         }
         catch (NoSuchMethodException e)
         {
            assert false : "Can not find constructor with 'int' parameter in '" + clazz + "'";
         }
      }
   }
}
