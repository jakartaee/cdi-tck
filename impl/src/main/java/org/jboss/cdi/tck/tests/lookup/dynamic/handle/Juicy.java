package org.jboss.cdi.tck.tests.lookup.dynamic.handle;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Qualifier
@Target({ TYPE, METHOD, PARAMETER, FIELD })
@Retention(RUNTIME)
public @interface Juicy {

    @SuppressWarnings("all")
    public static class Literal extends AnnotationLiteral<Juicy> implements Juicy {

        private Literal() {
        }

        public static final Literal INSTANCE = new Literal();
    }
}
