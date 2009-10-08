package org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise.broken.sameName;

import javax.ejb.Stateful;
import javax.inject.Named;

@Named("backyard")
@Stateful
class Yard implements YardInterface
{
   
}
