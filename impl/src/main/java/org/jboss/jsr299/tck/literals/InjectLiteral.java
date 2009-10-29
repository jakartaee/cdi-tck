package org.jboss.jsr299.tck.literals;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

/**
 * Annotation literal for @Current
 * 
 * @author Pete Muir
 */
public class InjectLiteral extends AnnotationLiteral<Inject> implements Inject {}