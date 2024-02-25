/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.full.extensions.afterBeanDiscovery.annotated;

import static org.jboss.cdi.tck.util.AddForwardingAnnotatedTypeAction.buildId;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.ProcessSyntheticAnnotatedType;

import org.jboss.cdi.tck.tests.full.extensions.afterBeanDiscovery.annotated.Alpha.AlphaLiteral;
import org.jboss.cdi.tck.tests.full.extensions.afterBeanDiscovery.annotated.Bravo.BravoLiteral;
import org.jboss.cdi.tck.tests.full.extensions.afterBeanDiscovery.annotated.Charlie.CharlieLiteral;
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
                        Any.Literal.INSTANCE);
            }

        }.perform(event);

        new AddForwardingAnnotatedTypeAction<Foo>() {

            @Override
            public String getBaseId() {
                return BASE_ID_CHARLIE;
            }

            @Override
            public AnnotatedType<Foo> delegate() {
                return new AnnotatedTypeWrapper<Foo>(beanManager.createAnnotatedType(Foo.class), false,
                        CharlieLiteral.INSTANCE);
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
     * The methods {@link AfterBeanDiscovery#getAnnotatedType(Class, String)} and
     * {@link AfterBeanDiscovery#getAnnotatedTypes(Class)} will be
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
