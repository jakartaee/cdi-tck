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
package org.jboss.cdi.tck.tests.decorators.builtin.injectionpoint;

import static org.jboss.cdi.tck.cdi.Sections.DECORATOR_BEAN_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Collections;

import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.decorators.AbstractDecoratorTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class BuiltinInjectionPointDecoratorTest extends AbstractDecoratorTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(BuiltinInjectionPointDecoratorTest.class)
                .withClass(AbstractDecoratorTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).getOrCreateDecorators()
                                .clazz(InjectionPointDecorator.class.getName()).up()).build();
    }

    @Inject
    Company company;

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_BEAN_EE, id = "acl") })
    public void testDecoratorIsResolved() {
        checkDecorator(resolveUniqueDecorator(Collections.<Type> singleton(InjectionPoint.class)),
                InjectionPointDecorator.class, Collections.<Type> singleton(InjectionPoint.class), InjectionPoint.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_BEAN_EE, id = "acl") })
    public void testDecoratorInvoked() throws Exception {
        assertTrue(company.getFuse().getInjectionPoint().isTransient());
        assertEquals(company.getFuse().getInjectionPoint().getBean(), getUniqueBean(Company.class));
    }
}
