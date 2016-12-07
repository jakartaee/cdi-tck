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

import static org.jboss.cdi.tck.cdi.Sections.AFTER_BEAN_DISCOVERY;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_BEAN;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@Test
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class BeanConfiguratorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BeanConfiguratorTest.class)
                .withExtension(LifecycleObserver.class).build();
    }

    @Inject
    Dungeon dungeon;

    @Test
    @SpecAssertions({
            @SpecAssertion(section = Sections.BEAN_CONFIGURATOR, id = "a"),
            @SpecAssertion(section = Sections.BEAN_CONFIGURATOR, id = "f"),
            @SpecAssertion(section = Sections.BEAN_CONFIGURATOR, id = "g") })
    public void testCreationalAndDisposalMethods() {
        Bean<Skeleton> skeletonBean = getUniqueBean(Skeleton.class, Undead.UndeadLiteral.INSTANCE);
        CreationalContext<Skeleton> skeletonCreationalContext = getCurrentManager().createCreationalContext(skeletonBean);
        Skeleton skeleton = skeletonBean.create(skeletonCreationalContext);

        Bean<Zombie> zombieBean = getUniqueBean(Zombie.class, Undead.UndeadLiteral.INSTANCE, Dangerous.DangerousLiteral.INSTANCE);
        CreationalContext<Zombie> zombieCreationalContext = getCurrentManager().createCreationalContext(zombieBean);
        Zombie zombie = zombieBean.create(zombieCreationalContext);

        // instantiate Ghost and Vampire to verify their creational methods were called
        spawnMonster(Ghost.class, Undead.UndeadLiteral.INSTANCE);
        spawnMonster(Vampire.class, Undead.UndeadLiteral.INSTANCE);

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
        // Dungeon should have Skeleton, Zombie, Ghost and Vampire Injected
        assertTrue(dungeon.hasAllMonters());

        // skeleton has one IP only
        assertTrue(getUniqueBean(Skeleton.class, Undead.UndeadLiteral.INSTANCE).getInjectionPoints().size() == 1);
        // zombie has two different
        assertTrue(getUniqueBean(Zombie.class, Undead.UndeadLiteral.INSTANCE, Dangerous.DangerousLiteral.INSTANCE).getInjectionPoints().size() == 2);
        // ghost has two but one was replaces with the other, resulting in only one IP
        Set<InjectionPoint> ghostIP = getUniqueBean(Ghost.class, Undead.UndeadLiteral.INSTANCE).getInjectionPoints();
        assertTrue(ghostIP.size() == 1);
        assertTrue(ghostIP.iterator().next().getAnnotated().getTypeClosure().contains(DesireToHurtHumans.class));
    }

    @Test
    @SpecAssertions({
            @SpecAssertion(section = Sections.BEAN_CONFIGURATOR, id = "a"),
            @SpecAssertion(section = Sections.BEAN_CONFIGURATOR, id = "e") })
    public void testPassivationCapability() {
        // BeanManager should be able to find a passivation capable bean
        assertNotNull(getCurrentManager().getPassivationCapableBean("zombie"));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = AFTER_BEAN_DISCOVERY, id = "da"), @SpecAssertion(section = PROCESS_BEAN, id = "eca") })
    public void processSynthethicBeanEventFired(LifecycleObserver extension) {
        assertTrue(extension.isSkeletonPSBFired());
        assertTrue(extension.isVampirePSBFired());
        assertTrue(extension.isZombiePSBFired());
        assertTrue(extension.isGhostPSBFired());
    }

    // helper method to create a bean
    private <T> void spawnMonster(Class<T> type, Annotation... annotation) {
        Bean<T> bean = getUniqueBean(type, annotation);
        CreationalContext<T> creationalContext = getCurrentManager().createCreationalContext(bean);
        bean.create(creationalContext);
    }
}
