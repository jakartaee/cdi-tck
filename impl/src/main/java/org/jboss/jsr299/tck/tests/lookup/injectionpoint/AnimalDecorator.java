package org.jboss.jsr299.tck.tests.lookup.injectionpoint;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
class AnimalDecorator implements Animal {
	
	@Inject	@Delegate
	private Animal bean;

	public String hello() {
		return bean.hello() + " world!";
	}
}
