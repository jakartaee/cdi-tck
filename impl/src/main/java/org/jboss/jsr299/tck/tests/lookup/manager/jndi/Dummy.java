package org.jboss.jsr299.tck.tests.lookup.manager.jndi;

import javax.ejb.Stateless;

/**
 * Dummy stateless session bean used to make true EJB module (JBoss AS7 sub-deployment).
 * 
 * See <a href="http://community.jboss.org/message/623471?tstart=0">Test EAR deploys on AS7, but test config can't be found</a>
 * or <a href="http://community.jboss.org/message/615412">War classes cannot see EJB classes</a> threads.
 * 
 * @author Martin Kouba
 */
@Stateless
public class Dummy
{

}
