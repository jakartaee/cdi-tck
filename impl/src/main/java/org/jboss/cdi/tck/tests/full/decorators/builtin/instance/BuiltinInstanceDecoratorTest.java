/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.full.decorators.builtin.instance;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DECORATOR_INVOCATION;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.util.TypeLiteral;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.full.decorators.AbstractDecoratorTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Basic test for decorating built-in {@link Instance} bean.
 *
 * @author Martin Kouba
 *
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class BuiltinInstanceDecoratorTest extends AbstractDecoratorTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(BuiltinInstanceDecoratorTest.class)
                .withClass(AbstractDecoratorTest.class)
                .withBeansXml(
                        new BeansXml().decorators(MuleInstanceDecorator.class))
                .build();
    }

    @Inject
    Instance<Mule> instance;

    @SuppressWarnings({ "serial" })
    @Test
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_INVOCATION, id = "acb") })
    public void testDecoratorIsResolved() {
        TypeLiteral<Instance<Mule>> instanceLiteral = new TypeLiteral<Instance<Mule>>() {
        };
        TypeLiteral<Provider<Mule>> providerLiteral = new TypeLiteral<Provider<Mule>>() {
        };
        TypeLiteral<Iterable<Mule>> iterableLiteral = new TypeLiteral<Iterable<Mule>>() {
        };
        checkDecorator(
                resolveUniqueDecorator(Collections.singleton(instanceLiteral.getType())),
                MuleInstanceDecorator.class,
                new HashSet<Type>(
                        Arrays.asList(instanceLiteral.getType(), providerLiteral.getType(), iterableLiteral.getType())),
                instanceLiteral.getType());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_INVOCATION, id = "acb") })
    public void testDecoratorIsInvoked() {
        assertTrue(instance.isAmbiguous());
        Mule mule = instance.get();
        assertNotNull(mule);
    }
}
