package org.jboss.jsr299.tck.tests.interceptors.definition.multipleBindings;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

@Target( { TYPE, METHOD})
@Retention(RUNTIME)
@Documented
@InterceptorBinding
public @interface Slow
{

}
