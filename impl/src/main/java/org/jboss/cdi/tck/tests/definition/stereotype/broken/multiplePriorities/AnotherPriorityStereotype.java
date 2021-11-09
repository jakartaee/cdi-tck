package org.jboss.cdi.tck.tests.definition.stereotype.broken.multiplePriorities;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Stereotype;
import org.jboss.cdi.tck.tests.definition.stereotype.priority.PriorityStereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Stereotype with {@code @Priority} that also declares another stereotype with {@code @Priority}
 */
@Stereotype
@Priority(200)
@PriorityStereotype // this one has @Priority(100)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnotherPriorityStereotype {
}
