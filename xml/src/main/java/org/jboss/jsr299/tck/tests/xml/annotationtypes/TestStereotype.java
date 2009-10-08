package org.jboss.jsr299.tck.tests.xml.annotationtypes;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.inject.Named;

@Retention(RUNTIME)
@Target(TYPE)
@Named
@interface TestStereotype
{

}
