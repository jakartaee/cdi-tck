package org.jboss.jsr299.tck.tests.context.application.ejb;

import javax.ejb.Local;

@Local
public interface Bird
{
   public void eat();
}
