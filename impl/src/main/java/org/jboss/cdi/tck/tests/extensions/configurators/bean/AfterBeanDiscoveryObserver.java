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
package org.jboss.cdi.tck.tests.extensions.configurators.bean;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanAttributes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.configurator.BeanConfigurator;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class AfterBeanDiscoveryObserver implements Extension {
    
    public void observeUndead(@Observes AfterBeanDiscovery abd, BeanManager bm) {
        // create Skeleton bean
        configureSkeleton(bm, abd.addBean());

        // create Zombie bean
        configureZombie(bm, abd.addBean());

        // create Ghost bean
        configureGhost(bm, abd.addBean());

        // create Vampire bean
        configureVampire(bm, abd.addBean());
    }
    
    private void configureSkeleton(BeanManager bm, BeanConfigurator<Skeleton> skeleton) {
        // set bean class, qualifier, stereotype, scope       
        // no read() method used here, all set manually
        skeleton.beanClass(Skeleton.class);
        skeleton.addQualifier(Undead.UndeadLiteral.INSTANCE);
        skeleton.addStereotype(Monster.class);
        skeleton.scope(RequestScoped.class);
        skeleton.addTransitiveTypeClosure(Skeleton.class);
        
        for (AnnotatedField<? super Skeleton> field : bm.createAnnotatedType(Skeleton.class).getFields()) {
            if (field.getJavaMember().getType().equals(DesireToHurtHumans.class)) {
                skeleton.addInjectionPoint(bm.createInjectionPoint(field));
                break;
            }
        }

        // set Supplier as producer
        skeleton.produceWith(MonsterController.skeletonSupplier);

        // set Consumer in dispose method
        skeleton.disposeWith(MonsterController.skeletonConsumer);
    }
    
    private void configureZombie(BeanManager bm, BeanConfigurator<Zombie> zombie) {
        // init with read() method, then set values
        zombie.read(bm.createAnnotatedType(Zombie.class));
        zombie.beanClass(Zombie.class);
        zombie.addQualifiers(Undead.UndeadLiteral.INSTANCE, Dangerous.DangerousLiteral.INSTANCE);
        zombie.addStereotype(Monster.class);
        zombie.scope(RequestScoped.class);
        
        InjectionPoint zombieWeaponIP = null;
        InjectionPoint zombieDesireIP = null;
        for (AnnotatedField<? super Zombie> field : bm.createAnnotatedType(Zombie.class).getFields()) {
            if (field.getJavaMember().getType().equals(DesireToHurtHumans.class)) {
                zombieDesireIP = bm.createInjectionPoint(field);
            }
            if (field.getJavaMember().getType().equals(Weapon.class)) {
                zombieWeaponIP = bm.createInjectionPoint(field);
            }
        }
        // add multiple injection points
        zombie.addInjectionPoints(zombieWeaponIP, zombieDesireIP);

        // set Function as a supplier
        zombie.produceWith(MonsterController.zombieProducingFunction);

        // set BiConsumer to destroyWith
        zombie.destroyWith(MonsterController.zombieConsumer);

        // make passivation capable
        zombie.id("zombie");
    }
    
    private void configureGhost(BeanManager bm, BeanConfigurator<Ghost> ghost) {
        // creation using read from bean attributes
        ghost.read(bm.createBeanAttributes(bm.createAnnotatedType(Ghost.class)));
        ghost.beanClass(Ghost.class);
        ghost.addQualifier(Undead.UndeadLiteral.INSTANCE);
        ghost.addStereotype(Monster.class);
        ghost.scope(RequestScoped.class);

        // test replacement of IPs
        InjectionPoint ghostWeaponIP = null;
        InjectionPoint ghostDesireIP = null;
        for (AnnotatedField<? super Ghost> field : bm.createAnnotatedType(Ghost.class).getFields()) {
            if (field.getJavaMember().getType().equals(DesireToHurtHumans.class)) {
                ghostDesireIP = bm.createInjectionPoint(field);
            }
            if (field.getJavaMember().getType().equals(Weapon.class)) {
                ghostWeaponIP = bm.createInjectionPoint(field);
            }
        }
        // firstly add one IP, then replace it with other
        ghost.addInjectionPoint(ghostWeaponIP);
        ghost.injectionPoints(ghostDesireIP);

        // set producing
        ghost.produceWith(MonsterController.getGhostInstance);
    }
    
    private void configureVampire(BeanManager bm, BeanConfigurator<Vampire> vampire) {
        vampire.read(bm.createAnnotatedType(Vampire.class));
        vampire.beanClass(Vampire.class);
        vampire.addQualifier(Undead.UndeadLiteral.INSTANCE);
        vampire.addStereotype(Monster.class);
        vampire.scope(RequestScoped.class);

        // set createWith function
        vampire.createWith((CreationalContext<Vampire> creationalContext) -> {
            MonsterController.vampireInstanceCreated = true;
            AnnotatedType<Vampire> at = bm.createAnnotatedType(Vampire.class);
            BeanAttributes<Vampire> ba = bm.createBeanAttributes(at);
            return bm.createBean(ba, Vampire.class, bm.getInjectionTargetFactory(at)).create(creationalContext);
        });
    }
}
