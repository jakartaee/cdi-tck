package org.jboss.jsr299.tck.literals;

import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.Default;

/**
 * Annotation literal for @Current
 * 
 * @author Pete Muir
 */
public class DefaultLiteral extends AnnotationLiteral<Default> implements Default {}