/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.interceptors.tests.contract.interceptorLifeCycle;

import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INTERCEPTOR_LIFECYCLE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "interceptors", version = "1.2")
public class InterceptorLifeCycleTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InterceptorLifeCycleTest.class).build();
    }

    @Test
    @SpecAssertion(section = INTERCEPTOR_LIFECYCLE, id = "ca")
    public void testInterceptorMethodsCalledAfterDependencyInjection() {
        createCallAndDestroyBazInstance();
        // assertions checking dependency injection are made in interceptors
        assertTrue(AroundInvokeInterceptor.called);
        assertTrue(PostConstructInterceptor.called);
        assertTrue(PreDestroyInterceptor.called);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = INTERCEPTOR_LIFECYCLE, id = "ba")
    public void testInterceptorInstanceCreatedWhenTargetInstanceCreated(Instance<Warrior> instance) {
        for (int i = 1; i < 3; i++) {
            createWarriorInstanceAndAssertInterceptorsCount(instance, i);
        }
    }

    private void createWarriorInstanceAndAssertInterceptorsCount(Instance<Warrior> instance, int repetition) {
        Warrior warrior = instance.get();
        assertEquals(WarriorPCInterceptor.count, repetition);
        assertEquals(WarriorPDInterceptor.count, repetition);
        assertEquals(WarriorAIInterceptor.count, repetition);
        assertEquals(MethodInterceptor.count, repetition);
        assertEquals(WarriorAttackAIInterceptor.count, repetition);
        // two weapons injected into warrior
        assertEquals(WeaponAIInterceptor.count, 2 * repetition);

        // when warrior attacks, he uses his weapon - at that time
        // an instance of WeaponAIInterceptor intercepting its usage is set to its field
        warrior.attack1();
        warrior.attack2();
        assertNotEquals(warrior.getWeapon1().getWI(), warrior.getWeapon2().getWI());
        assertEquals(WarriorAIInterceptor.count, repetition);
        assertEquals(MethodInterceptor.count, repetition);
        assertEquals(WarriorAttackAIInterceptor.count, repetition);
    }

    private void createCallAndDestroyBazInstance() {
        Class<Baz> clazz = Baz.class;
        Bean<Baz> bean = getUniqueBean(clazz);
        CreationalContext<Baz> ctx = getCurrentManager().createCreationalContext(bean);
        Baz instance = (Baz) getCurrentManager().getReference(bean, clazz, ctx);
        instance.doSomething();
        bean.destroy(instance, ctx);
    }
}
