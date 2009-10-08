package org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise.broken.directlyExtendsSimpleBean;

import javax.ejb.Stateful;
import javax.enterprise.inject.Specializes;

@Specializes
@Stateful
class Tractor_Broken extends FarmEquipment implements TractorLocal
{

}
