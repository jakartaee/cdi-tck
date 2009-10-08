package org.jboss.jsr299.tck.tests.context.dependent.ejb;

import java.io.Serializable;

import javax.annotation.PreDestroy;

class Horse implements Serializable
{
   
   /**
	 * 
	 */
	private static final long serialVersionUID = 1417245695143239294L;
public static boolean destroyed;
   
   @PreDestroy
   public void preDestroy()
   {
      destroyed = true;
   }
   
}
