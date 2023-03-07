package org.jboss.cdi.tck.literals;

import jakarta.enterprise.util.AnnotationLiteral;

public class OverrideLiteral extends AnnotationLiteral<Override> implements Override {
    public static OverrideLiteral INSTANCE = new OverrideLiteral();
}
