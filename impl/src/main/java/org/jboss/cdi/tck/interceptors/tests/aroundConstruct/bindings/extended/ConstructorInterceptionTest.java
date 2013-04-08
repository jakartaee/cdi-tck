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
package org.jboss.cdi.tck.interceptors.tests.aroundConstruct.bindings.extended;

import static org.testng.Assert.assertEquals;

import java.util.List;

import javax.enterprise.inject.Instance;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * There are no assertions assigned at the moment - it's not clear whether Interceptors 1.2 spec should have its own TCK.
 *
 * <p>
 * This test was originally part of the Weld test suite.
 * <p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "int", version = "3.1.PFD")
public class ConstructorInterceptionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(ConstructorInterceptionTest.class)
                .withBeansXml(
                        Descriptors
                                .create(BeansDescriptor.class)
                                .createInterceptors()
                                .clazz(AlphaInterceptor1.class.getName(), AlphaInterceptor2.class.getName(), BravoInterceptor.class.getName()).up()).build();
    }

    @Test(dataProvider=ARQUILLIAN_DATA_PROVIDER)
    public void testConstructorLevelBinding(Instance<BeanWithConstructorLevelBinding> instance) {
        ActionSequence.reset();
        instance.get();
        assertSequenceEquals(AlphaInterceptor2.class, BeanWithConstructorLevelBinding.class);
    }

    @Test(dataProvider=ARQUILLIAN_DATA_PROVIDER)
    public void testTypeLevelBinding(Instance<BeanWithTypeLevelBinding> instance) {
        ActionSequence.reset();
        instance.get();
        assertSequenceEquals(AlphaInterceptor1.class, BeanWithTypeLevelBinding.class);
    }

    @Test(dataProvider=ARQUILLIAN_DATA_PROVIDER)
    public void testTypeLevelAndConstructorLevelBinding(Instance<BeanWithConstructorLevelAndTypeLevelBinding> instance) {
        ActionSequence.reset();
        instance.get();
        assertSequenceEquals(AlphaInterceptor1.class, BravoInterceptor.class, BeanWithConstructorLevelAndTypeLevelBinding.class);
    }

    @Test(dataProvider=ARQUILLIAN_DATA_PROVIDER)
    public void testOverridingTypeLevelBinding(Instance<BeanOverridingTypeLevelBinding> instance) {
        ActionSequence.reset();
        instance.get();
        assertSequenceEquals(AlphaInterceptor2.class, BeanOverridingTypeLevelBinding.class);
    }

    private void assertSequenceEquals(Class<?>... expected) {
        List<String> data = ActionSequence.getSequence().getData();
        assertEquals(expected.length, data.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getSimpleName(), data.get(i));
        }
    }
}
