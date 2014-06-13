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
package org.jboss.cdi.tck.tests.implementation.simple.newSimpleBean;

import static org.jboss.cdi.tck.cdi.Sections.NEW;
import static org.testng.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.NewLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.DependentInstance;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class NewSimpleBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(NewSimpleBeanTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createAlternatives().clazz(Tiger.class.getName()).up()
                                .createInterceptors().clazz(OrderInterceptor.class.getName()).up()).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = NEW, id = "ya") })
    public void testNewBeanCreatedForFieldInjectionPoint() {
        assert getContextualReference(Griffin.class).getList() instanceof ArrayList<?>;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = NEW, id = "yc") })
    public void testNewBeanCreatedForInitializerInjectionPoint() {
        assert getContextualReference(Dragon.class).getChildren() instanceof HashSet<?>;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = NEW, id = "ye") })
    public void testNewBeanCreatedForConstructorInjectioAnPoint() {
        assert getContextualReference(Hippogriff.class).getHomes() instanceof HashMap<?, ?>;
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertions({ @SpecAssertion(section = NEW, id = "yg") })
    public void testNewBeanCreatedForProducerMethod() {
        assert getContextualReference(new TypeLiteral<Collection<Dragon>>() {
        }) instanceof ArrayList<?>;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = NEW, id = "yi") })
    public void testNewBeanCreatedForObserverMethod() {
        getCurrentManager().fireEvent(new Griffin());
        assert getContextualReference(Bestiary.class).getPossibleNames() instanceof TreeSet<?>;
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertions({ @SpecAssertion(section = NEW, id = "yk") })
    public void testNewBeanCreatedForDisposerMethod() {
        Bean<Collection<Dragon>> bean = getUniqueBean(new TypeLiteral<Collection<Dragon>>() {
        });
        CreationalContext<Collection<Dragon>> ctx = getCurrentManager().createCreationalContext(bean);
        Collection<Dragon> dragons = bean.create(ctx);
        bean.destroy(dragons, ctx);
        assert getContextualReference(Bestiary.class).getKnightsWhichKilledTheDragons() instanceof LinkedHashSet<?>;
    }

    @Test
    @SpecAssertions({})
    public void testNewBeanIsDependentScoped() {
        FoxRun foxRun = getContextualReference(FoxRun.class);
        foxRun.getNewFox().setDen(new Den("TheLarches"));
        assert !foxRun.getNewFox().getDen().getName().equals(foxRun.getNewFox2().getDen().getName());
    }

    @Test
    @SpecAssertion(section = NEW, id = "t")
    public void testNewBeanHasNoStereotypes() {
        Bean<Fox> foxBean = getBeans(Fox.class).iterator().next();
        assert foxBean.getScope().equals(RequestScoped.class);
        assert foxBean.getName().equals("fox");
        Fox newFox1 = getContextualReference(FoxRun.class).getNewFox();
        Fox newFox2 = getContextualReference(FoxRun.class).getNewFox();
        newFox1.setDen(new Den("TheElms"));
        assert newFox2.getDen().getName() != "TheElms";
    }

    @Test
    @SpecAssertion(section = NEW, id = "u")
    public void testNewBeanHasNoObservers() {
        // As long as only one observer exists here, we know it is not from the @New bean
        assert getCurrentManager().resolveObserverMethods("event").size() == 1;
    }

    @Test
    @SpecAssertion(section = NEW, id = "w")
    public void testNewBeanHasNoProducerFields() throws Exception {
        FoxRun foxRun = getContextualReference(FoxRun.class);
        foxRun.getNewFox().setDen(new Den("NewFoxDen"));
        Den theOnlyDen = getContextualReference(Den.class);
        assert theOnlyDen.getName().equals(foxRun.getFox().getDen().getName());
    }

    @Test
    @SpecAssertion(section = NEW, id = "v")
    public void testNewBeanHasNoProducerMethods() throws Exception {
        FoxRun foxRun = getContextualReference(FoxRun.class);
        foxRun.getFox().setNextLitterSize(3);
        foxRun.getNewFox().setNextLitterSize(5);
        Litter theOnlyLitter = getContextualReference(Litter.class);
        assert theOnlyLitter.getQuantity() == foxRun.getFox().getNextLitterSize();
    }

    @Test
    @SpecAssertion(section = NEW, id = "x")
    public void testNewBeanHasNoDisposerMethods() throws Exception {
        FoxRun foxRun = getContextualReference(FoxRun.class);
        DependentInstance<Litter> litterBean = newDependentInstance(Litter.class);
        Litter litter = litterBean.get();
        assertFalse(litter.equals(null));
        litterBean.destroy();
        assertTrue(foxRun.getFox().isLitterDisposed());
        assertFalse(foxRun.getNewFox().isLitterDisposed());
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertion(section = NEW, id = "d")
    public void testForEachSimpleBeanANewBeanExists() {

        Bean<Order> orderBean = getUniqueBean(Order.class);

        assertEquals(orderBean.getQualifiers().size(), 2);
        assertTrue(annotationSetMatches(orderBean.getQualifiers(), Any.class, Default.class));

        assertNotNull(getContextualReference(Shop.class).getNewOrder());

        Bean<Order> newOrderBean = getUniqueBean(Order.class, NewLiteral.INSTANCE);
        assertEquals(newOrderBean.getBeanClass(), Order.class);

        Bean<Lion> lionBean = getUniqueBean(Lion.class, new TameLiteral());
        assertEquals(lionBean.getQualifiers().size(), 2);
        assertTrue(annotationSetMatches(lionBean.getQualifiers(), Any.class, Tame.class));

        assertNotNull(getContextualReference(LionCage.class).getNewLion());
    }

    @Test
    @SpecAssertion(section = NEW, id = "e")
    public void testNewBeanHasTheSameBeanTypes() {
        Bean<Order> bean = getUniqueBean(Order.class, NewLiteral.INSTANCE);
        assertTrue(typeSetMatches(bean.getTypes(), Object.class, Serializable.class, Order.class));
    }

    @Test
    @SpecAssertion(section = NEW, id = "i")
    public void testNewBeanHasTheSameInterceptorBindings() {
        // Method foo() is intercepted
        assertTrue(getContextualReference(Shop.class).getNewOrder().foo());
    }

    @Test
    @SpecAssertion(section = NEW, id = "f")
    public void testNewBeanHasSameConstructor() {
        ExplicitContructorSimpleBean.setConstructorCalls(0);
        Consumer consumer = getContextualReference(Consumer.class);
        // Make sure all deps are initialized, even if deps are lazily init'd
        consumer.getExplicitConstructorBean().ping();
        consumer.getNewExplicitConstructorBean().ping();
        int calls = ExplicitContructorSimpleBean.getConstructorCalls();
        assert calls == 2;
    }

    @Test
    @SpecAssertion(section = NEW, id = "g")
    public void testNewBeanHasSameInitializers() {
        InitializerSimpleBean.setInitializerCalls(0);
        Consumer consumer = getContextualReference(Consumer.class);
        consumer.getInitializerSimpleBean().businessMethod(); // Cause proxy to initialize the bean
        consumer.getNewInitializerSimpleBean().businessMethod();
        int calls = InitializerSimpleBean.getInitializerCalls();
        assert calls == 2;
    }

    @Test
    @SpecAssertion(section = NEW, id = "h")
    public void testNewBeanHasSameInjectedFields() {
        Consumer consumer = getContextualReference(Consumer.class);
        assert consumer.getNewInitializerSimpleBean().getOrder() != null;
    }

    @Test
    @SpecAssertion(section = NEW, id = "xb")
    public void testNewBeanIsNotAlternative() {
        assert getUniqueBean(Tiger.class).isAlternative();
        assert !getUniqueBean(Tiger.class, NewLiteral.INSTANCE).isAlternative();
    }

    @Test
    @SpecAssertion(section = NEW, id = "z")
    public void testNewBeanWithNoMemberValue() {
        assert getContextualReference(NewLionConsumer.class).getLion() instanceof Lion;
    }
}
