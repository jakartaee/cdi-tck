package org.jboss.jsr299.tck.tests.interceptors.definition.enterprise;

import javax.ejb.Local;

@Local
public interface MissileLocal
{
   void fire();
}
