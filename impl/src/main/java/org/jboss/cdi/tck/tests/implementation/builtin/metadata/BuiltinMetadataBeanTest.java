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
package org.jboss.cdi.tck.tests.implementation.builtin.metadata;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.Collections;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.Decorator;
import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.inject.spi.Interceptor;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * Note that we also test that all built-in beans are passivation capable dependencies - if validation of passivation capable
 * beans and dependencies fails test the deployment (and thus all test methods) will fail.
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class BuiltinMetadataBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(BuiltinMetadataBeanTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createInterceptors()
                                .clazz(YoghurtInterceptor.class.getName()).up().createDecorators()
                                .clazz(MilkProductDecorator.class.getName()).up()).build();
    }

    @Inject
    private Yoghurt yoghurt;

    @Inject
    private YoghurtFactory factory;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "5.5.8", id = "a"), @SpecAssertion(section = "5.5.8", id = "f") })
    public void testBeanMetadata() {
        Bean<?> resolvedBean = getUniqueBean(Yoghurt.class);
        assertEquals(resolvedBean, yoghurt.getBeanBean());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "5.5.8", id = "a"), @SpecAssertion(section = "5.5.8", id = "n"),
            @SpecAssertion(section = "5.5.8", id = "f") })
    public void testProducerAndDisposerMethodMetadata() {
        Bean<Yoghurt> fruitYoghurtBean = getUniqueBean(Yoghurt.class, new Fruit.Literal());
        CreationalContext<Yoghurt> fruitCtx = getCurrentManager().createCreationalContext(fruitYoghurtBean);
        Yoghurt fruitYoghurt = (Yoghurt) getCurrentManager().getReference(fruitYoghurtBean, Yoghurt.class, fruitCtx);
        assertEquals(fruitYoghurtBean, factory.getFruitYoghurtBean());

        Bean<Yoghurt> probioticYoghurtBean = getUniqueBean(Yoghurt.class, new Probiotic.Literal());
        CreationalContext<Yoghurt> probioticCtx = getCurrentManager().createCreationalContext(probioticYoghurtBean);
        Yoghurt probioticYoghurt = (Yoghurt) getCurrentManager().getReference(probioticYoghurtBean, Yoghurt.class, fruitCtx);
        assertEquals(probioticYoghurtBean, factory.getProbioticYoghurtBean());

        // Now verify the disposer method
        fruitYoghurtBean.destroy(fruitYoghurt, fruitCtx);
        probioticYoghurtBean.destroy(probioticYoghurt, probioticCtx);
        assertEquals(factory.getDisposedBeans().size(), 2);
        assertEquals(fruitYoghurtBean, factory.getDisposedBeans().get(0));
        assertEquals(probioticYoghurtBean, factory.getDisposedBeans().get(1));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "5.5.8", id = "b"), @SpecAssertion(section = "5.5.8", id = "d"),
            @SpecAssertion(section = "5.5.8", id = "f") })
    public void testInterceptorMetadata() {
        Bean<?> bean = getUniqueBean(Yoghurt.class);
        Interceptor<?> interceptor = getCurrentManager()
                .resolveInterceptors(InterceptionType.AROUND_INVOKE, new Frozen.Literal()).iterator().next();
        YoghurtInterceptor instance = yoghurt.getInterceptorInstance();
        assertEquals(interceptor, instance.getBean());
        assertEquals(interceptor, instance.getInterceptor());
        assertEquals(bean, instance.getInterceptedBean());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "5.5.8", id = "c"), @SpecAssertion(section = "5.5.8", id = "e"),
            @SpecAssertion(section = "5.5.8", id = "f") })
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
