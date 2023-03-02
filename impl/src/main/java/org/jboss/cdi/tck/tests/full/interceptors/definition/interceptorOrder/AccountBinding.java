package org.jboss.cdi.tck.tests.full.interceptors.definition.interceptorOrder;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.interceptor.InterceptorBinding;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@InterceptorBinding
@Inherited
@Target({ TYPE })
@Retention(RUNTIME)
public @interface AccountBinding {
}
