package org.jboss.cdi.tck.interceptors.tests.contract.invocationContext;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.interceptor.InterceptorBinding;

@InterceptorBinding
@Binding15Additional("AdditionalBinding")
@Target({ TYPE, METHOD })
@Retention(RUNTIME)
public @interface Binding15 {

    public class Literal extends AnnotationLiteral<Binding15> implements Binding15 {}
}
