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

import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.literals.InjectLiteral;
import org.jboss.cdi.tck.util.annotated.*;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.*;
import javax.enterprise.util.AnnotationLiteral;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroceryWrapper extends AnnotatedTypeWrapper<Grocery> {
    private final Set<Type> typeClosure = new HashSet<Type>();
    private static boolean getBaseTypeOfFruitFieldUsed = false;
    private static boolean getBaseTypeOfInitializerTropicalFruitParameterUsed = false;
    private static boolean getBaseTypeOfBillDisposerParameterUsed = false;
    private static boolean getBaseTypeOfObserverInjectionPointUsed = false;
    private static boolean getBaseTypeOfObserverParameterUsed = false;
    private static boolean getTypeClosureUsed = false;
    private static boolean getTypeClosureOfProducerMethodUsed = false;

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
                constructors.add(wrapConstructor(constructor, new InjectLiteral()));
            } else {
                constructors.add(constructor);
            }
        }
        return constructors;
    }

    @Override
    public Set<AnnotatedField<? super Grocery>> getFields() {
        Set<AnnotatedField<? super Grocery>> fields = new HashSet<AnnotatedField<? super Grocery>>();
        for (AnnotatedField<? super Grocery> field : super.getFields()) {
            if (field.getJavaMember().getName().equals("vegetables")) {
                fields.add(wrapField(field, new InjectLiteral()));
            } else if (field.getJavaMember().getName().equals("fruit")) {
                fields.add(wrapFruitField(field, new CheapLiteral()));
            } else if (field.getBaseType().equals(Bread.class)) {
                fields.add(wrapField(field, new AnnotationLiteral<Produces>() {
                }));
            } else {
                fields.add(field);
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
                methods.add(wrapMethod(method, false, new AnnotationLiteral<Produces>() {
                }));
            } else if (method.getJavaMember().getName().equals("getYogurt")) {
                // wrap the method and its parameters
                AnnotatedMethod<? super Grocery> wrappedMethod = wrapMethod(method, false, new ExpensiveLiteral(),
                        new AnnotationLiteral<Produces>() {
                        }
                );
                methods.add(wrapMethodParameters(wrappedMethod, false, new Annotation[] { new CheapLiteral() }));
            } else if (method.getJavaMember().getName().equals("nonInjectAnnotatedInitializer")) {
                methods.add(wrapMethod(method, false, new InjectLiteral()));
            } else if (method.getJavaMember().getName().equals("initializer")) {

                AnnotatedMethodWrapper<? super Grocery> methodWrapper = new AnnotatedMethodWrapper(method, true);
                methodWrapper.replaceParameters(new AnnotatedParameterWrapper(methodWrapper.getParameter(0), false,
                        new CheapLiteral()) {
                    @Override
                    public Type getBaseType() {
                        getBaseTypeOfInitializerTropicalFruitParameterUsed = true;
                        return TropicalFruit.class;
                    }
                });
                methods.add(methodWrapper);

            } else if (method.getJavaMember().getName().equals("observer1")) {
                Annotation[] secondParameterAnnotations = new Annotation[] { new CheapLiteral() };
                methods.add(wrapMethodParameters(method, false, observesAnnotation(), secondParameterAnnotations));
            } else if (method.getJavaMember().getName().equals("observer2")) {
                methods.add(wrapMethodParameters(method, true, new Annotation[] { new ExpensiveLiteral() }));
            } else if (method.getJavaMember().getName().equals("observerMilk")) {
                AnnotatedMethodWrapper<? super Grocery> methodWrapper = new AnnotatedMethodWrapper(method, true);
                methodWrapper.replaceParameters(new AnnotatedParameterWrapper(methodWrapper.getParameter(0), true),
                        new AnnotatedParameterWrapper(methodWrapper.getParameter(1), true) {
                            @Override
                            public Type getBaseType() {
                                getBaseTypeOfObserverInjectionPointUsed = true;
                                return TropicalFruit.class;
                            }
                        });
                methods.add(methodWrapper);
            } else if (method.getJavaMember().getName().equals("destroyBill")) {
                AnnotatedMethodWrapper<? super Grocery> methodWrapper = new AnnotatedMethodWrapper(method, true);
                // replace the second parameter and keep the first one
                methodWrapper = methodWrapper.replaceParameters(new AnnotatedParameterWrapper(methodWrapper.getParameter(0), true),
                        new AnnotatedParameterWrapper(methodWrapper.getParameter(1), true, AnyLiteral.INSTANCE) {
                            @Override
                            public Type getBaseType() {
                                getBaseTypeOfBillDisposerParameterUsed = true;
                                return TropicalFruit.class;
                            }
                        });
                methods.add(methodWrapper);

            } else if (method.getJavaMember().getName().equals("observesVegetable")) {
                AnnotatedMethodWrapper<? super Grocery> methodWrapper = new AnnotatedMethodWrapper(method, true);
                methodWrapper = methodWrapper.replaceParameters(new AnnotatedParameterWrapper(methodWrapper.getParameter(0), true) {
                    @Override
                    public Type getBaseType() {
                        getBaseTypeOfObserverParameterUsed = true;
                        return Carrot.class;
                    }
                });
                methods.add(methodWrapper);

            } else if (method.getJavaMember().getName().equals("destroyVegetable")) {
                AnnotatedMethodWrapper<? super Grocery> methodWrapper = new AnnotatedMethodWrapper(method, true);
                methodWrapper = methodWrapper.replaceParameters(new AnnotatedParameterWrapper(methodWrapper.getParameter(0), true, new CheapLiteral()) {
                    @Override
                    public Type getBaseType() {
                        return Carrot.class;
                    }
                });
                methods.add(methodWrapper);

            } else if (method.getJavaMember().getName().equals("destroyYogurt")) {
                AnnotatedMethodWrapper<? super Grocery> methodWrapper = new AnnotatedMethodWrapper(method, true);
                methodWrapper = methodWrapper.replaceParameters(
                        new AnnotatedParameterWrapper(methodWrapper.getParameter(0), false, new Annotation[] { new ExpensiveLiteral(), new Disposes() {
                            public Class<? extends Annotation> annotationType() {
                                return Disposes.class;
                            }
                        } }));
                methods.add(methodWrapper);

            } else if (method.getJavaMember().getName().equals("createVegetable")) {

                AnnotatedMethodWrapper<? super Grocery> methodWrapper = new AnnotatedMethodWrapper(method, true) {
                    @Override
                    public Set<Type> getTypeClosure() {
                        Set<Type> types = new HashSet<Type>();
                        types.add(Carrot.class);
                        getTypeClosureOfProducerMethodUsed = true;
                        return types;
                    }
                };
                methods.add(methodWrapper);

            } else {
                methods.add(method);
            }
        }
        return methods;
    }

    private <Y> AnnotatedConstructor<Y> wrapConstructor(AnnotatedConstructor<Y> delegate, Annotation... annotations) {
        return new AnnotatedConstructorWrapper<Y>(delegate, false, annotations);
    }

    private <Y> AnnotatedField<Y> wrapField(AnnotatedField<Y> delegate, Annotation... annotations) {
        return new AnnotatedFieldWrapper<Y>(delegate, false, annotations);
    }

    private <Y> AnnotatedField<Y> wrapFruitField(AnnotatedField<Y> delegate, Annotation... annotations) {
        return new AnnotatedFieldWrapper<Y>(delegate, true, annotations) {
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

    private <Y> AnnotatedMethodWrapper<Y> wrapMethod(AnnotatedMethod<Y> delegate, boolean keepOriginalAnnotations,
            Annotation... annotations) {
        return new AnnotatedMethodWrapper<Y>(delegate, keepOriginalAnnotations, annotations);
    }

    /**
     * This method allows you to add a set of Annotations to every method parameter. Note that the method will remove all
     * method-level annotations.
     */
    private <Y> AnnotatedMethodWrapper<Y> wrapMethodParameters(AnnotatedMethod<Y> delegate,
            final boolean keepOriginalAnnotations, final Annotation[]... annotations) {
        return new AnnotatedMethodWrapper<Y>(delegate, true) {
            @Override
            public List<AnnotatedParameter<Y>> getParameters() {
                List<AnnotatedParameter<Y>> parameters = new ArrayList<AnnotatedParameter<Y>>();
                for (AnnotatedParameter<Y> parameter : super.getParameters()) {
                    parameters.add(new AnnotatedParameterWrapper<Y>(parameter, keepOriginalAnnotations, annotations[parameter
                            .getPosition()]));
                }
                return parameters;
            }
        };
    }

    public Annotation[] observesAnnotation() {
        return new Annotation[] { new Observes() {

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

    public static boolean isGetBaseTypeOfBillDisposerParameterUsed() {
        return getBaseTypeOfBillDisposerParameterUsed;
    }

    public static boolean isGetBaseTypeOfObserverInjectionPointUsed() {
        return getBaseTypeOfObserverInjectionPointUsed;
    }

    public static boolean isGetTypeClosureOfProducerMethodUsed() {
        return getTypeClosureOfProducerMethodUsed;
    }

    public static boolean isGetBaseTypeOfObserverParameterUsed() {
        return getBaseTypeOfObserverParameterUsed;
    }

}
