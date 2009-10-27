package org.jboss.jsr299.tck.tests.event.observer.resolve;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.spi.ObserverMethod;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091018")
public class ResolveEventObserversTest extends AbstractJSR299Test
{
   private static final String BEAN_MANAGER_RESOLVE_OBSERVERS_METHOD_NAME = "resolveObserverMethods";

   @Test(groups = { "events" })
   @SpecAssertion(section = "10.4", id = "e")
   public void testMultipleObserverMethodsForSameEventPermissible()
   {
      assert getCurrentManager().resolveObserverMethods(new DiskSpaceEvent()).size() == 2;
   }
   
   @Test(groups = { "events" })
   @SpecAssertion(section = "10.4", id = "f")
   public void testMultipleObserverMethodsOnBeanPermissible()
   {
      assert getCurrentManager().resolveObserverMethods(new BatteryEvent()).size() == 1;
      assert getCurrentManager().resolveObserverMethods(new DiskSpaceEvent()).size() == 2;
   }
   
   @Test(groups = { "events" })
   @SpecAssertion(section = "10.4.2", id = "a")
   public void testMethodWithParameterAnnotatedWithObservesRegistersObserverMethod() throws SecurityException, NoSuchMethodException
   {
      Set<ObserverMethod<?, Temperature>> temperatureObservers = getCurrentManager().resolveObserverMethods(new Temperature(0d));
      assert temperatureObservers.size() == 1;
      ObserverMethod<?, Temperature> temperatureObserver = temperatureObservers.iterator().next();
      assert temperatureObserver.getBeanClass().equals(AirConditioner.class);
      assert temperatureObserver.getObservedType().equals(Temperature.class);
      
      Method method = AirConditioner.class.getMethod("temperatureChanged", Temperature.class);
      assert method != null;
      assert method.getParameterTypes().length == 1;
      assert method.getParameterTypes()[0].equals(Temperature.class);
      assert method.getParameterAnnotations()[0][0].annotationType().equals(Observes.class);
   }
   
   @Test(groups = { "events" })
   @SpecAssertion(section = "10.4.1", id = "b")
   public void testObserverMethodWithoutBindingTypesObservesEventsWithoutBindingTypes()
   {
      // Resolve registered observers with an event containing no binding types
      assert getCurrentManager().resolveObserverMethods(new SimpleEventType()).size() == 2;
   }

   @Test(groups = { "events" })
   @SpecAssertions( {
      @SpecAssertion(section = "10.4.2", id = "c"),
      @SpecAssertion(section = "10.2.2", id = "a"),
      @SpecAssertion(section = "10.2.3", id = "a")
   })
   public void testObserverMethodMayHaveMultipleBindingTypes()
   {
      // If we can resolve the observer with the two binding types, then it worked
      assert getCurrentManager().resolveObserverMethods(new MultiBindingEvent(), new RoleBinding("Admin"), new TameAnnotationLiteral()).size() == 2;
   }
   
   @Test(groups = { "events" })
   @SpecAssertion(section = "10.5", id = "aa")
   public void testObserverMethodRegistration()
   {
      // Resolve registered observers with an event containing no binding types
      assert getCurrentManager().resolveObserverMethods(new SimpleEventType()).size() == 2;
   }
   
   @Test(groups = { "events" })
   @SpecAssertions({
      // these two assertions combine to create a logical, testable assertion
      @SpecAssertion(section = "11.3.10", id = "a"),
      @SpecAssertion(section = "11.3.10", id = "b")
   })
   public void testBeanManagerResolveObserversSignature() throws Exception
   {
      assert getCurrentManager().getClass().getDeclaredMethod(BEAN_MANAGER_RESOLVE_OBSERVERS_METHOD_NAME, Object.class, Annotation[].class) != null;
   }

   @Test(groups = { "events" })
   @SpecAssertion(section = "12.3", id = "o")
   public void testObserverMethodAutomaticallyRegistered()
   {
      assert !getCurrentManager().resolveObserverMethods(new String(), new AnnotationLiteral<Secret>() {}).isEmpty();
   }
   
   @Test(groups = { "events" })
   @SpecAssertion(section = "12.3", id = "o")
   public void testObserverMethodNotAutomaticallyRegisteredForDisabledBeans()
   {
      Set<ObserverMethod<?, Ghost>> ghostObservers = getCurrentManager().resolveObserverMethods(new Ghost());
      assert ghostObservers.size() == 0;
      
      Set<ObserverMethod<?, String>> stringObservers = getCurrentManager().resolveObserverMethods(new String(), new AnnotationLiteral<Secret>() {});
      assert stringObservers.size() == 1;
      for (ObserverMethod<?, String> observer : stringObservers)
      {
         // an assertion error will be raised if an inappropriate observer is called
         observer.notify("fail if disabled observer invoked");
      }
   }
   
}
