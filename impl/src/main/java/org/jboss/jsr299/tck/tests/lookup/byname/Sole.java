package org.jboss.jsr299.tck.tests.lookup.byname;

import javax.enterprise.inject.Alternative;
import javax.inject.Named;

@Named("fish")
@Alternative
class Sole implements Animal
{

}
