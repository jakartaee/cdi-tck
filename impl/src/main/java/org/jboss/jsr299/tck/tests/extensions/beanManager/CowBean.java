package org.jboss.jsr299.tck.tests.extensions.beanManager;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.PassivationCapable;

import org.jboss.jsr299.tck.literals.DefaultLiteral;

public class CowBean implements Bean<Cow>, PassivationCapable, Serializable {
    private static final long serialVersionUID = 6249623250272328272L;
    public static final String PASSIVATION_ID = "Cow-6249623250272328272L";
    private final Set<Annotation> qualifiers = new HashSet<Annotation>(Arrays.asList(new DefaultLiteral()));
    private final Set<Type> types = new HashSet<Type>(Arrays.<Type> asList(Cow.class));

    public Class<?> getBeanClass() {
        return Cow.class;
    }

    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }

    public String getName() {
        return "cow";
    }

    public Set<Annotation> getQualifiers() {
        return qualifiers;
    }

    public Class<? extends Annotation> getScope() {
        return Dependent.class;
    }

    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    public Set<Type> getTypes() {
        return types;
    }

    public boolean isAlternative() {
        return false;
    }

    public boolean isNullable() {
        return false;
    }

    public Cow create(CreationalContext<Cow> creationalContext) {
        return new Cow("Betsy");
    }

    public void destroy(Cow instance, CreationalContext<Cow> creationalContext) {
    }

    public String getId() {
        return PASSIVATION_ID;
    }

}
