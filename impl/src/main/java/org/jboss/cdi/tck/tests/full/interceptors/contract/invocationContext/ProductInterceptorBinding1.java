package org.jboss.cdi.tck.tests.full.interceptors.contract.invocationContext;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({TYPE, METHOD})
@Retention(RUNTIME)
@Documented
@InterceptorBinding
public @interface ProductInterceptorBinding1 {

    class Literal extends AnnotationLiteral<ProductInterceptorBinding1> implements ProductInterceptorBinding1 {

        public static Literal INSTANCE = new Literal();

    }
}
