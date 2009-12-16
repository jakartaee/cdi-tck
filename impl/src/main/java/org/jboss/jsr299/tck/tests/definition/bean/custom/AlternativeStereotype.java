package org.jboss.jsr299.tck.tests.definition.bean.custom;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Stereotype;

@Alternative
@Stereotype
@Target(TYPE)
@Retention(RUNTIME)
@interface AlternativeStereotype
{
}
