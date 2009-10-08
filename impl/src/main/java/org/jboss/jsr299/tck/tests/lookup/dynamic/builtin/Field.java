package org.jboss.jsr299.tck.tests.lookup.dynamic.builtin;

import java.io.Serializable;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

public class Field implements Serializable
{
   
   @Inject private Instance<Cow> instance;
   
   public Instance<Cow> getInstance()
   {
      return instance;
   }

}
