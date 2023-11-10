package org.jboss.cdi.tck.tests.alternative.selection.priority;

import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ProducedByMethod {
}
