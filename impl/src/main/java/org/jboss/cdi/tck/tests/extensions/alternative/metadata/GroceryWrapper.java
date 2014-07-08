/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.tests.extensions.alternative.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.util.AnnotationLiteral;
import org.jboss.cdi.tck.literals.InjectLiteral;
import org.jboss.cdi.tck.util.annotated.AnnotatedConstructorWrapper;
import org.jboss.cdi.tck.util.annotated.AnnotatedFieldWrapper;
import org.jboss.cdi.tck.util.annotated.AnnotatedMethodWrapper;
import org.jboss.cdi.tck.util.annotated.AnnotatedParameterWrapper;
import org.jboss.cdi.tck.util.annotated.AnnotatedTypeWrapper;

public class GroceryWrapper extends AnnotatedTypeWrapper<Grocery> {
    private final Set<Type> typeClosure = new HashSet<Type>();
    private static boolean getBaseTypeOfFruitFieldUsed = false;
    private static boolean getBaseTypeOfInitializerTropicalFruitParameterUsed = false;
    private static boolean getTypeClosureUsed = false;

    public GroceryWrapper(AnnotatedType<Grocery> delegate) {
        super(delegate, false, new AnnotationLiteral<RequestScoped>() {
        }, new CheapLiteral(), new AnnotationLiteral<NamedStereotype>() {
        }, new AnnotationLiteral<GroceryInterceptorBinding>() {
        });
        typeClosure.add(Grocery.class);
        typeClosure.add(Object.class);
    }

    @Override
    public Set<Type> getTypeClosure() {
        getTypeClosureUsed = true;
        return typeClosure;
    }

    @Override
    public Set<AnnotatedConstructor<Grocery>> getConstructors() {
        Set<AnnotatedConstructor<Grocery>> constructors = new HashSet<AnnotatedConstructor<Grocery>>();
        for (AnnotatedConstructor<Grocery> constructor : super.getConstructors()) {
            if (constructor.getParameters().size() == 1) {
                AnnotatedConstructorWrapper<Grocery> constructorWrapper = new AnnotatedConstructorWrapper<Grocery>(constructor, this, true,
                        new InjectLiteral());
                constructors.add(constructorWrapper);
            } else {
                AnnotatedConstructorWrapper<Grocery> constructorWrapper = new AnnotatedConstructorWrapper<Grocery>(constructor, this, true);
                constructors.add(constructorWrapper);
            }
        }
        return constructors;
    }

    @Override
    public Set<AnnotatedField<? super Grocery>> getFields() {
        Set<AnnotatedField<? super Grocery>> fields = new HashSet<AnnotatedField<? super Grocery>>();
        for (AnnotatedField<? super Grocery> field : super.getFields()) {
            if (field.getBaseType().equals(Vegetables.class)) {
                fields.add(wrapField(field, this, new InjectLiteral()));
            } else if (field.getJavaMember().getName().equals("fruit")) {
                fields.add(wrapFruitField(field, this, new CheapLiteral()));
            } else if (field.getBaseType().equals(Bread.class)) {
                fields.add(wrapField(field, this, new AnnotationLiteral<Produces>() {
                }));
            } else {
                fields.add(new AnnotatedFieldWrapper(field, this, true));
            }
        }
        return fields;
    }

    @SuppressWarnings({ "unchecked", "rawtypes", "serial" })
    @Override
    public Set<AnnotatedMethod<? super Grocery>> getMethods() {
        Set<AnnotatedMethod<? super Grocery>> methods = new HashSet<AnnotatedMethod<? super Grocery>>();
        for (AnnotatedMethod<? super Grocery> method : super.getMethods()) {
            if (method.getJavaMember().getName().equals("getMilk")) {

                AnnotatedMethodWrapper<? super Grocery> wrappedMethod = new AnnotatedMethodWrapper(method, this, false,
                        new Annotation[] { new AnnotationLiteral<Produces>() {
                        } });
                methods.add(wrappedMethod);
            } else if (method.getJavaMember().getName().equals("getYogurt")) {
                // wrap the method and its parameters
                AnnotatedMethodWrapper<? super Grocery> wrappedMethod = new AnnotatedMethodWrapper(method, this, false, new ExpensiveLiteral(),
                        new AnnotationLiteral<Produces>() {
                        });
                methods.add(wrapMethodParameters(method.getParameters(), wrappedMethod, false, new Annotation[] { new CheapLiteral() }));
            } else if (method.getJavaMember().getName().equals("nonInjectAnnotatedInitializer")) {
                AnnotatedMethodWrapper<? super Grocery> wrappedMethod = new AnnotatedMethodWrapper(method, this, true,
                        new Annotation[] { new InjectLiteral() });
                methods.add(wrappedMethod);
            } else if (method.getJavaMember().getName().equals("initializer")) {

                AnnotatedMethodWrapper<? super Grocery> methodWrapper = new AnnotatedMethodWrapper(method, this, true);

                methodWrapper.replaceParameters(new AnnotatedParameterWrapper(method.getParameters().get(0), methodWrapper, false,
                        new CheapLiteral()) {
                    @Override
                    public Type getBaseType() {
                        getBaseTypeOfInitializerTropicalFruitParameterUsed = true;
                        return TropicalFruit.class;
                    }
                });
                methods.add(methodWrapper);

            } else if (method.getJavaMember().getName().equals("observer1")) {
                Annotation[] firstParameterAnnotation = new Annotation[] { new Observes() {

                    public TransactionPhase during() {
                        return TransactionPhase.IN_PROGRESS;
                    }

                    public Reception notifyObserver() {
                        return Reception.ALWAYS;
                    }

                    public Class<? extends Annotation> annotationType() {
                        return Observes.class;
                    }

                } };
                Annotation[] secondParameterAnnotations = new Annotation[] { new CheapLiteral() };
                AnnotatedMethodWrapper<? super Grocery> methodWrapper = new AnnotatedMethodWrapper(method, this, true);
                methods.add(wrapMethodParameters(method.getParameters(), methodWrapper, false, firstParameterAnnotation[0], secondParameterAnnotations[0]));
            } else if (method.getJavaMember().getName().equals("observer2")) {
                AnnotatedMethodWrapper<? super Grocery> methodWrapper = new AnnotatedMethodWrapper(method, this, true);
                methods.add(wrapMethodParameters(method.getParameters(), methodWrapper, true, new Annotation[] { new ExpensiveLiteral() }));
            } else {
                methods.add(new AnnotatedMethodWrapper(method, this, true));
            }
        }
        return methods;
    }

    private <Y> AnnotatedFieldWrapper<Y> wrapField(AnnotatedField<? super Y> delegate, AnnotatedTypeWrapper<Y> declaringType, Annotation... annotations) {
        return new AnnotatedFieldWrapper(delegate, declaringType, false, annotations);
    }

    private <Y> AnnotatedFieldWrapper<Y> wrapFruitField(AnnotatedField<? super Y> delegate, AnnotatedTypeWrapper<Y> declaringType, Annotation... annotations) {
        return new AnnotatedFieldWrapper(delegate, declaringType, true, annotations) {
            @Override
            public Type getBaseType() {
                getBaseTypeOfFruitFieldUsed = true;
                return TropicalFruit.class;
            }

            @SuppressWarnings("serial")
            @Override
            public Set<Type> getTypeClosure() {
                return new HashSet<Type>() {
                    {
                        add(Object.class);
                        add(Fruit.class);
                        add(TropicalFruit.class);
                    }
                };
            }

        };
    }

    /**
     * This method allows you to add a set of Annotations to every method parameter. Note that the method will remove all
     * method-level annotations.
     */
    private <Y> AnnotatedMethodWrapper<Y> wrapMethodParameters(List<AnnotatedParameter<Y>> params, AnnotatedMethodWrapper declaringMethod,
            final boolean keepOriginalAnnotations, final Annotation... annotations) {

        List<AnnotatedParameter<Y>> wrappedParams = new ArrayList<AnnotatedParameter<Y>>();
        for (AnnotatedParameter<Y> param : params) {
            wrappedParams.add(new AnnotatedParameterWrapper(param, declaringMethod, keepOriginalAnnotations, annotations[param.getPosition()]));
        }

        declaringMethod.replaceParameters(wrappedParams.toArray(new AnnotatedParameter[wrappedParams.size()]));
        return declaringMethod;
    }

    public static boolean isGetBaseTypeOfFruitFieldUsed() {
        return getBaseTypeOfFruitFieldUsed;
    }

    public static boolean isGetBaseTypeOfInitializerTropicalFruitParameterUsed() {
        return getBaseTypeOfInitializerTropicalFruitParameterUsed;
    }

    public static boolean isGetTypeClosureUsed() {
        return getTypeClosureUsed;
    }
}
