package org.jboss.jsr299.tck.tests.definition.stereotype;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

@AnimalStereotype
@SessionScoped
class ShortHairedDog implements Animal, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1859229950272574260L;

}
