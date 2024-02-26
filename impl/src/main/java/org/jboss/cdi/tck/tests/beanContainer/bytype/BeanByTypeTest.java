/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.beanContainer.bytype;

import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_BEAN_BY_TYPE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.reflect.TypeVariable;
import java.util.Set;

import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests related to obtaining beans by their type from the bean container.
 *
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class BeanByTypeTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(BeanByTypeTest.class)
                .build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_BEAN_BY_TYPE, id = "aa"),
            @SpecAssertion(section = BM_OBTAIN_BEAN_BY_TYPE, id = "b") })
    public void testGetBeans() {
        Set<Bean<?>> beans = getCurrentBeanContainer().getBeans(SimpleBean.class);
        assertEquals(beans.size(), 1);
        assertEquals(beans.iterator().next().getBeanClass(), SimpleBean.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_BEAN_BY_TYPE, id = "c") })
    public void testNoBindingImpliesCurrent() {
        Set<Bean<?>> beans = getCurrentBeanContainer().getBeans(SimpleBean.class);
        assertEquals(beans.size(), 1);
        assertTrue(beans.iterator().next().getQualifiers().contains(Default.Literal.INSTANCE));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_BEAN_BY_TYPE, id = "ab") })
    public void testGetBeansDoesNotResolveAlternatives() {
        Set<Bean<?>> beans = getCurrentBeanContainer().getBeans(Connector.class);
        assertEquals(beans.size(), 2);
        for (Bean<?> bean : beans) {
            if (!typeSetMatches(bean.getTypes(), Object.class, Connector.class)
                    && !typeSetMatches(bean.getTypes(), Object.class, Connector.class, AlternativeConnector.class)) {
                fail("Unexpected bean types found");
            }
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_BEAN_BY_TYPE, id = "da") })
    public <T> void testTypeVariable() {
        TypeVariable<?> t = (TypeVariable<?>) new TypeLiteral<T>() {
        }.getType();
        getCurrentBeanContainer().getBeans(t);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_BEAN_BY_TYPE, id = "e") })
    public void testSameBindingTwice() {
        getCurrentBeanContainer().getBeans(SimpleBean.class, new TameLiteral("a"), new TameLiteral("b"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_BEAN_BY_TYPE, id = "f") })
    public void testNonBindingType() {
        getCurrentBeanContainer().getBeans(SimpleBean.class, new NonBindingTypeLiteral());
    }

}
