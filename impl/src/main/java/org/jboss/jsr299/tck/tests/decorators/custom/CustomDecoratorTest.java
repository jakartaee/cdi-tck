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
package org.jboss.jsr299.tck.tests.decorators.custom;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class CustomDecoratorTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(CustomDecoratorTest.class).withBeansXml("beans.xml")
                .withExtension("javax.enterprise.inject.spi.Extension").build();
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = "8.3", id = "b"), @SpecAssertion(section = "11.5.6", id = "bc"),
            @SpecAssertion(section = "11.5.2", id = "dc") })
    public void testCustomImplementationOfDecoratorInterface() {
        assert getInstanceByType(Vehicle.class).start().equals("Bus started and decorated.");
        assert getInstanceByType(Vehicle.class).stop().equals("Bus stopped and decorated.");
        assert AfterBeanDiscoveryObserver.getDecorator().isGetDecoratedTypesCalled();
        assert AfterBeanDiscoveryObserver.getDecorator().isGetDelegateQualifiersCalled();
        assert AfterBeanDiscoveryObserver.getDecorator().isGetDelegateTypeCalled();
        assert !getCurrentManager().resolveDecorators(new HashSet<Type>(Arrays.asList(Vehicle.class))).isEmpty();
        assert !getCurrentManager().resolveDecorators(new HashSet<Type>(Arrays.asList(Vehicle.class, Bus.class))).isEmpty();
    }
}
