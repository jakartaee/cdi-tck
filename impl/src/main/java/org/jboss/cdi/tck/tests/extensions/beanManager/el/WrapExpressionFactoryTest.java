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

package org.jboss.cdi.tck.tests.extensions.beanManager.el;

import static org.jboss.cdi.tck.cdi.Sections.BM_WRAP_EXPRESSIONFACTORY;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import jakarta.el.ExpressionFactory;
import jakarta.el.MethodExpression;
import jakarta.el.ValueExpression;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test wrapping a Unified EL ExpressionFactory and enforcing rules defined in Section 6.4.3, Dependent pseudo-scope and Unified
 * EL.
 * <ol>
 * <li>Dependent bean is instantiated at most once</li>
 * <li>The resulting instance is reused for every appearance of the EL name</li>
 * <li>The resulting instance is destroyed when the evaluation completes</li>
 * </ol>
 * 
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class WrapExpressionFactoryTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(WrapExpressionFactoryTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_WRAP_EXPRESSIONFACTORY, id = "a"), @SpecAssertion(section = BM_WRAP_EXPRESSIONFACTORY, id = "b") })
    public void testWrapping() {

        ActionSequence.reset();

        // Wrap custom expression factory
        ExpressionFactory wrappedExpressionFactory = getCurrentManager().wrapExpressionFactory(new DummyExpressionFactory());

        // Create method expression and invoke it with supplied EL context (porting package)
        MethodExpression methodExpression = wrappedExpressionFactory.createMethodExpression(null, "foo.test", String.class,
                null);
        Object methodElResult = methodExpression.invoke(getCurrentConfiguration().getEl().createELContext(getCurrentManager()),
                null);
        assertNotNull(methodElResult);
        assertTrue(methodElResult instanceof Integer);
        assertEquals(methodElResult, Integer.valueOf(-1));

        List<String> fooSingleton = Collections.singletonList(Foo.class.getName());
        assertEquals(ActionSequence.getSequenceData("create"), fooSingleton);
        assertEquals(ActionSequence.getSequenceData("destroy"), fooSingleton);
        ActionSequence.reset();

        // Create value expression and get value with supplied EL context (porting package)
        ValueExpression valueExpression = wrappedExpressionFactory.createValueExpression(null, "foo.test", String.class);
        Object valElResult = valueExpression.getValue(getCurrentConfiguration().getEl().createELContext(getCurrentManager()));
        assertNotNull(valElResult);
        assertTrue(valElResult instanceof Integer);
        assertEquals(valElResult, Integer.valueOf(-1));

        assertEquals(ActionSequence.getSequenceData("create"), fooSingleton);
        assertEquals(ActionSequence.getSequenceData("destroy"), fooSingleton);
    }

}
