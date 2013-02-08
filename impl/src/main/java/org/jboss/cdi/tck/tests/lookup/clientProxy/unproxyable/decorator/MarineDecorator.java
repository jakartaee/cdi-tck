package org.jboss.cdi.tck.tests.lookup.clientProxy.unproxyable.decorator;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

@Decorator
public abstract class MarineDecorator implements Fish {

	@Inject
	@Delegate
	@Any
	private Fish fish;

	public MarineDecorator() {
	}

}
