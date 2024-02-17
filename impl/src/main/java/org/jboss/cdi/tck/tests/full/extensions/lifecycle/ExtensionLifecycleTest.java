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
package org.jboss.cdi.tck.tests.full.extensions.lifecycle;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.INIT_EVENTS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Note that is't not possible to test that container maintains a reference to extension instance until the application shuts
 * down.
 * 
 * @author Martin Kouba
 */
@Test(groups = CDI_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class ExtensionLifecycleTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ExtensionLifecycleTest.class).withExtension(SimpleExtension.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .build();
    }

    @Inject
    SimpleBean simpleBean;

    @Test
    @SpecAssertion(section = INIT_EVENTS, id = "f")
    public void testContainerProvidesBeanForExtension() {
        // For each service provider the container must provide a bean
        Set<Bean<?>> beans = getCurrentManager().getBeans(SimpleExtension.class);
        assertEquals(beans.size(), 1);
        Bean<?> simpleExtensionBean = beans.iterator().next();
        // Bean scope is @ApplicationScoped
        assertEquals(simpleExtensionBean.getScope(), ApplicationScoped.class);
        // Bean has two qualifiers @Default and @Any
        assertEquals(simpleExtensionBean.getQualifiers().size(), 2);
        assertTrue(simpleExtensionBean.getQualifiers().contains(Default.Literal.INSTANCE));
        assertTrue(simpleExtensionBean.getQualifiers().contains(Any.Literal.INSTANCE));
        // Bean types include the class of the service provider and all superclases and interfaces
        Set<Type> types = simpleExtensionBean.getTypes();
        assertEquals(types.size(), 4);
        assertTrue(types.contains(SuperExtension.class));
        assertTrue(types.contains(Extension.class));
        assertTrue(types.contains(SimpleExtension.class));
        assertTrue(types.contains(Object.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INIT_EVENTS, id = "d"), @SpecAssertion(section = INIT_EVENTS, id = "f") })
    public void testContainerInstantiatesSingleInstanceOfExtension() {
        // The container instantiates a single instance of each extension
        long id = simpleBean.getSimpleExtension().getId();
        assertEquals(getContextualReference(SimpleExtension.class).getId(), id);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INIT_EVENTS, id = "d"), @SpecAssertion(section = INIT_EVENTS, id = "f"),
            @SpecAssertion(section = INIT_EVENTS, id = "bb") })
    public void testContainerDeliversEventNotifications() {
        assertTrue(simpleBean.getSimpleExtension().isContainerEventObserved());
        getCurrentManager().getEvent().select(SimpleEvent.class).fire(new SimpleEvent(System.currentTimeMillis()));
        assertTrue(simpleBean.getSimpleExtension().isSimpleEventObserved());
    }

}
