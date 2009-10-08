package org.jboss.jsr299.tck.tests.context;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

@SessionScoped
public class SimpleBeanB implements Serializable
{
   private static final long serialVersionUID = 1L;

   @Inject @SessionScoped
   private SimpleBeanZ z;

   public SimpleBeanZ getZ()
   {
      return z;
   }
}
