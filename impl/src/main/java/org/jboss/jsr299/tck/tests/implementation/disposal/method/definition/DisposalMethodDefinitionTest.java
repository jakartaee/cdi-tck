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
package org.jboss.jsr299.tck.tests.implementation.disposal.method.definition;

import java.lang.annotation.Annotation;

import javax.enterprise.context.spi.CreationalContext;
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

@SpecVersion(spec = "cdi", version = "20091101")
public class DisposalMethodDefinitionTest extends AbstractJSR299Test {
    private static final Annotation DEADLIEST_LITERAL = new AnnotationLiteral<Deadliest>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DisposalMethodDefinitionTest.class).withBeansXml("beans.xml")
                .build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.3.5", id = "c"), @SpecAssertion(section = "3.3.4", id = "b"),
            @SpecAssertion(section = "3.3.4", id = "c"), @SpecAssertion(section = "3.3.4", id = "e"),
            @SpecAssertion(section = "3.3.5", id = "ba"), @SpecAssertion(section = "3.3.6", id = "a"),
            @SpecAssertion(section = "3.3.6", id = "b0"), @SpecAssertion(section = "3.3.7", id = "aa"),
            @SpecAssertion(section = "5.5.4", id = "b") })
    public void testBindingTypesAppliedToDisposalMethodParameters() throws Exception {
        assert !SpiderProducer.isTameSpiderDestroyed();
        assert !SpiderProducer.isDeadliestSpiderDestroyed();
        Bean<Tarantula> tarantula = getBeans(Tarantula.class, DEADLIEST_LITERAL).iterator().next();
        CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantula);
        Tarantula instance = tarantula.create(creationalContext);
        tarantula.destroy(instance, creationalContext);
        assert SpiderProducer.isTameSpiderDestroyed();
        assert SpiderProducer.isDeadliestSpiderDestroyed();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.3.4", id = "aa"), @SpecAssertion(section = "3.3.5", id = "ba") })
    public void testDisposalMethodOnNonBean() throws Exception {
        Bean<Tarantula> tarantula = getBeans(Tarantula.class, DEADLIEST_LITERAL).iterator().next();
        CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantula);
        Tarantula instance = getCurrentManager().getContext(tarantula.getScope()).get(tarantula);
        tarantula.destroy(instance, creationalContext);
        assert !DisposalNonBean.isSpiderDestroyed();
    }

    /**
     * In addition to the disposed parameter, a disposal method may declare additional parameters, which may also specify
     * bindings. The container calls Manager.getInstanceToInject() to determine a value for each parameter of a disposal method
     * and calls the disposal method with those parameter values
     * 
     * @throws Exception
     */
    @Test(groups = { "disposalMethod" })
    @SpecAssertions({ @SpecAssertion(section = "3.3.6", id = "h"), @SpecAssertion(section = "3.10", id = "a") })
    public void testDisposalMethodParametersGetInjected() throws Exception {
        Bean<Tarantula> tarantula = getBeans(Tarantula.class, DEADLIEST_LITERAL).iterator().next();
        CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantula);
        Tarantula instance = getCurrentManager().getContext(tarantula.getScope()).get(tarantula);
        tarantula.destroy(instance, creationalContext);
        assert SpiderProducer.isDeadliestSpiderDestroyed();
    }

}
