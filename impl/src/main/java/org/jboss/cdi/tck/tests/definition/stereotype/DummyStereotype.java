package org.jboss.cdi.tck.tests.definition.stereotype;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.enterprise.inject.Stereotype;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

// empty stereotype just to trigger bean discovery
@Stereotype
@Target(TYPE)
@Retention(RUNTIME)
public @interface DummyStereotype {
}
