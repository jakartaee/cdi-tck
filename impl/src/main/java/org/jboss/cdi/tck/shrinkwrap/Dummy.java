package org.jboss.cdi.tck.shrinkwrap;

import javax.ejb.Stateless;

/**
 * Dummy SLSB used to make every EJB module <i>true</i> EJB module (JBoss AS7 sub-deployment).
 * 
 * <p>
 * See <a href="http://community.jboss.org/message/623471?tstart=0">Test EAR deploys on AS7, but test config can't be found</a>
 * or <a href="http://community.jboss.org/message/615412">War classes cannot see EJB classes</a> threads.
 * </p>
 * 
 * @author Martin Kouba
 */
@Stateless(name = "org.jboss.cdi.tck.shrinkwrap.Dummy")
public class Dummy {

}
