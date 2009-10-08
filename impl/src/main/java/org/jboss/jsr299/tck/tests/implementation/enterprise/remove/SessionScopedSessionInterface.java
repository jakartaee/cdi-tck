package org.jboss.jsr299.tck.tests.implementation.enterprise.remove;

import javax.ejb.Local;

@Local
public interface SessionScopedSessionInterface
{
   public void remove();
}
