package org.jboss.jsr299.tck.tests.context.passivating.broken.interceptorWithNonPassivatingInitializerMethodParameter;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.interceptor.Interceptor;

@Target({TYPE, METHOD})
@Retention(RUNTIME)
@Interceptor @interface InterceptorType
{

}