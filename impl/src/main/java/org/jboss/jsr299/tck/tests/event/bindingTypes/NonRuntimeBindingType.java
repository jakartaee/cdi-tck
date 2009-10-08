package org.jboss.jsr299.tck.tests.event.bindingTypes;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Target( { FIELD, PARAMETER })
@Qualifier
@Retention(RetentionPolicy.CLASS)
@interface NonRuntimeBindingType
{
}
