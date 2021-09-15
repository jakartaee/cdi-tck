package org.jboss.cdi.tck.tests.full.deployment.trimmed;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Stereotype;

@RequestScoped
@Stereotype
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Popular {
}
