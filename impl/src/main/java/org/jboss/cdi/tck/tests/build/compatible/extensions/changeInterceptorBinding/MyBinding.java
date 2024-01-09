package org.jboss.cdi.tck.tests.build.compatible.extensions.changeInterceptorBinding;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;

@InterceptorBinding
@Inherited
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface MyBinding {

    @Nonbinding
    public String value();
    
    public class Literal extends AnnotationLiteral<MyBinding> implements MyBinding {
        private String value;

        public Literal(String value) {
            this.value = value;
        }

        @Override
        public String value() {
            return value;
        }
    }
}
