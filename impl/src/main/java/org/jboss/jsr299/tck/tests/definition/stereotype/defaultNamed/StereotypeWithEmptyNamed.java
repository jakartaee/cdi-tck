package org.jboss.jsr299.tck.tests.definition.stereotype.defaultNamed;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.inject.stereotype.Stereotype;
import javax.inject.Named;

@Stereotype
@Target( { TYPE })
@Retention(RUNTIME)
@Named
@interface StereotypeWithEmptyNamed
{

}
