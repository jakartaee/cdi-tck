package org.jboss.cdi.tck.literals;

import jakarta.enterprise.util.AnnotationLiteral;

import java.lang.annotation.Inherited;

public class InheritedLiteral extends AnnotationLiteral<Inherited> implements Inherited {
    public static InheritedLiteral INSTANCE = new InheritedLiteral();
}
