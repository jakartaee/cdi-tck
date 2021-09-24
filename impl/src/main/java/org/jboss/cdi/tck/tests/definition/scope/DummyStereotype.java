package org.jboss.cdi.tck.tests.definition.scope;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.enterprise.inject.Stereotype;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

// technically useless stereotype that only serves as bean defining annotation
@Stereotype
@Target({ TYPE })
@Retention(RUNTIME)
public @interface DummyStereotype {
}
