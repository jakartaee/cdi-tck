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

package org.jboss.cdi.tck.tests.inheritance.generics;

import static org.jboss.cdi.tck.cdi.Sections.MEMBER_LEVEL_INHERITANCE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class MemberLevelInheritanceTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(MemberLevelInheritanceTest.class).build();
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "f")
    public void testInjectionPointDefinition() throws Exception {

        Bean<Foo> fooBean = getUniqueBean(Foo.class);
        Set<InjectionPoint> injectionPoints = fooBean.getInjectionPoints();
        // Baz<T1> baz, T1 t1, Baz<List<T2>> t2BazList, T1[] t1Array
        assertEquals(injectionPoints.size(), 4);

        for (InjectionPoint injectionPoint : injectionPoints) {

            if ("baz".equals(injectionPoint.getMember().getName())) {
                assertEquals(injectionPoint.getType(), new TypeLiteral<Baz<String>>() {
                }.getType());
            } else if ("t1".equals(injectionPoint.getMember().getName())) {
                assertEquals(injectionPoint.getType(), String.class);
            } else if ("t2BazList".equals(injectionPoint.getMember().getName())) {
                assertEquals(injectionPoint.getType(), new TypeLiteral<Baz<List<Qux>>>() {
                }.getType());
            } else if ("setT1Array".equals(injectionPoint.getMember().getName())) {
                // Initializer IP
                assertEquals(injectionPoint.getType(), String[].class);
            } else {
                fail("Unexpected injection point");
            }
        }
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "f")
    public void testInjectionPoint(Foo foo) throws Exception {
        assertNotNull(foo);
        assertNotNull(foo.getBaz());
        assertNotNull(foo.getT1Array());
        assertNotNull(foo.getT2BazList());
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "g")
    public void testObserverResolution() throws Exception {

        Set<ObserverMethod<? super Qux>> observerMethods = getCurrentManager().resolveObserverMethods(new Qux(null));
        assertEquals(observerMethods.size(), 1);
        ObserverMethod<? super Qux> observerMethod = observerMethods.iterator().next();
        assertEquals(observerMethod.getBeanClass(), Foo.class);
        assertEquals(observerMethod.getObservedType(), new TypeLiteral<Baz<String>>() {
        }.getType());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "g")
    public void testObserver(Foo foo) throws Exception {
        assertNotNull(foo);
        getCurrentManager().fireEvent(new Qux(null));
        assertNotNull(foo.getT1BazEvent());
        assertEquals(foo.getT1ObserverInjectionPoint(), "ok");
    }

}
