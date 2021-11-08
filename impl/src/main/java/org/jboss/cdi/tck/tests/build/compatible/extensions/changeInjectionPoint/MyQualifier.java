package org.jboss.cdi.tck.tests.build.compatible.extensions.changeInjectionPoint;

import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface MyQualifier {
}
