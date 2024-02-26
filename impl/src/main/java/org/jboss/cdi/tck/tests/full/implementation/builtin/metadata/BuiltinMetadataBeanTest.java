/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.implementation.builtin.metadata;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_METADATA;
import static org.testng.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.Collections;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.Decorator;
import jakarta.enterprise.inject.spi.InterceptionType;
import jakarta.enterprise.inject.spi.Interceptor;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * </p>
 * <p>
 * Note that we also test that all built-in beans are passivation capable dependencies - if validation of passivation capable
 * beans and dependencies fails test the deployment (and thus all test methods) will fail.
 * </p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class BuiltinMetadataBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(BuiltinMetadataBeanTest.class)
                .withBeansXml(new BeansXml().interceptors(YoghurtInterceptor.class).decorators(MilkProductDecorator.class))
                .build();
    }

    @Inject
    private Yoghurt yoghurt;

    @Inject
    private YoghurtFactory factory;

    @Test
    @SpecAssertions({ @SpecAssertion(section = BEAN_METADATA, id = "a"), @SpecAssertion(section = BEAN_METADATA, id = "f") })
    public void testBeanMetadata() {
        Bean<?> resolvedBean = getUniqueBean(Yoghurt.class);
        assertEquals(resolvedBean, yoghurt.getBeanBean());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BEAN_METADATA, id = "a"),
            @SpecAssertion(section = BEAN_METADATA, id = "f") })
    public void testProducerMethodMetadata() {
        Bean<Yoghurt> fruitYoghurtBean = getUniqueBean(Yoghurt.class, new Fruit.Literal());
        CreationalContext<Yoghurt> fruitCtx = getCurrentManager().createCreationalContext(fruitYoghurtBean);
        Yoghurt fruitYoghurt = (Yoghurt) getCurrentManager().getReference(fruitYoghurtBean, Yoghurt.class, fruitCtx);
        assertEquals(fruitYoghurtBean, factory.getFruitYoghurtBean());

        Bean<Yoghurt> probioticYoghurtBean = getUniqueBean(Yoghurt.class, new Probiotic.Literal());
        CreationalContext<Yoghurt> probioticCtx = getCurrentManager().createCreationalContext(probioticYoghurtBean);
        Yoghurt probioticYoghurt = (Yoghurt) getCurrentManager().getReference(probioticYoghurtBean, Yoghurt.class,
                probioticCtx);
        assertEquals(probioticYoghurtBean, factory.getProbioticYoghurtBean());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BEAN_METADATA, id = "b"), @SpecAssertion(section = BEAN_METADATA, id = "d"),
            @SpecAssertion(section = BEAN_METADATA, id = "f") })
    public void testInterceptorMetadata() {
        Bean<?> bean = getUniqueBean(Yoghurt.class);
        Interceptor<?> interceptor = getCurrentManager()
                .resolveInterceptors(InterceptionType.AROUND_INVOKE, new Frozen.Literal()).iterator().next();
        YoghurtInterceptor yoghurtInterceptor = yoghurt.getInterceptorInstance();
        assertEquals(interceptor, yoghurtInterceptor.getBean());
        assertEquals(interceptor, yoghurtInterceptor.getInterceptor());
        assertEquals(bean, yoghurtInterceptor.getInterceptedBean());

    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BEAN_METADATA, id = "c"), @SpecAssertion(section = BEAN_METADATA, id = "e"),
            @SpecAssertion(section = BEAN_METADATA, id = "f") })
    public void testDecoratorMetadata() {
        Bean<?> bean = getUniqueBean(Yoghurt.class);
        Decorator<?> decorator = getCurrentManager().resolveDecorators(Collections.<Type> singleton(MilkProduct.class))
                .iterator().next();
        MilkProductDecorator instance = yoghurt.getDecoratorInstance();
        assertEquals(decorator, instance.getBean());
        assertEquals(decorator, instance.getDecorator());
        assertEquals(bean, instance.getDecoratedBean());
    }

}
