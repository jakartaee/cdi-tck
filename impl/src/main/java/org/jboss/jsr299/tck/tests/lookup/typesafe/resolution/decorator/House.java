package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution.decorator;

import javax.inject.Inject;

class House {
	@SuppressWarnings("unused")
	@Inject
	private AnimalDecorator decorator;
}
