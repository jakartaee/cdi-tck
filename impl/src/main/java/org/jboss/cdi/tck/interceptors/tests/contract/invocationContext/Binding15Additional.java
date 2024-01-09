package org.jboss.cdi.tck.interceptors.tests.contract.invocationContext;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.interceptor.InterceptorBinding;

@InterceptorBinding
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface Binding15Additional {

    String value();

    public class Literal extends AnnotationLiteral<Binding15Additional> implements Binding15Additional {
        
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
