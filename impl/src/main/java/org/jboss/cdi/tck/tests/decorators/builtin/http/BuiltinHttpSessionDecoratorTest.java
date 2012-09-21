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

package org.jboss.cdi.tck.tests.decorators.builtin.http;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import javax.enterprise.inject.spi.Decorator;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 * 
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class BuiltinHttpSessionDecoratorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(BuiltinHttpSessionDecoratorTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createDecorators()
                                .clazz(HttpSessionDecorator.class.getName()).up()).build();
    }

    @Inject
    HttpSession httpSession;

    @Inject
    HttpSessionObserver httpSessionObserver;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "8.4", id = "ac"), @SpecAssertion(section = "8.3", id = "aa") })
    public void testDecoratorIsResolved() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(Collections.<Type> singleton(HttpSession.class));
        assertNotNull(decorators);
        assertEquals(decorators.size(), 1);
        assertTrue(HttpSessionDecorator.class.equals(decorators.iterator().next().getBeanClass()));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "8.4", id = "ac") })
    public void testDecoratorIsInvoked() {
        httpSession.invalidate();
        assertTrue(httpSessionObserver.isDestroyed());
        assertTrue(httpSessionObserver.isDecorated());
    }
}
