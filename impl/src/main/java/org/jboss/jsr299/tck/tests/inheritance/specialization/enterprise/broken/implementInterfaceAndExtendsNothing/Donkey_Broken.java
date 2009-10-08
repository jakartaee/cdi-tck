package org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise.broken.implementInterfaceAndExtendsNothing;

import javax.ejb.Stateful;
import javax.enterprise.inject.Specializes;

@Specializes
@Stateful
class Donkey_Broken implements Animal
{

}
