package org.jboss.cdi.tck.tests.definition.stereotype;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.enterprise.inject.Stereotype;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

// servers purely as bean defining annotation
@Stereotype
@Target({ TYPE, METHOD, FIELD })
@Retention(RUNTIME)
public @interface DummyStereotype {
}
