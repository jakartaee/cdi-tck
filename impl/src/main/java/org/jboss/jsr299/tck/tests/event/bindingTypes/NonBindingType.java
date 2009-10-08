package org.jboss.jsr299.tck.tests.event.bindingTypes;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target( { FIELD, PARAMETER })
@Retention(RUNTIME)
@interface NonBindingType
{
}
