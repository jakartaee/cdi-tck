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
package org.jboss.cdi.tck.tests.implementation.disposal.method.definition;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class DisposalMethodDefinitionTest extends AbstractTest {

    @SuppressWarnings("serial")
    private static final Annotation DEADLIEST_LITERAL = new AnnotationLiteral<Deadliest>() {
    };

    @SuppressWarnings("serial")
    private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DisposalMethodDefinitionTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.3.5", id = "c"), @SpecAssertion(section = "3.5", id = "b"),
            @SpecAssertion(section = "3.5", id = "c"), @SpecAssertion(section = "3.5", id = "e"),
            @SpecAssertion(section = "3.5.1", id = "ba"), @SpecAssertion(section = "3.5.2", id = "a"),
            @SpecAssertion(section = "3.5.2", id = "b0"), @SpecAssertion(section = "3.5.3", id = "aa"),
            @SpecAssertion(section = "5.5.4", id = "b") })
    public void testBindingTypesAppliedToDisposalMethodParameters() throws Exception {

        SpiderProducer.reset();

        assertFalse(SpiderProducer.isTameSpiderDestroyed());
        assertFalse(SpiderProducer.isDeadliestSpiderDestroyed());

        Bean<Tarantula> tarantula = getBeans(Tarantula.class, DEADLIEST_LITERAL).iterator().next();
        CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantula);
        Tarantula instance = tarantula.create(creationalContext);
        tarantula.destroy(instance, creationalContext);

        assertTrue(SpiderProducer.isTameSpiderDestroyed());
        assertTrue(SpiderProducer.isDeadliestSpiderDestroyed());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.5", id = "aa"), @SpecAssertion(section = "3.5.1", id = "ba") })
    public void testDisposalMethodOnNonBean() throws Exception {

        Bean<Tarantula> tarantula = getBeans(Tarantula.class, DEADLIEST_LITERAL).iterator().next();
        CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantula);
        Tarantula instance = getCurrentManager().getContext(tarantula.getScope()).get(tarantula);
        tarantula.destroy(instance, creationalContext);

        assertFalse(DisposalNonBean.isSpiderDestroyed());
    }

    /**
     * In addition to the disposed parameter, a disposal method may declare additional parameters, which may also specify
     * bindings. The container calls Manager.getInstanceToInject() to determine a value for each parameter of a disposal method
     * and calls the disposal method with those parameter values
     * 
     * @throws Exception
     */
    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.5.2", id = "h"), @SpecAssertion(section = "3.11", id = "a"),
            @SpecAssertion(section = "5.5.4", id = "e") })
    public void testDisposalMethodParametersGetInjected() throws Exception {

        SpiderProducer.reset();

        Bean<Tarantula> tarantula = getBeans(Tarantula.class, DEADLIEST_LITERAL).iterator().next();
        CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantula);
        Tarantula instance = getCurrentManager().getContext(tarantula.getScope()).get(tarantula);
        tarantula.destroy(instance, creationalContext);

        assertTrue(SpiderProducer.isDeadliestSpiderDestroyed());
    }

    @Test
    @SpecAssertion(section = "3.5.1", id = "da")
    public void testDisposalMethodForMultipleProducerMethods() throws Exception {

        SpiderProducer.reset();

        Bean<Widow> deadliest = getBeans(Widow.class, DEADLIEST_LITERAL).iterator().next();
        CreationalContext<Widow> deadliestCreationalContext = getCurrentManager().createCreationalContext(deadliest);
        Widow deadliestInstance = getCurrentManager().getContext(deadliest.getScope()).get(deadliest);
        deadliest.destroy(deadliestInstance, deadliestCreationalContext);

        Bean<Widow> tame = getBeans(Widow.class, TAME_LITERAL).iterator().next();
        CreationalContext<Widow> tameCreationalContext = getCurrentManager().createCreationalContext(tame);
        Widow tameInstance = getCurrentManager().getContext(deadliest.getScope()).get(tame);
        tame.destroy(tameInstance, tameCreationalContext);

        assertEquals(SpiderProducer.getWidowsDestroyed(), 2);
    }

    /**
     * Tests that a disposal method can be bound to a product of a producer field. CDI-145
     */
    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.5.3", id = "ab"), @SpecAssertion(section = "7.3.5", id = "o") })
    public void testDisposalMethodCalledForProducerField() throws Exception {

        SpiderProducer.reset();
        createAndDestroyBean(Calisoga.class, new Scary.Literal());
        assertTrue(SpiderProducer.isScaryBlackWidowDestroyed());
        assertFalse(SpiderProducer.isTameBlackWidowDestroyed());

        SpiderProducer.reset();
        createAndDestroyBean(Calisoga.class, TAME_LITERAL);
        assertFalse(SpiderProducer.isScaryBlackWidowDestroyed());
        assertTrue(SpiderProducer.isTameBlackWidowDestroyed());
    }

    private <T> void createAndDestroyBean(Class<T> type, Annotation... qualifiers) {
        Bean<T> bean = getBeans(type, qualifiers).iterator().next();
        CreationalContext<T> creationalContext = getCurrentManager().createCreationalContext(bean);
        T instance = bean.create(creationalContext);
        bean.destroy(instance, creationalContext);
    }

}
