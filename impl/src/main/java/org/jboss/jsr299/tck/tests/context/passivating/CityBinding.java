package org.jboss.jsr299.tck.tests.context.passivating;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.context.Dependent;
import javax.interceptor.InterceptorBinding;

@InterceptorBinding
@Dependent
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@interface CityBinding
{

}
