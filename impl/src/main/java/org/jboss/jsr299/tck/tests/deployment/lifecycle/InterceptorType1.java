package org.jboss.jsr299.tck.tests.deployment.lifecycle;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

@Target({TYPE, METHOD})
@Retention(RUNTIME)
@InterceptorBinding
@interface InterceptorType1
{

}
