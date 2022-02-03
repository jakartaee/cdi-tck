package org.jboss.cdi.tck.tests.definition.stereotype.priority.inherited;

import jakarta.enterprise.inject.Stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Doesn't add anything but declares another stereotype with priority.
 */
@Stereotype
@StereotypeWithPriority
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DumbStereotype {
}
