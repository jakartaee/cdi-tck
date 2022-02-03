package org.jboss.cdi.tck.tests.definition.stereotype.priority;

import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Makes the bean {@code @Alternative} and declares {@code @PriorityStereotype} so it inherits {@code @Priority}
 */
@Stereotype
@Alternative
@PriorityStereotype
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AlternativePriorityStereotype {
}
