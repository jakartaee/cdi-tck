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
package org.jboss.jsr299.tck.tests.implementation.producer.method.lifecycle;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.CreationException;
import javax.enterprise.inject.IllegalProductException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * NOTE May be able to get rid of some of the binding types if the producer method precedence question is resolved
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class ProducerMethodLifecycleTest extends AbstractJSR299Test {
    private AnnotationLiteral<Pet> PET_LITERAL = new AnnotationLiteral<Pet>() {
    };
    private AnnotationLiteral<FirstBorn> FIRST_BORN_LITERAL = new AnnotationLiteral<FirstBorn>() {
    };
    private AnnotationLiteral<Fail> FAIL_LITERAL = new AnnotationLiteral<Fail>() {
    };
    private AnnotationLiteral<Null> NULL_LITERAL = new AnnotationLiteral<Null>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProducerMethodLifecycleTest.class).withBeansXml("beans.xml")
                .build();
    }

    @Test(groups = { "producerMethod" })
    @SpecAssertion(section = "7.3.4", id = "ea")
    public void testProducerMethodBeanCreate() {
        PreferredSpiderProducer.reset();
        Bean<Tarantula> tarantulaBean = getBeans(Tarantula.class, PET_LITERAL).iterator().next();
        CreationalContext<Tarantula> tarantulaCc = getCurrentManager().createCreationalContext(tarantulaBean);
        Tarantula tarantula = tarantulaBean.create(tarantulaCc);
        assert PreferredSpiderProducer.getTarantulaCreated() == tarantula;
        assert PreferredSpiderProducer.getInjectedWeb() != null;
        assert PreferredSpiderProducer.getInjectedWeb().isDestroyed();
    }

    @Test(groups = { "producerMethod" })
    @SpecAssertions({ @SpecAssertion(section = "7.3.4", id = "ea") })
    public void testProducerMethodInvokedOnCreate() {
        Bean<SpiderEgg> eggBean = getBeans(SpiderEgg.class, FIRST_BORN_LITERAL).iterator().next();
        CreationalContext<SpiderEgg> eggCc = getCurrentManager().createCreationalContext(eggBean);
        assert eggBean.create(eggCc) != null;
    }

    @Test(groups = { "producerMethod" })
    @SpecAssertion(section = "3.3", id = "j")
    public void testWhenApplicationInvokesProducerMethodParametersAreNotInjected() {
        try {
            getInstanceByType(BrownRecluse.class).layAnEgg(null);
        } catch (AssertionError e) {
            return;
        }

        assert false : "The BeanManager should not have been injected into the producer method";
    }

    @Test(groups = { "producerMethod" })
    @SpecAssertions({ @SpecAssertion(section = "5.5.4", id = "c"), @SpecAssertion(section = "4.3", id = "cb") })
    public void testProducerMethodFromSpecializedBeanUsed() {
        SpiderProducer.reset();
        PreferredSpiderProducer.reset();
        Bean<Tarantula> spiderBean = getBeans(Tarantula.class, PET_LITERAL).iterator().next();
        CreationalContext<Tarantula> spiderBeanCc = getCurrentManager().createCreationalContext(spiderBean);
        Tarantula tarantula = spiderBean.create(spiderBeanCc);
        assert PreferredSpiderProducer.getTarantulaCreated() == tarantula;
        assert !SpiderProducer.isTarantulaCreated();
    }

    @Test(groups = { "producerMethod" })
    @SpecAssertions({ @SpecAssertion(section = "7.3.4", id = "k") })
    public void testCreateReturnsNullIfProducerDoesAndDependent() {
        Bean<Spider> nullSpiderBean = getBeans(Spider.class, NULL_LITERAL).iterator().next();
        CreationalContext<Spider> nullSpiderBeanCc = getCurrentManager().createCreationalContext(nullSpiderBean);
        assert nullSpiderBean.create(nullSpiderBeanCc) == null;
    }

    @Test(groups = { "producerMethod" }, expectedExceptions = IllegalProductException.class)
    @SpecAssertions({ @SpecAssertion(section = "7.3.4", id = "l") })
    public void testCreateFailsIfProducerReturnsNullAndNotDependent() {
        Bean<PotatoChip> potatoChipBean = getBeans(PotatoChip.class, NULL_LITERAL).iterator().next();
        assert potatoChipBean != null;

        CreationalContext<PotatoChip> chipCc = getCurrentManager().createCreationalContext(potatoChipBean);
        potatoChipBean.create(chipCc);
        assert false;
    }

    @Test(groups = { "producerMethod", "disposalMethod" })
    @SpecAssertions({ @SpecAssertion(section = "7.3.4", id = "ma"), @SpecAssertion(section = "7.3.4", id = "r") })
    public void testProducerMethodBeanDestroy() {
        PreferredSpiderProducer.reset();
        Set<Bean<?>> beans = getCurrentManager().getBeans(Tarantula.class, PET_LITERAL);
        Bean<?> bean = getCurrentManager().resolve(beans);
        assert bean.getBeanClass().equals(PreferredSpiderProducer.class);
        assert bean.getTypes().contains(Tarantula.class);
        Bean<Tarantula> tarantulaBean = (Bean<Tarantula>) bean;
        CreationalContext<Tarantula> tarantulaCc = getCurrentManager().createCreationalContext(tarantulaBean);
        Tarantula tarantula = tarantulaBean.create(tarantulaCc);
        PreferredSpiderProducer.resetInjections();
        tarantulaBean.destroy(tarantula, tarantulaCc);
        assert PreferredSpiderProducer.getTarantulaDestroyed() == tarantula;
        assert PreferredSpiderProducer.isDestroyArgumentsSet();
        assert PreferredSpiderProducer.getInjectedWeb() != null;
        assert PreferredSpiderProducer.getInjectedWeb().isDestroyed();
    }

    @Test(groups = { "producerMethod" }, expectedExceptions = FooException.class)
    @SpecAssertions({ @SpecAssertion(section = "6.1", id = "a0") })
    public void testCreateRethrowsUncheckedException() {
        Bean<Ship> shipBean = getBeans(Ship.class, FAIL_LITERAL).iterator().next();
        CreationalContext<Ship> shipCc = getCurrentManager().createCreationalContext(shipBean);
        shipBean.create(shipCc);
        assert false;
    }

    @Test(groups = { "producerMethod" }, expectedExceptions = CreationException.class)
    @SpecAssertions({ @SpecAssertion(section = "6.1", id = "a0") })
    public void testCreateWrapsCheckedExceptionAndRethrows() {
        Bean<Lorry> lorryBean = getBeans(Lorry.class, FAIL_LITERAL).iterator().next();
        CreationalContext<Lorry> lorryCc = getCurrentManager().createCreationalContext(lorryBean);
        lorryBean.create(lorryCc);
        assert false;
    }
}
