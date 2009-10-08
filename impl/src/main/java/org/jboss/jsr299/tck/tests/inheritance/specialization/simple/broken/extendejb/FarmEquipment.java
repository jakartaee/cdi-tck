package org.jboss.jsr299.tck.tests.inheritance.specialization.simple.broken.extendejb;

import javax.ejb.Stateful;
import javax.inject.Named;


@Named("plough")
@Stateful
class FarmEquipment implements FarmEquipmentLocal
{

}
