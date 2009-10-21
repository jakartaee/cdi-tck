package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.interceptor.InterceptorBinding;

@InterceptorBinding
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@interface GroceryInterceptorBinding
{

}
