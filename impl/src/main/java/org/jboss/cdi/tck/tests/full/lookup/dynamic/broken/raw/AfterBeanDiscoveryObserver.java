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
package org.jboss.cdi.tck.tests.full.lookup.dynamic.broken.raw;

import java.lang.reflect.Type;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;

public class AfterBeanDiscoveryObserver implements Extension {

    public void afterDiscovery(@Observes AfterBeanDiscovery event, BeanManager beanManager) {
        event.addBean(new CustomBarBean(getAnnotatedField(beanManager, Instance.class)));
    }

    private AnnotatedField<?> getAnnotatedField(BeanManager beanManager, Type memberType) {

        for (AnnotatedField<?> field : beanManager.createAnnotatedType(Bar.class).getFields()) {
            if (field.getJavaMember().getType().equals(memberType)) {
                return field;
            }
        }
        return null;
    }

}
