package org.jboss.jsr299.tck.tests.implementation.enterprise.broken.statelessWithApplicationScope;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;

@Stateless
@ApplicationScoped
class Dachshund_Broken implements DachshundLocal_Broken
{

}
