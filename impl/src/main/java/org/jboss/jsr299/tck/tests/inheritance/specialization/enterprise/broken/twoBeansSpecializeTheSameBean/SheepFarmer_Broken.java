package org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise.broken.twoBeansSpecializeTheSameBean;

import javax.ejb.Stateful;
import javax.enterprise.inject.Specializes;

@Specializes
@Stateful
class SheepFarmer_Broken extends Farmer
{
}
