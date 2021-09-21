package org.jboss.cdi.tck.tests.full.decorators.lookup.clientProxy.unproxyable.decorator;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class TestBean {
	
	@Inject
	Tuna tuna;

}
