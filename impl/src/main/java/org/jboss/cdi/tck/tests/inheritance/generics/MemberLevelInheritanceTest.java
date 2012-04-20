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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ObserverMethod;

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
@SpecVersion(spec = "cdi", version = "20091101")
public class MemberLevelInheritanceTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(MemberLevelInheritanceTest.class).build();
    }

    @Test
    @SpecAssertion(section = "4.2", id = "f")
    public void testInjectionPoint() throws Exception {

        Bean<Foo> fooBean = getUniqueBean(Foo.class);
        Set<InjectionPoint> injectionPoints = fooBean.getInjectionPoints();
        assertEquals(injectionPoints.size(), 1);
        InjectionPoint inheritedInjectionPoint = injectionPoints.iterator().next();

        checkParameterizedType(inheritedInjectionPoint.getType(), Baz.class, String.class);
    }

    @Test
    @SpecAssertion(section = "4.2", id = "j")
    public void testObserver() throws Exception {

        Set<ObserverMethod<? super Qux>> observerMethods = getCurrentManager().resolveObserverMethods(new Qux(null));
        // Foo and Bar
        assertEquals(observerMethods.size(), 2);

        for (ObserverMethod<? super Qux> observerMethod : observerMethods) {
            if (observerMethod.getBeanClass().equals(Foo.class)) {
                checkParameterizedType(observerMethod.getObservedType(), Baz.class, String.class);
                return;
            }
        }
        // No Foo observer
        fail();
    }

    private void checkParameterizedType(Type declaredType, Type rawType, Type argumentType) {

        assertTrue(declaredType instanceof ParameterizedType);
        ParameterizedType parameterizedType = (ParameterizedType) declaredType;

        assertEquals(parameterizedType.getRawType(), rawType);

        Type[] arguments = parameterizedType.getActualTypeArguments();
        assertEquals(arguments.length, 1);
        assertTrue(arguments[0].equals(argumentType));
    }

}
