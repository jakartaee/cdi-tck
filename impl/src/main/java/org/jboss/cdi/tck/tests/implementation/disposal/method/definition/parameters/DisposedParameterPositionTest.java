/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.implementation.disposal.method.definition.parameters;

import static org.jboss.cdi.tck.cdi.Sections.DECLARING_DISPOSER_METHOD;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_PRODUCER_METHOD;
import static org.jboss.cdi.tck.cdi.Sections.DISPOSER_METHOD_DISPOSED_PARAMETER;
import static org.testng.Assert.assertEquals;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * There is no requirement for disposed parameter position in the spec.
 * 
 * @author Martin Kouba
 * @see <a href="https://issues.jboss.org/browse/CDITCK-271">CDITCK-271</a>
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class DisposedParameterPositionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DisposedParameterPositionTest.class).build();
    }

    @Inject
    Thinker thinker;

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_DISPOSER_METHOD, id = "h"), @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "i"),
            @SpecAssertion(section = DISPOSER_METHOD_DISPOSED_PARAMETER, id = "a") })
    public void testDisposedParameterPosition() {

        assertEquals(thinker.getIdeas().size(), 0);

        Bean<Idea> bean = getUniqueBean(Idea.class);
        CreationalContext<Idea> ctx = getCurrentManager().createCreationalContext(bean);
        Idea instance = bean.create(ctx);

        assertEquals(thinker.getIdeas().size(), 1);

        bean.destroy(instance, ctx);
        assertEquals(thinker.getIdeas().size(), 0);
    }

}
