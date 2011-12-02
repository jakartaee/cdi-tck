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
package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import static org.jboss.jsr299.tck.TestGroups.NEW;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.jsr299.tck.literals.DefaultLiteral;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class NewSimpleBeanTest extends AbstractJSR299Test {

    private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(NewSimpleBeanTest.class).withBeansXml("beans.xml").build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.14", id = "ya") })
    public void testNewBeanCreatedForFieldInjectionPoint() {
        assert getInstanceByType(Griffin.class).getList() instanceof ArrayList<?>;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.14", id = "yc") })
    public void testNewBeanCreatedForInitializerInjectionPoint() {
        assert getInstanceByType(Dragon.class).getChildren() instanceof HashSet<?>;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.14", id = "ye") })
    public void testNewBeanCreatedForConstructorInjectioAnPoint() {
        assert getInstanceByType(Hippogriff.class).getHomes() instanceof HashMap<?, ?>;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.14", id = "yg") })
    public void testNewBeanCreatedForProducerMethod() {
        assert getInstanceByType(new TypeLiteral<Collection<Dragon>>() {
        }) instanceof ArrayList<?>;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.14", id = "yi") })
    public void testNewBeanCreatedForObserverMethod() {
        getCurrentManager().fireEvent(new Griffin());
        assert getInstanceByType(Bestiary.class).getPossibleNames() instanceof TreeSet<?>;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.14", id = "yk") })
    public void testNewBeanCreatedForDisposerMethod() {
        Bean<Collection<Dragon>> bean = getUniqueBean(new TypeLiteral<Collection<Dragon>>() {
        });
        CreationalContext<Collection<Dragon>> ctx = getCurrentManager().createCreationalContext(bean);
        Collection<Dragon> dragons = bean.create(ctx);
        bean.destroy(dragons, ctx);
        assert getInstanceByType(Bestiary.class).getKnightsWhichKilledTheDragons() instanceof LinkedHashSet<?>;
    }

    @Test(groups = { NEW })
    @SpecAssertions({
    // FIXME empty assertion!
    })
    public void testNewBeanIsDependentScoped() {
        FoxRun foxRun = getInstanceByType(FoxRun.class);
        foxRun.getNewFox().setDen(new Den("TheLarches"));
        assert !foxRun.getNewFox().getDen().getName().equals(foxRun.getNewFox2().getDen().getName());
    }

    @Test(groups = { NEW })
    @SpecAssertion(section = "3.14", id = "t")
    public void testNewBeanHasNoStereotypes() {
        Bean<Fox> foxBean = getBeans(Fox.class).iterator().next();
        assert foxBean.getScope().equals(RequestScoped.class);
        assert foxBean.getName().equals("fox");
        Fox newFox1 = getInstanceByType(FoxRun.class).getNewFox();
        Fox newFox2 = getInstanceByType(FoxRun.class).getNewFox();
        newFox1.setDen(new Den("TheElms"));
        assert newFox2.getDen().getName() != "TheElms";
    }

    @Test(groups = { NEW })
    @SpecAssertion(section = "3.14", id = "u")
    public void testNewBeanHasNoObservers() {
        // As long as only one observer exists here, we know it is not from the @New bean
        assert getCurrentManager().resolveObserverMethods("event").size() == 1;
    }

    @Test(groups = { NEW })
    @SpecAssertion(section = "3.14", id = "w")
    public void testNewBeanHasNoProducerFields() throws Exception {
        FoxRun foxRun = getInstanceByType(FoxRun.class);
        foxRun.getNewFox().setDen(new Den("NewFoxDen"));
        Den theOnlyDen = getInstanceByType(Den.class);
        assert theOnlyDen.getName().equals(foxRun.getFox().getDen().getName());
    }

    @Test(groups = { NEW })
    @SpecAssertion(section = "3.14", id = "v")
    public void testNewBeanHasNoProducerMethods() throws Exception {
        FoxRun foxRun = getInstanceByType(FoxRun.class);
        foxRun.getFox().setNextLitterSize(3);
        foxRun.getNewFox().setNextLitterSize(5);
        Litter theOnlyLitter = getInstanceByType(Litter.class);
        assert theOnlyLitter.getQuantity() == foxRun.getFox().getNextLitterSize();
    }

    @Test(groups = { NEW })
    @SpecAssertion(section = "3.14", id = "x")
    public void testNewBeanHasNoDisposerMethods() throws Exception {
        FoxRun foxRun = getInstanceByType(FoxRun.class);
        Bean<Litter> litterBean = getBeans(Litter.class).iterator().next();
        CreationalContext<Litter> creationalContext = getCurrentManager().createCreationalContext(litterBean);
        Litter litter = getInstanceByType(Litter.class);
        litterBean.destroy(litter, creationalContext);
        assert foxRun.getFox().isLitterDisposed();
        assert !foxRun.getNewFox().isLitterDisposed();
    }

    @Test
    @SpecAssertion(section = "3.14", id = "d")
    public void testForEachSimpleBeanANewBeanExists() {
        assert getBeans(Order.class).size() == 1;
        assert getUniqueBean(Order.class).getQualifiers().size() == 2;
        assert getUniqueBean(Order.class).getQualifiers().contains(new DefaultLiteral());
        assert getInstanceByType(Shop.class).getNewOrder() != null;

        assert getCurrentManager().getBeans(Lion.class, TAME_LITERAL).size() == 1;
        assert getCurrentManager().getBeans(Lion.class, TAME_LITERAL).iterator().next().getQualifiers().size() == 2;
        assert getCurrentManager().getBeans(Lion.class, TAME_LITERAL).iterator().next().getQualifiers().contains(TAME_LITERAL);
        assert getCurrentManager().getBeans(Lion.class, TAME_LITERAL).iterator().next().getQualifiers()
                .contains(AnyLiteral.INSTANCE);

        assert getInstanceByType(LionCage.class).getNewLion() != null;
    }

    @Test(groups = { NEW })
    @SpecAssertion(section = "3.14", id = "f")
    public void testNewBeanHasSameConstructor() {
        ExplicitContructorSimpleBean.setConstructorCalls(0);
        Consumer consumer = getInstanceByType(Consumer.class);
        // Make sure all deps are initialized, even if deps are lazily init'd
        consumer.getExplicitConstructorBean().ping();
        consumer.getNewExplicitConstructorBean().ping();
        int calls = ExplicitContructorSimpleBean.getConstructorCalls();
        assert calls == 2;
    }

    @Test(groups = { NEW })
    @SpecAssertion(section = "3.14", id = "g")
    public void testNewBeanHasSameInitializers() {
        InitializerSimpleBean.setInitializerCalls(0);
        Consumer consumer = getInstanceByType(Consumer.class);
        consumer.getInitializerSimpleBean().businessMethod(); // Cause proxy to initialize the bean
        consumer.getNewInitializerSimpleBean().businessMethod();
        int calls = InitializerSimpleBean.getInitializerCalls();
        assert calls == 2;
    }

    @Test(groups = { NEW })
    @SpecAssertion(section = "3.14", id = "h")
    public void testNewBeanHasSameInjectedFields() {
        Consumer consumer = getInstanceByType(Consumer.class);
        assert consumer.getNewInitializerSimpleBean().getOrder() != null;
    }

    @Test(groups = { NEW })
    @SpecAssertion(section = "3.14", id = "xb")
    public void testNewBeanIsNotAlternative() {
        assert getUniqueBean(Tiger.class).isAlternative();
        assert !getUniqueBean(Tiger.class, Tiger.NEW).isAlternative();
    }

    @Test
    @SpecAssertion(section = "3.14", id = "z")
    public void testNewBeanWithNoMemberValue() {
        assert getInstanceByType(NewLionConsumer.class).getLion() instanceof Lion;
    }
}
