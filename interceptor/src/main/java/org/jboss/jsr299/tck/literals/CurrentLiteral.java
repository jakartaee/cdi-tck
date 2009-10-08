package org.jboss.jsr299.tck.literals;

import javax.inject.AnnotationLiteral;
import javax.inject.Current;

/**
 * Annotation literal for @Current
 * 
 * @author Pete Muir
 */
public class CurrentLiteral extends AnnotationLiteral<Current> implements Current {}