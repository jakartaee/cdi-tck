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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@Test
@SpecVersion(spec = "cdi", version = "2.0-EDR1")
public class BeanConfiguratorTest extends AbstractTest {

    @Inject
    Dungeon dungeon;

    @Inject
    HauntedTower tower;
    
    @Inject
    BeanManager manager;
    
    @Test
    @SpecAssertions({
        @SpecAssertion(section = Sections.BEAN_CONFIGURATOR, id = "a"),
        @SpecAssertion(section = Sections.BEAN_CONFIGURATOR, id = "f"),
        @SpecAssertion(section = Sections.BEAN_CONFIGURATOR, id = "g"),
        @SpecAssertion(section = Sections.BEAN_CONFIGURATOR, id = "h"),
        @SpecAssertion(section = Sections.BEAN_CONFIGURATOR, id = "i") })
    public void testCreationalAndDisposalMethods() {
        Bean<Skeleton> skeletonBean = getUniqueBean(Skeleton.class, Undead.UndeadLiteral.INSTANCE);
        CreationalContext<Skeleton> skeletonCreationalContext = getCurrentManager().createCreationalContext(skeletonBean);
        Skeleton skeleton = skeletonBean.create(skeletonCreationalContext);
        
        Bean<Zombie> zombieBean = getUniqueBean(Zombie.class, Undead.UndeadLiteral.INSTANCE, Dangerous.DangerousLiteral.INSTANCE);
        CreationalContext<Zombie> zombieCreationalContext = getCurrentManager().createCreationalContext(zombieBean);
        Zombie zombie = zombieBean.create(zombieCreationalContext);
        
        // verify creational methods were called
        assertTrue(MonsterController.skeletonProducerCalled);
        assertTrue(MonsterController.zombieProducerCalled);
        assertTrue(MonsterController.ghostInstanceObtained);
        assertTrue(MonsterController.vampireInstanceCreated);
        
        // verify destroy methods were called
        skeletonBean.destroy(skeleton, skeletonCreationalContext);
        zombieBean.destroy(zombie, zombieCreationalContext);
        assertTrue(MonsterController.zombieKilled);
        assertTrue(MonsterController.skeletonKilled);
    }
    
    @Test
    @SpecAssertions({
        @SpecAssertion(section = Sections.BEAN_CONFIGURATOR, id = "a"),
        @SpecAssertion(section = Sections.BEAN_CONFIGURATOR, id = "b"),
        @SpecAssertion(section = Sections.BEAN_CONFIGURATOR, id = "c"),
        @SpecAssertion(section = Sections.BEAN_CONFIGURATOR, id = "d") })
    public void testInjectionPoints() {
        // Dungeon should have Skeleton, Zombie, Ghost and Vampire injected
        assertTrue(dungeon.hasMonters());
        // HauntedTower should have Zombie IP removed and Ghost IP added
        assertFalse(tower.hasZombie());
        assertTrue(tower.hasGhost());
    }
    
    @Test
    @SpecAssertions({
        @SpecAssertion(section = Sections.BEAN_CONFIGURATOR, id = "a"),
        @SpecAssertion(section = Sections.BEAN_CONFIGURATOR, id = "e") })
    public void testPassivationCapability() {
        // BeanManager should be able to find a passivation capable bean
        assertNotNull(manager.getPassivationCapableBean("zombie"));
    }
}
