package org.jboss.jsr299.tck.tests.implementation.enterprise.definition;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Named;

@Stateless
@Named("Monkey") @Default
class Monkey implements MonkeyLocal
{

}
