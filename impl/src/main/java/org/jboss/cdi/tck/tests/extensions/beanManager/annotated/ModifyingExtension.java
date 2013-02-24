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

package org.jboss.cdi.tck.tests.extensions.beanManager.annotated;

import static org.jboss.cdi.tck.util.AddForwardingAnnotatedTypeAction.buildId;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessSyntheticAnnotatedType;

import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.tests.extensions.beanManager.annotated.Alpha.AlphaLiteral;
import org.jboss.cdi.tck.tests.extensions.beanManager.annotated.Bravo.BravoLiteral;
import org.jboss.cdi.tck.tests.extensions.beanManager.annotated.Charlie.CharlieLiteral;
import org.jboss.cdi.tck.util.AddForwardingAnnotatedTypeAction;
import org.jboss.cdi.tck.util.annotated.AnnotatedTypeWrapper;

public class ModifyingExtension implements Extension {

    private static final String BASE_ID_BRAVO = ModifyingExtension.class.getName() + "_b";

    private static final String BASE_ID_CHARLIE = ModifyingExtension.class.getName() + "_c";

    private AnnotatedType<Foo> aplha;

    private AnnotatedType<Foo> bravo;

    private AnnotatedType<Foo> charlie;

    private AnnotatedType<Bar> bar;

    private List<AnnotatedType<Foo>> allFoo = new ArrayList<AnnotatedType<Foo>>();

    /**
     * Add two {@link AnnotatedType}s derived from {@link Foo}.
     *
     * @param event
     * @param beanManager
     */
    public void observeBeforeBeanDiscovery(@Observes BeforeBeanDiscovery event, final BeanManager beanManager) {

        new AddForwardingAnnotatedTypeAction<Foo>() {

            @Override
            public String getBaseId() {
                return BASE_ID_BRAVO;
            }

            @Override
            public AnnotatedType<Foo> delegate() {
                return new AnnotatedTypeWrapper<Foo>(beanManager.createAnnotatedType(Foo.class), false, BravoLiteral.INSTANCE,
                        AnyLiteral.INSTANCE);
            }

        }.perform(event);

        new AddForwardingAnnotatedTypeAction<Foo>() {

            @Override
            public String getBaseId() {
                return BASE_ID_CHARLIE;
            }

            @Override
            public AnnotatedType<Foo> delegate() {
                return new AnnotatedTypeWrapper<Foo>(beanManager.createAnnotatedType(Foo.class), false, CharlieLiteral.INSTANCE);
            }

        }.perform(event);

    }

    /**
     * Change the discovered {@link AnnotatedType} for {@link Foo}.
     *
     * @param event
     */
    public void observeProcessAnnotatedType(@Observes ProcessAnnotatedType<Foo> event) {
        if (!(event instanceof ProcessSyntheticAnnotatedType<?>)) {
            // Wrap the default annotated type
            event.setAnnotatedType(new AnnotatedTypeWrapper<Foo>(event.getAnnotatedType(), false, AlphaLiteral.INSTANCE));
        }
    }

    /**
     * Store the result, don't verify anything since it's more transparent to have assertions in the test class methods.
     *
     * The methods {@link BeanManager#getAnnotatedType(Class, String)} and {@link BeanManager#getAnnotatedTypes(Class)} will be
     * probably placed on the {@link AfterBeanDiscovery}. See also CDI-83.
     *
     * @param event
     * @param beanManager
     */
    public void observeAfterBeanDiscovery(@Observes AfterBeanDiscovery event, BeanManager beanManager) {

        for (AnnotatedType<Foo> annotatedType : event.getAnnotatedTypes(Foo.class)) {
            allFoo.add(annotatedType);
        }
        bravo = event.getAnnotatedType(Foo.class, buildId(BASE_ID_BRAVO, Foo.class));
        charlie = event.getAnnotatedType(Foo.class, buildId(BASE_ID_CHARLIE, Foo.class));
        aplha = event.getAnnotatedType(Foo.class, null);
        bar = event.getAnnotatedType(Bar.class, null);
    }

    public AnnotatedType<Foo> getAplha() {
        return aplha;
    }

    public AnnotatedType<Foo> getBravo() {
        return bravo;
    }

    public AnnotatedType<Foo> getCharlie() {
        return charlie;
    }

    public List<AnnotatedType<Foo>> getAllFoo() {
        return allFoo;
    }

    public AnnotatedType<Bar> getBar() {
        return bar;
    }

}
