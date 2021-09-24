package org.jboss.cdi.tck.tests.full.extensions.beanManager;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.inject.spi.PassivationCapable;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CowBean implements Bean<Cow>, PassivationCapable, Serializable {
    private static final long serialVersionUID = 6249623250272328272L;
    public static final String PASSIVATION_ID = "Cow-6249623250272328272L";
    private final Set<Annotation> qualifiers = new HashSet<Annotation>(Arrays.asList(Default.Literal.INSTANCE));
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
