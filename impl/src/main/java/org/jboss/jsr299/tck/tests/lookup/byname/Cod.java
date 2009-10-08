package org.jboss.jsr299.tck.tests.lookup.byname;

import javax.enterprise.inject.Alternative;
import javax.inject.Named;

@Named("whitefish")
@Alternative
class Cod implements Animal
{

}
