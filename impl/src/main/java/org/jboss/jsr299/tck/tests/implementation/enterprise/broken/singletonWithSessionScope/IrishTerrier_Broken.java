package org.jboss.jsr299.tck.tests.implementation.enterprise.broken.singletonWithSessionScope;

import java.io.Serializable;

import javax.ejb.Singleton;
import javax.enterprise.context.SessionScoped;

@Singleton
@SessionScoped
class IrishTerrier_Broken implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1628520203303413522L;

}
