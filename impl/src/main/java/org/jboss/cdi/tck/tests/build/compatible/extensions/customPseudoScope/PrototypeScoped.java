package org.jboss.cdi.tck.tests.build.compatible.extensions.customPseudoScope;

import jakarta.enterprise.inject.Stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Prototype
@Stereotype
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrototypeScoped {
}
