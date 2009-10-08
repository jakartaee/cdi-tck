package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.context.RequestScoped;
import javax.inject.Production;

@RequestScopedAnimalStereotype
@RequestScoped
@Production
class Goldfish implements Animal
{

}
