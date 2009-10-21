package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.inject.stereotype.Stereotype;
import javax.inject.Named;

@Target(TYPE)
@Retention(RUNTIME)
@Stereotype
@Named
@interface NamedStereotype
{

}
