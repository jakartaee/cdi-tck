package org.jboss.jsr299.tck.tests.interceptors.definition.enterprise;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;

import javax.interceptor.InterceptorBinding;

@Target( { TYPE, METHOD})
@Retention(RUNTIME)
@Documented
@InterceptorBinding
public @interface Airborne
{

}