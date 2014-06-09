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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.DependentInstance;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import java.lang.annotation.Annotation;

import static org.jboss.cdi.tck.cdi.Sections.*;
import static org.testng.Assert.*;

@SpecVersion(spec = "cdi", version = "1.1 Final Release")
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
    @SpecAssertions({@SpecAssertion(section = METHOD_CONSTRUCTOR_PARAMETER_QUALIFIERS, id = "c"), @SpecAssertion(section = DISPOSER_METHOD, id = "b"),
            @SpecAssertion(section = DISPOSER_METHOD, id = "c"), @SpecAssertion(section = DISPOSER_METHOD, id = "e"),
            @SpecAssertion(section = DISPOSER_METHOD_DISPOSED_PARAMETER, id = "ba"), @SpecAssertion(section = DECLARING_DISPOSER_METHOD, id = "a"),
            @SpecAssertion(section = DECLARING_DISPOSER_METHOD, id = "b0"), @SpecAssertion(section = DISPOSER_METHOD_RESOLUTION, id = "aa"),
            @SpecAssertion(section = PRODUCER_OR_DISPOSER_METHODS_INVOCATION, id = "b")})
    public void testBindingTypesAppliedToDisposalMethodParameters() throws Exception {

        assertFalse(SpiderProducer.isTameSpiderDestroyed());
        assertFalse(SpiderProducer.isDeadliestTarantulaDestroyed());

        Bean<Tarantula> tarantula = getBeans(Tarantula.class, DEADLIEST_LITERAL).iterator().next();
        CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantula);
        Tarantula instance = tarantula.create(creationalContext);
        tarantula.destroy(instance, creationalContext);

        assertTrue(SpiderProducer.isTameSpiderDestroyed());
        assertTrue(SpiderProducer.isDeadliestTarantulaDestroyed());
    }

    @Test
    @SpecAssertions({@SpecAssertion(section = DISPOSER_METHOD, id = "aa"), @SpecAssertion(section = DISPOSER_METHOD_DISPOSED_PARAMETER, id = "ba")})
    public void testDisposalMethodOnNonBean() throws Exception {

        DependentInstance<WebSpider> webSpider = newDependentInstance(WebSpider.class, DEADLIEST_LITERAL);
        WebSpider instance = webSpider.get();
        assertNotNull(instance);
        webSpider.destroy();
        assertFalse(DisposalNonBean.isWebSpiderdestroyed());
    }

    /**
     * In addition to the disposed parameter, a disposal method may declare additional parameters, which may also specify
     * bindings. The container calls Manager.getInstanceToInject() to determine a value for each parameter of a disposal method
     * and calls the disposal method with those parameter values
     *
     * @throws Exception
     */
    @Test
    @SpecAssertions({@SpecAssertion(section = DECLARING_DISPOSER_METHOD, id = "h"), @SpecAssertion(section = INJECTION_POINT_DEFAULT_QUALIFIER, id = "a"),
            @SpecAssertion(section = PRODUCER_OR_DISPOSER_METHODS_INVOCATION, id = "e")})
    public void testDisposalMethodParametersGetInjected() throws Exception {

        DependentInstance<SandSpider> sandSpider = newDependentInstance(SandSpider.class, DEADLIEST_LITERAL);
        SandSpider sandSpiderInst = sandSpider.get();
        assertNotNull(sandSpiderInst);
        sandSpider.destroy();
        assertTrue(SpiderProducer.isDeadliestSandSpiderDestroyed());
    }

    @Test
    @SpecAssertion(section = DISPOSER_METHOD_DISPOSED_PARAMETER, id = "da")
    public void testDisposalMethodForMultipleProducerMethods() throws Exception {

        DependentInstance<Widow> deadliest = newDependentInstance(Widow.class, DEADLIEST_LITERAL);
        Widow deadliestInstance = deadliest.get();
        assertNotNull(deadliestInstance);
        deadliest.destroy();

        DependentInstance<Widow> tame = newDependentInstance(Widow.class, TAME_LITERAL);
        Widow tameInstance = tame.get();
        assertNotNull(tameInstance);
        tame.destroy();

        assertEquals(SpiderProducer.getWidowsDestroyed(), 2);
    }

    /**
     * Tests that a disposal method can be bound to a product of a producer field. CDI-145
     */
    @Test
    @SpecAssertions({@SpecAssertion(section = DISPOSER_METHOD_RESOLUTION, id = "ab"), @SpecAssertion(section = PRODUCER_FIELD_LIFECYCLE, id = "o")})
    public void testDisposalMethodCalledForProducerField() throws Exception {

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
