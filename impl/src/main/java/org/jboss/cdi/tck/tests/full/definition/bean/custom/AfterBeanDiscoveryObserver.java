/*
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
package org.jboss.cdi.tck.tests.full.definition.bean.custom;

import java.lang.reflect.Type;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.*;

public class AfterBeanDiscoveryObserver implements Extension {

    public static final IntegerBean integerBean = new IntegerBean();

    public void afterDiscovery(@Observes AfterBeanDiscovery event, BeanManager beanManager) {
        event.addBean(integerBean);
        event.addBean(new FooBean(getAnnotatedField(beanManager, Integer.class), getAnnotatedField(beanManager, Bar.class),
                true));

        // register custom alternative via configurator but don't select it
        event.addBean()
            .beanClass(SomeBean.class)
            .types(SomeBean.class, AlternativeSomeBean.class, Object.class)
            .scope(ApplicationScoped.class)
            .alternative(true)
            .produceWith(obj -> new AlternativeSomeBean());
    }

    private AnnotatedField<?> getAnnotatedField(BeanManager beanManager, Type memberType) {

        for (AnnotatedField<?> field : beanManager.createAnnotatedType(Foo.class).getFields()) {
            if (field.getJavaMember().getType().equals(memberType)) {
                return field;
            }
        }
        return null;
    }

    public void afterTypeDiscovery(@Observes AfterTypeDiscovery event) {
        event.getAlternatives().add(Integer.class);
    }

}
