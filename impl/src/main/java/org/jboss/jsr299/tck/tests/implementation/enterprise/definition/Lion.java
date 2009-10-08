package org.jboss.jsr299.tck.tests.implementation.enterprise.definition;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;

@Stateful
@RequestScoped
class Lion implements LionLocal
{

}
