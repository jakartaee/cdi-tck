/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.configurators.bean.alternativePriority;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanAttributes;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.inject.spi.ProcessSyntheticBean;
import jakarta.enterprise.inject.spi.configurator.BeanConfigurator;
import org.jboss.cdi.tck.tests.full.extensions.configurators.bean.Bogey;
import org.jboss.cdi.tck.tests.full.extensions.configurators.bean.Dangerous;
import org.jboss.cdi.tck.tests.full.extensions.configurators.bean.DesireToHurtHumans;
import org.jboss.cdi.tck.tests.full.extensions.configurators.bean.Ghost;
import org.jboss.cdi.tck.tests.full.extensions.configurators.bean.Monster;
import org.jboss.cdi.tck.tests.full.extensions.configurators.bean.MonsterController;
import org.jboss.cdi.tck.tests.full.extensions.configurators.bean.Skeleton;
import org.jboss.cdi.tck.tests.full.extensions.configurators.bean.Undead;
import org.jboss.cdi.tck.tests.full.extensions.configurators.bean.Vampire;
import org.jboss.cdi.tck.tests.full.extensions.configurators.bean.Weapon;
import org.jboss.cdi.tck.tests.full.extensions.configurators.bean.Werewolf;
import org.jboss.cdi.tck.tests.full.extensions.configurators.bean.Zombie;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AlternativePriorityExtension implements Extension {
    private AtomicBoolean syntheticAlternativeProcessed = new AtomicBoolean(false);

    public void registerSyntheticBean(@Observes AfterBeanDiscovery abd) {
        abd.addBean()
                .beanClass(Bar.class)
                .types(Bar.class, Foo.class, Object.class)
                .scope(Dependent.class)
                .alternative(true)
                .priority(1)
                .createWith(ctx -> new Bar());

    }

    void observeSyntheticBean(@Observes ProcessSyntheticBean<Bar> event) {
        syntheticAlternativeProcessed.set(true);
    }

    public boolean isSyntheticAlternativeProcessed() {
        return syntheticAlternativeProcessed.get();
    }
}
