package org.jboss.cdi.tck.tests.full.decorators.lookup.clientProxy.unproxyable.decorator;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;

@Decorator
public abstract class MarineDecorator implements Fish {

	@Inject
	@Delegate
	@Any
	private Fish fish;

	public MarineDecorator() {
	}

}
