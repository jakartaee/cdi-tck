package org.jboss.cdi.tck.interceptors.tests.contract.aroundConstruct;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.interceptor.InterceptorBinding;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@InterceptorBinding
@Inherited
@Target({ TYPE, CONSTRUCTOR })
@Retention(RUNTIME)
public @interface AlphaBinding {
}
