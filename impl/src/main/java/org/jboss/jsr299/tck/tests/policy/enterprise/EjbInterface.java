package org.jboss.jsr299.tck.tests.policy.enterprise;

import javax.ejb.Local;

@Local
interface EjbInterface
{
   String hello();
}
