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
package org.jboss.cdi.tck.tests.implementation.producer.method.lifecycle;

import static org.jboss.cdi.tck.cdi.Sections.CONTEXTUAL;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_METHOD;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_METHOD_LIFECYCLE;

import java.util.Set;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.CreationException;
import jakarta.enterprise.inject.IllegalProductException;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * NOTE May be able to get rid of some of the binding types if the producer method precedence question is resolved
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ProducerMethodLifecycleTest extends AbstractTest {
    private AnnotationLiteral<Pet> PET_LITERAL = new Pet.Literal();
    private AnnotationLiteral<FirstBorn> FIRST_BORN_LITERAL = new FirstBorn.Literal();
    private AnnotationLiteral<Fail> FAIL_LITERAL = new Fail.Literal();
    private AnnotationLiteral<Null> NULL_LITERAL = new Null.Literal();

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProducerMethodLifecycleTest.class)
                .build();
    }

    @Test
    @SpecAssertion(section = PRODUCER_METHOD_LIFECYCLE, id = "ea")
    public void testProducerMethodBeanCreate() {
        SpiderProducer.reset();
        Bean<Tarantula> tarantulaBean = getBeans(Tarantula.class, PET_LITERAL).iterator().next();
        CreationalContext<Tarantula> tarantulaCc = getCurrentManager().createCreationalContext(tarantulaBean);
        Tarantula tarantula = tarantulaBean.create(tarantulaCc);
        assert SpiderProducer.getTarantulaCreated() == tarantula;
        assert SpiderProducer.getInjectedWeb() != null;
        assert SpiderProducer.getInjectedWeb().isDestroyed();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD_LIFECYCLE, id = "ea") })
    public void testProducerMethodInvokedOnCreate() {
        Bean<SpiderEgg> eggBean = getBeans(SpiderEgg.class, FIRST_BORN_LITERAL).iterator().next();
        CreationalContext<SpiderEgg> eggCc = getCurrentManager().createCreationalContext(eggBean);
        assert eggBean.create(eggCc) != null;
    }

    @Test
    @SpecAssertion(section = PRODUCER_METHOD, id = "j")
    public void testWhenApplicationInvokesProducerMethodParametersAreNotInjected() {
        try {
            getContextualReference(BrownRecluse.class).layAnEgg(null);
        } catch (AssertionError e) {
            return;
        }

        assert false : "The BeanManager should not have been injected into the producer method";
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD_LIFECYCLE, id = "k") })
    public void testCreateReturnsNullIfProducerDoesAndDependent() {
        Bean<Spider> nullSpiderBean = getBeans(Spider.class, NULL_LITERAL).iterator().next();
        CreationalContext<Spider> nullSpiderBeanCc = getCurrentManager().createCreationalContext(nullSpiderBean);
        assert nullSpiderBean.create(nullSpiderBeanCc) == null;
    }

    @Test(expectedExceptions = IllegalProductException.class)
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD_LIFECYCLE, id = "l") })
    public void testCreateFailsIfProducerReturnsNullAndNotDependent() {
        Bean<PotatoChip> potatoChipBean = getBeans(PotatoChip.class, NULL_LITERAL).iterator().next();
        assert potatoChipBean != null;

        CreationalContext<PotatoChip> chipCc = getCurrentManager().createCreationalContext(potatoChipBean);
        potatoChipBean.create(chipCc);
        assert false;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD_LIFECYCLE, id = "ma"),
            @SpecAssertion(section = PRODUCER_METHOD_LIFECYCLE, id = "r") })
    public void testProducerMethodBeanDestroy() {
        SpiderProducer.reset();
        Set<Bean<?>> beans = getCurrentManager().getBeans(Tarantula.class, PET_LITERAL);
        Bean<?> bean = getCurrentManager().resolve(beans);
        assert bean.getBeanClass().equals(SpiderProducer.class);
        assert bean.getTypes().contains(Tarantula.class);
        Bean<Tarantula> tarantulaBean = (Bean<Tarantula>) bean;
        CreationalContext<Tarantula> tarantulaCc = getCurrentManager().createCreationalContext(tarantulaBean);
        Tarantula tarantula = tarantulaBean.create(tarantulaCc);
        SpiderProducer.resetInjections();
        tarantulaBean.destroy(tarantula, tarantulaCc);
        assert SpiderProducer.getTarantulaDestroyed() == tarantula;
        assert SpiderProducer.isDestroyArgumentsSet();
        assert SpiderProducer.getInjectedWeb() != null;
        assert SpiderProducer.getInjectedWeb().isDestroyed();
    }

    @Test(expectedExceptions = FooException.class)
    @SpecAssertions({ @SpecAssertion(section = CONTEXTUAL, id = "aa") })
    public void testCreateRethrowsUncheckedException() {
        Bean<Ship> shipBean = getBeans(Ship.class, FAIL_LITERAL).iterator().next();
        CreationalContext<Ship> shipCc = getCurrentManager().createCreationalContext(shipBean);
        shipBean.create(shipCc);
        assert false;
    }

    @Test(expectedExceptions = CreationException.class)
    @SpecAssertions({ @SpecAssertion(section = CONTEXTUAL, id = "aa") })
    public void testCreateWrapsCheckedExceptionAndRethrows() {
        Bean<Lorry> lorryBean = getBeans(Lorry.class, FAIL_LITERAL).iterator().next();
        CreationalContext<Lorry> lorryCc = getCurrentManager().createCreationalContext(lorryBean);
        lorryBean.create(lorryCc);
        assert false;
    }
}
