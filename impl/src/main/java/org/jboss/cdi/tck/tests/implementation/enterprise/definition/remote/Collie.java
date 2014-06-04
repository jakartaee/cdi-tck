package org.jboss.cdi.tck.tests.implementation.enterprise.definition.remote;

import javax.ejb.Singleton;

@Tame
@Singleton(mappedName="Collie")
public class Collie implements DogRemote {

}
