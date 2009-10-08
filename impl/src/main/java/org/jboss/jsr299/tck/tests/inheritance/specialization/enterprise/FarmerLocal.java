package org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise;

import javax.ejb.Local;

@Local
public interface FarmerLocal
{
   public String getClassName();
}
