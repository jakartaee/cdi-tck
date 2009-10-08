package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.annotation.Stereotype;
import javax.context.RequestScoped;

@Stereotype(requiredTypes=Animal.class, supportedScopes=RequestScoped.class)
@Target( { TYPE })
@Retention(RUNTIME)
@interface RequestScopedAnimalStereotype
{

}
