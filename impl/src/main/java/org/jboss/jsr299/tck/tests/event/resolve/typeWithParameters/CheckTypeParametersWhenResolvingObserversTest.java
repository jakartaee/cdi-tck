package org.jboss.jsr299.tck.tests.event.resolve.typeWithParameters;

import java.util.ArrayList;

import javax.enterprise.event.Observes;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091018")
public class CheckTypeParametersWhenResolvingObserversTest extends AbstractJSR299Test
{
   public static class CharacterList extends ArrayList<Character>
   {
      private static final long serialVersionUID = 1L;
   }
   
   public static class StringList extends ArrayList<String>
   {
      private static final long serialVersionUID = 1L;
   }
   
   public static class IntegerList extends ArrayList<Integer>
   {
      private static final long serialVersionUID = 1L;
   }
   
   public static class StringListObserver
   {
      public boolean wasNotified = false;

      public void observer(@Observes ArrayList<String> event)
      {
         wasNotified = true;
      }
   }

   public static class IntegerListObserver
   {
      public boolean wasNotified = false;

      public void observer(@Observes ArrayList<Integer> event)
      {
         wasNotified = true;
      }
   }

   @Test(groups = { "events" })
   @SpecAssertions({
      @SpecAssertion(section = "10.4.1", id = "a"),
      @SpecAssertion(section = "11.3.10", id = "a")
   })
   public void testResolvingChecksTypeParameters()
   {
      assert getCurrentManager().resolveObserverMethods(new StringList()).size() == 1;
      assert getCurrentManager().resolveObserverMethods(new CharacterList()).size() == 0;
   }
   
   @Test(groups = { "events" })
   @SpecAssertions({
      @SpecAssertion(section = "10.2", id = "h"),
      @SpecAssertion(section = "10.4", id = "aa")
      // FIXME also 10.3.1, which does not yet have spec assertions cataloged
   })
   public void testResolvingChecksTypeParametersOnObservesMethod()
   {
      Foo<String> fooEvent = new Foo<String>() {};
      getCurrentManager().fireEvent(fooEvent);
      assert fooEvent.isObserved();
   }
}
