package org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise.broken.directlyExtendsNothing;

import javax.ejb.Stateful;
import javax.enterprise.inject.Specializes;

@Specializes
@Stateful
class Cow_Broken implements CowLocal_Broken
{

}
