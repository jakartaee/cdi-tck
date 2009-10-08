package org.jboss.jsr299.tck.tests.definition.name;

import javax.enterprise.inject.Default;
import javax.inject.Named;

@Named("aMoose") @Default
class Moose implements Animal
{

}
