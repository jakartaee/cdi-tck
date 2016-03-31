/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.configurators.beanConfigurator;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanAttributes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBeanAttributes;
import javax.enterprise.inject.spi.builder.BeanConfigurator;
import javax.enterprise.inject.spi.builder.Builders;
import javax.enterprise.inject.spi.builder.InjectionPointBuilder;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class AfterBeanDiscoveryObserver {

    public static BeanAttributes<Zombie> zombieAttributes;
    public static BeanAttributes<Vampire> vampireAttributes;

    public static AnnotatedType<Vampire> vampireAnnotatedType;

    public static AnnotatedField<?> skeletonField;
    public static AnnotatedField<?> zombieField;
    public static AnnotatedField<?> ghostFieldInDungeon;
    public static AnnotatedField<?> ghostFieldInTower;
    public static AnnotatedField<?> vampireField;

    public void observeUndead(@Observes AfterBeanDiscovery abd, BeanManager bm) {
        // firstly, create a Skeleton bean
        BeanConfigurator<Skeleton> skeleton = abd.addBean();

        // set bean class, qualifier, stereotype
        skeleton.beanClass(Skeleton.class);
        skeleton.addQualifier(Undead.UndeadLiteral.INSTANCE);
        skeleton.addStereotype(Monster.class);

        // use builder to create injection points
        InjectionPointBuilder injectionPointBuilder = Builders.injectionPoint();
        injectionPointBuilder.configure().read(skeletonField)
            .addQualifier(Undead.UndeadLiteral.INSTANCE)
            .type(Skeleton.class);

        //set IP
        skeleton.addInjectionPoint(injectionPointBuilder.build());

        // set Supplier as producer
        skeleton.produceWith(MonsterController.skeletonSupplier);

        // set Consumer in dispose method
        skeleton.disposeWith(MonsterController.skeletonConsumer);

        BeanConfigurator<Zombie> zombie = abd.addBean();

        zombie.beanClass(Zombie.class);
        zombie.addQualifiers(Undead.UndeadLiteral.INSTANCE, Dangerous.DangerousLiteral.INSTANCE);
        zombie.addStereotype(Monster.class);

        // use builder to create injection points
        injectionPointBuilder.configure().read(zombieField)
            .addQualifiers(Undead.UndeadLiteral.INSTANCE, Dangerous.DangerousLiteral.INSTANCE)
            .type(Zombie.class);

        // replace the injection point in HauntedTower with the one in Dungeon
        zombie.injectionPoints(injectionPointBuilder.build());

        // set Function as a supplier
        zombie.produceWith(MonsterController.zombieProducingFunction);

        // set BiConsumer to destroyWith
        zombie.destroyWith(MonsterController.zombieConsumer);

        // make passivation capable
        zombie.id("zombie");

        BeanConfigurator<Ghost> ghost = abd.addBean();

        ghost.beanClass(Ghost.class);
        ghost.addQualifier(Undead.UndeadLiteral.INSTANCE);
        ghost.addStereotype(Monster.class);

        injectionPointBuilder.configure().read(ghostFieldInDungeon)
            .addQualifier(Undead.UndeadLiteral.INSTANCE)
            .type(Ghost.class);

        InjectionPoint dungeonIP = injectionPointBuilder.build();

        injectionPointBuilder.configure().read(ghostFieldInTower)
            .addQualifier(Undead.UndeadLiteral.INSTANCE)
            .type(Ghost.class);

        InjectionPoint towerIP = injectionPointBuilder.build();

        // add multiple IPs
        ghost.addInjectionPoints(dungeonIP, towerIP);

        // set producing
        ghost.producing(MonsterController.getGhostInstance());

        BeanConfigurator<Vampire> vampire = abd.addBean();

        vampire.beanClass(Vampire.class);
        vampire.addQualifier(Undead.UndeadLiteral.INSTANCE);
        vampire.addStereotype(Monster.class);

        injectionPointBuilder.configure().read(vampireField)
            .addQualifier(Undead.UndeadLiteral.INSTANCE)
            .type(Vampire.class);

        vampire.addInjectionPoint(injectionPointBuilder.build());

        // set createWith function
        vampire.createWith((CreationalContext<Vampire> creationalContext) -> {
            MonsterController.vampireInstanceCreated = true;
            return bm.createBean(vampireAttributes, Vampire.class, bm.getInjectionTargetFactory(vampireAnnotatedType)).create(creationalContext);
        });
    }

    public void observeZombieAttributes(@Observes ProcessBeanAttributes<Zombie> pba) {
        zombieAttributes = pba.getBeanAttributes();
    }

    public void observeVampireAttributes(@Observes ProcessBeanAttributes<Vampire> pba) {
        vampireAttributes = pba.getBeanAttributes();
    }

    public void observeDungeon(@Observes ProcessAnnotatedType<Dungeon> pat) {
        for (AnnotatedField<?> field : pat.getAnnotatedType().getFields()) {
            if (field.getJavaMember().getName().equals("skeleton")) {
                skeletonField = field;
            }
            if (field.getJavaMember().getName().equals("zombie")) {
                zombieField = field;
            }
            if (field.getJavaMember().getName().equals("ghost")) {
                ghostFieldInDungeon = field;
            }
            if (field.getJavaMember().getName().equals("vampire")) {
                vampireField = field;
            }
        }
    }

    public void observeHauntedTower(@Observes ProcessAnnotatedType<HauntedTower> pat) {
        for (AnnotatedField<?> field : pat.getAnnotatedType().getFields()) {
            if (field.getJavaMember().getName().equals("ghost")) {
                ghostFieldInTower = field;
            }
        }
    }

    public void observeVampire(@Observes ProcessAnnotatedType<Vampire> pat) {
        vampireAnnotatedType = pat.getAnnotatedType();
    }

}
