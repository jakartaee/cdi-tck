package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonPassivatingInitializerParameterInInterceptor;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

@Documented
@Retention(RUNTIME)
@Target({METHOD, TYPE})
@InterceptorBinding
public @interface City
{
}
