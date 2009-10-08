package org.jboss.jsr299.tck.literals;

import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.Current;

/**
 * Annotation literal for @Current
 * 
 * @author Pete Muir
 */
public class CurrentLiteral extends AnnotationLiteral<Current> implements Current {}