package org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise.broken.sameName;

import javax.ejb.Stateful;
import javax.enterprise.inject.Specializes;
import javax.inject.Named;

@Specializes
@Stateful
@Named
class FarmYard_Broken extends Yard
{
   
}
