package org.jboss.cdi.tck.literals;

import jakarta.enterprise.inject.Stereotype;
import jakarta.enterprise.util.AnnotationLiteral;

public class StereotypeLiteral extends AnnotationLiteral<Stereotype> implements Stereotype {
    public static StereotypeLiteral INSTANCE = new StereotypeLiteral();
}
