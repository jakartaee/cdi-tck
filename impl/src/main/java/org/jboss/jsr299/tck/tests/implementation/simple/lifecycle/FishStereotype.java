package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;

@Stereotype
@Target( { TYPE })
@Retention(RUNTIME)
@ApplicationScoped
@Named
@interface FishStereotype
{

}
