package org.jboss.jsr299.tck.tests.implementation.enterprise.broken.statelessWithRequestScope;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;

@Stateless
@RequestScoped
class Beagle_Broken implements BeagleLocal_Broken
{

}
