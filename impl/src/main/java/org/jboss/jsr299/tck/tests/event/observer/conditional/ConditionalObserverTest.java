package org.jboss.jsr299.tck.tests.event.observer.conditional;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Reception;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091101")
public class ConditionalObserverTest extends AbstractJSR299Test
{
   @Test(groups = { "events" })
   @SpecAssertions( {
      @SpecAssertion(section = "5.5.6", id = "baa"),
      @SpecAssertion(section = "10.4.3", id = "a")
   } )
   public void testConditionalObserver()
   {
      RecluseSpider.reset();
      getCurrentManager().fireEvent(new ConditionalEvent());
      // Should not be notified since bean is not instantiated yet
      assert !RecluseSpider.isNotified();

      // Now instantiate the bean and fire another event
      RecluseSpider bean = getInstanceByType(RecluseSpider.class);
      assert bean != null;
      // Must invoke a method to really create the instance
      assert !bean.isInstanceNotified();
      getCurrentManager().fireEvent(new ConditionalEvent());
      assert RecluseSpider.isNotified() && bean.isInstanceNotified();

      RecluseSpider.reset();
   }
   
   @Test(groups = { "events" })
   @SpecAssertion(section = "5.5.6", id = "baa")
   public void testObserverMethodInvokedOnReturnedInstanceFromContext()
   {
      RecluseSpider spider = getInstanceByType(RecluseSpider.class);
      spider.setWeb(new Web());
      getCurrentManager().fireEvent(new ConditionalEvent());
      assert spider.isInstanceNotified();
      assert spider.getWeb().getRings() == 1;
   }
   
   @Test
   @SpecAssertion(section = "10.4.3", id = "c")
   public void testNotifyEnumerationContainsNotifyValues()
   {
      assert Reception.values().length == 2;
      List<String> notifyValueNames = new ArrayList<String>();
      for (Reception value : Reception.values())
      {
         notifyValueNames.add(value.name());
      }
      
      assert notifyValueNames.contains("IF_EXISTS");
      assert notifyValueNames.contains("ALWAYS");
   }
}
