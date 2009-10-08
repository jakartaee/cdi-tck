package org.jboss.jsr299.tck.tests.context.dependent.ejb;

import java.io.Serializable;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

class Stable implements Serializable
{
   
   /**
	 * 
	 */
	private static final long serialVersionUID = -5240670259345637799L;

	@Inject Horse horse;
   
   public static boolean destroyed;
   
   @PreDestroy
   public void preDestroy()
   {
      destroyed = true;
   }
   
}
