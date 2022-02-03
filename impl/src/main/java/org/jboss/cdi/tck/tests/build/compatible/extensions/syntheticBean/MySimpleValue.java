package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBean;

import jakarta.enterprise.util.AnnotationLiteral;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MySimpleValue {
    String value();

    byte[] bytes() default { 1, 2, 3 };

    class Literal extends AnnotationLiteral<MySimpleValue> implements MySimpleValue {
        private final String value;
        private final byte[] bytes;

        Literal(String value) {
            this(value, new byte[] { 1, 2, 3 });
        }

        Literal(String value, byte[] bytes) {
            this.value = value;
            this.bytes = bytes;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public byte[] bytes() {
            return bytes;
        }
    }
}
