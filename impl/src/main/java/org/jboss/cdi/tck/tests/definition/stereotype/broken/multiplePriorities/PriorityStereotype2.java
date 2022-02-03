package org.jboss.cdi.tck.tests.definition.stereotype.broken.multiplePriorities;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Simple stereotype with priority different than
 * {@link org.jboss.cdi.tck.tests.definition.stereotype.priority.PriorityStereotype}.
 */
@Stereotype
@Priority(200)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PriorityStereotype2 {
}
