package org.jboss.jsr299.tck.tests.definition.stereotype.enterprise;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

@AnimalStereotype
@SessionScoped
class ShortHairedDog implements Animal, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8316028446413430833L;

}
