package org.jboss.cdi.tck.tests.implementation.simple.newSimpleBean;

import jakarta.enterprise.inject.New;
import jakarta.inject.Inject;

/**
 * Provide injection point for {@link Tiger} alternative to test {@link New} qualifier functionality.
 * 
 * @author Martin Kouba
 * @see Tiger
 */
public class TigerNewInject {

    @Inject
    @New
    Tiger tiger;

}
