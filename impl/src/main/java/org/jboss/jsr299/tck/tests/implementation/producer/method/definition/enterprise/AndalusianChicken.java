package org.jboss.jsr299.tck.tests.implementation.producer.method.definition.enterprise;

import javax.ejb.Stateful;
import javax.enterprise.inject.Specializes;

@Stateful
@Specializes
public class AndalusianChicken extends Chicken implements AndalusianChickenLocal
{

}
