package org.jboss.cdi.tck.tests.definition.stereotype.broken.scopeConflict.transitive;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Stereotype;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Stereotype
@Target({ TYPE, METHOD, FIELD })
@Retention(RUNTIME)
@FishStereotype
@RequestScoped
public @interface AnimalStereotype {
}
