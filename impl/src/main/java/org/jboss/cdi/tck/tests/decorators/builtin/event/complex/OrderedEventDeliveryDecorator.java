/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.decorators.builtin.event.complex;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.event.Event;
import javax.enterprise.event.NotificationOptions;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;

@Decorator
@SuppressWarnings("serial")
public class OrderedEventDeliveryDecorator<T> implements Event<T>, Serializable {

    private final Set<Annotation> qualifiers;
    private final BeanManager manager;
    private final ObserverMethodComparator comparator;

    public OrderedEventDeliveryDecorator(Set<Annotation> qualifiers, BeanManager manager, ObserverMethodComparator comparator) {
        this.qualifiers = qualifiers;
        this.manager = manager;
        this.comparator = comparator;
    }

    @Inject
    public OrderedEventDeliveryDecorator(@Delegate Event<T> delegate, BeanManager manager,
            OrderedEventDeliveryExtension extension) {
        this.qualifiers = new HashSet<Annotation>();
        this.manager = manager;
        this.comparator = new ObserverMethodComparator(extension);
    }

    @Override
    public void fire(T event) {
        Set<ObserverMethod<? super T>> observers = manager.resolveObserverMethods(event,
                this.qualifiers.toArray(new Annotation[qualifiers.size()]));
        List<ObserverMethod<? super T>> sortedObservers = new ArrayList<ObserverMethod<? super T>>(observers);
        Collections.sort(sortedObservers, comparator);
        for (ObserverMethod<? super T> observer : sortedObservers) {
            observer.notify(event);
        }
    }

    @Override
    public <U extends T> CompletionStage<U> fireAsync(U event) {
        return null;
    }

    @Override
    public <U extends T> CompletionStage<U> fireAsync(U event, NotificationOptions options) {
        return null;
    }

    @Override
    public Event<T> select(Annotation... qualifiers) {
        return new OrderedEventDeliveryDecorator<T>(mergeAnnotations(this.qualifiers, qualifiers), manager, comparator);
    }

    @Override
    public <U extends T> Event<U> select(Class<U> subtype, Annotation... qualifiers) {
        return new OrderedEventDeliveryDecorator<U>(mergeAnnotations(this.qualifiers, qualifiers), manager, comparator);
    }

    @Override
    public <U extends T> Event<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
        return new OrderedEventDeliveryDecorator<U>(mergeAnnotations(this.qualifiers, qualifiers), manager, comparator);
    }

    private Set<Annotation> mergeAnnotations(Set<Annotation> annotations, Annotation... additionalAnnotations) {
        Set<Annotation> result = new HashSet<Annotation>(annotations);
        for (Annotation annotation : additionalAnnotations) {
            result.add(annotation);
        }
        return result;
    }

}
