package org.jboss.cdi.tck.tests.build.compatible.extensions.customQualifier;

import jakarta.enterprise.util.AnnotationLiteral;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MyCustomQualifier {
    String value();

    class Literal extends AnnotationLiteral<MyCustomQualifier> implements MyCustomQualifier {
        private final String value;

        Literal(String value) {
            this.value = value;
        }

        @Override
        public String value() {
            return value;
        }
    }
}
