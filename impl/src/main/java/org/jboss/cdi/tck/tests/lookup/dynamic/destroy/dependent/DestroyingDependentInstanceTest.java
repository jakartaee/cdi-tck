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
package org.jboss.cdi.tck.tests.lookup.dynamic.destroy.dependent;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Instance;

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
 * Test for CDI-139. It verifies that Instance.destroy() can be used to destroy a dependent bean instance and bean instances
 * depending on the bean instance are destroyed as well.
 * 
 * <p>
 * This test was originally part of the Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class DestroyingDependentInstanceTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(DestroyingDependentInstanceTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createInterceptors()
                                .clazz(TransactionalInterceptor.class.getName()).up()).build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "5.6.1", id = "n") })
    public void testDestroyingDependentInstances(Instance<Foo> instance) {

        assertNotNull(instance);

        List<Foo> foos = new ArrayList<Foo>();
        List<Qux> quxs = new ArrayList<Qux>();

        for (int i = 0; i < 10; i++) {
            Foo foo = instance.get();
            foo.ping();
            foos.add(foo);
            quxs.add(foo.getQux());
        }

        Foo.reset();
        Qux.reset();
        Baz.reset();

        for (Foo component : foos) {
            instance.destroy(component);
        }
        assertEquals(Foo.getDestroyedComponents(), foos);
        assertEquals(Qux.getDestroyedComponents(), quxs);
        assertFalse(Baz.isDestroyed());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "5.6.1", id = "n") })
    public void testDestroyingInterceptedDependentBean(Instance<Bar> instance) {

        assertNotNull(instance);

        Bar bar = instance.get();
        bar.ping();

        Bar.reset();
        TransactionalInterceptor.reset();
        instance.destroy(bar);

        assertTrue(Bar.isDestroyed());
        assertTrue(TransactionalInterceptor.isDestroyed());
    }
}
