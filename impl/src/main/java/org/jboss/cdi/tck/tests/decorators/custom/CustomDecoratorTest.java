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
package org.jboss.cdi.tck.tests.decorators.custom;

import static org.jboss.cdi.tck.cdi.Sections.AFTER_BEAN_DISCOVERY;
import static org.jboss.cdi.tck.cdi.Sections.DECORATOR_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_ANNOTATED_TYPE;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class CustomDecoratorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(CustomDecoratorTest.class).withBeansXml("beans.xml")
                .withExtension(AfterBeanDiscoveryObserver.class).build();
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_RESOLUTION, id = "b"), @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "bc"),
            @SpecAssertion(section = AFTER_BEAN_DISCOVERY, id = "dc") })
    public void testCustomImplementationOfDecoratorInterface() {
        assert getContextualReference(Vehicle.class).start().equals("Bus started and decorated.");
        assert getContextualReference(Vehicle.class).stop().equals("Bus stopped and decorated.");
        assert AfterBeanDiscoveryObserver.getDecorator().isGetDecoratedTypesCalled();
        assert AfterBeanDiscoveryObserver.getDecorator().isGetDelegateQualifiersCalled();
        assert AfterBeanDiscoveryObserver.getDecorator().isGetDelegateTypeCalled();
        assert !getCurrentManager().resolveDecorators(new HashSet<Type>(Arrays.asList(Vehicle.class))).isEmpty();
        assert !getCurrentManager().resolveDecorators(new HashSet<Type>(Arrays.asList(Vehicle.class, Bus.class))).isEmpty();
    }
}
