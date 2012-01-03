/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.jsr299.tck.tests.implementation.builtin.metadata;

import static org.jboss.jsr299.tck.TestGroups.PASSIVATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.lang.reflect.Type;
import java.util.Collections;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.Decorator;
import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.inject.spi.Interceptor;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
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
public class BuiltinMetadataBeanTest extends AbstractJSR299Test {

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
    @SpecAssertions({ @SpecAssertion(section = "5.5.8", id = "a"), @SpecAssertion(section = "5.5.8", id = "f") })
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
        assertEquals(factory.getBeans().size(), 2);
        assertEquals(fruitYoghurtBean, factory.getBeans().get(0));
        assertEquals(probioticYoghurtBean, factory.getBeans().get(1));
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

    @Test(groups = { PASSIVATION })
    @SpecAssertions({ @SpecAssertion(section = "5.5.8", id = "g"), @SpecAssertion(section = "5.5.8", id = "f") })
    public void testIllegalInjectionDetected() {
        assertNull(getReference(Bean.class));
        assertNull(getReference(Interceptor.class));
        assertNull(getReference(Decorator.class));
    }

    private Object getReference(Class<?> type) {
        Bean<?> bean = getCurrentManager().resolve(getCurrentManager().getBeans(type));
        CreationalContext<?> ctx = getCurrentManager().createCreationalContext(bean);
        return getCurrentManager().getReference(bean, type, ctx);
    }
}
