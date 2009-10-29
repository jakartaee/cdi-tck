package org.jboss.jsr299.tck.literals;

import javax.enterprise.inject.Default;
import javax.enterprise.util.AnnotationLiteral;

/**
 * Annotation literal for @Current
 * 
 * @author Pete Muir
 */
public class DefaultLiteral extends AnnotationLiteral<Default> implements Default {}