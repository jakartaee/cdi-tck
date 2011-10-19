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
package org.jboss.jsr299.tck.tests.extensions.lifecycle;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

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

/**
 * Note that is't not possible to test that container maintains a reference to extension instance until the application shuts
 * down.
 * 
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class ExtensionLifecycleTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ExtensionLifecycleTest.class).withExtension(SimpleExtension.class)
                .build();
    }

    @Inject
    SimpleBean simpleBean;

    @Test
    @SpecAssertion(section = "11.5", id = "e")
    public void testContainerProvidesBeanForExtension() {
        // For each service provider the container must provide a bean
        Set<Bean<?>> beans = getCurrentManager().getBeans(SimpleExtension.class);
        assertEquals(beans.size(), 1);
        Bean<?> simpleExtensionBean = beans.iterator().next();
        // Bean scope is @ApplicationScoped
        assertEquals(simpleExtensionBean.getScope(), ApplicationScoped.class);
        // Bean has two qualifiers @Default and @Any
        assertEquals(simpleExtensionBean.getQualifiers().size(), 2);
        assertTrue(simpleExtensionBean.getQualifiers().contains(new DefaultLiteral()));
        assertTrue(simpleExtensionBean.getQualifiers().contains(new AnyLiteral()));
        // Bean types include the class of the service provider and all superclases and interfaces
        Set<Type> types = simpleExtensionBean.getTypes();
        assertEquals(types.size(), 4);
        assertTrue(types.contains(SuperExtension.class));
        assertTrue(types.contains(Extension.class));
        assertTrue(types.contains(SimpleExtension.class));
        assertTrue(types.contains(Object.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5", id = "d"), @SpecAssertion(section = "11.5", id = "e") })
    public void testContainerInstantiatesSingleInstanceOfExtension() {
        // The container instantiates a single instance of each extension
        long id = simpleBean.getSimpleExtension().getId();
        assertEquals(getInstanceByType(SimpleExtension.class).getId(), id);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5", id = "d"), @SpecAssertion(section = "11.5", id = "e"),
            @SpecAssertion(section = "11.5", id = "bb") })
    public void testContainerDeliversEventNotifications() {
        assertTrue(simpleBean.getSimpleExtension().isContainerEventObserved());
        getCurrentManager().fireEvent(new SimpleEvent(System.currentTimeMillis()));
        assertTrue(simpleBean.getSimpleExtension().isSimpleEventObserved());
    }

}
