/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.interceptors.tests.bindings.aroundConstruct;

import static org.jboss.cdi.tck.util.ActionSequence.assertSequenceDataEquals;

import javax.enterprise.inject.Instance;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of the Weld test suite.
 * <p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "int", version = "1.2")
public class ConstructorInterceptionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(ConstructorInterceptionTest.class)
                .withBeansXml(
                        Descriptors
                                .create(BeansDescriptor.class)
                                .getOrCreateInterceptors()
                                .clazz(AlphaInterceptor1.class.getName(), AlphaInterceptor2.class.getName(),
                                        BravoInterceptor.class.getName()).up()).build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "3.1", id = "a"), @SpecAssertion(section = "3.1", id = "b"),
            @SpecAssertion(section = "3.3", id = "ac"), })
    public void testConstructorLevelBinding(Instance<BeanWithConstructorLevelBinding> instance) {
        ActionSequence.reset();
        instance.get();
        assertSequenceDataEquals(AlphaInterceptor2.class, BeanWithConstructorLevelBinding.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "3.3", id = "bc"), @SpecAssertion(section = "3.4", id = "da"),
            @SpecAssertion(section = "3.4", id = "db"), @SpecAssertion(section = "3.4", id = "dc") })
    public void testMultipleConstructorLevelBinding(Instance<BeanWithMultipleConstructorLevelBinding> instance) {
        ActionSequence.reset();
        instance.get();
        assertSequenceDataEquals(AlphaInterceptor2.class, BravoInterceptor.class, BeanWithMultipleConstructorLevelBinding.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "3.1", id = "a"), @SpecAssertion(section = "3.1", id = "b") })
    public void testTypeLevelBinding(Instance<BeanWithTypeLevelBinding> instance) {
        ActionSequence.reset();
        instance.get();
        assertSequenceDataEquals(AlphaInterceptor1.class, BeanWithTypeLevelBinding.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "3.3", id = "c"), @SpecAssertion(section = "2.3", id = "i") })
    public void testTypeLevelAndConstructorLevelBinding(Instance<BeanWithConstructorLevelAndTypeLevelBinding> instance) {
        ActionSequence.reset();
        instance.get();
        assertSequenceDataEquals(AlphaInterceptor1.class, BravoInterceptor.class, BeanWithConstructorLevelAndTypeLevelBinding.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "3.3", id = "da") })
    public void testOverridingTypeLevelBinding(Instance<BeanOverridingTypeLevelBinding> instance) {
        ActionSequence.reset();
        instance.get();
        assertSequenceDataEquals(AlphaInterceptor2.class, BeanOverridingTypeLevelBinding.class);
    }
}
