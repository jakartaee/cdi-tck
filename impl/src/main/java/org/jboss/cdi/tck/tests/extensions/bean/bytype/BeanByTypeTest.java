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

package org.jboss.cdi.tck.tests.extensions.bean.bytype;

import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_BEAN_BY_TYPE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.DefaultLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests related to obtaining beans by their type from the bean manager.
 * 
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class BeanByTypeTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(BeanByTypeTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createAlternatives()
                                .clazz(AlternativeConnector.class.getName()).up()).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_BEAN_BY_TYPE, id = "aa"), @SpecAssertion(section = BM_OBTAIN_BEAN_BY_TYPE, id = "b") })
    public void testGetBeans() {
        Set<Bean<?>> beans = getCurrentManager().getBeans(SimpleBean.class);
        assertEquals(beans.size(), 1);
        assertEquals(beans.iterator().next().getBeanClass(), SimpleBean.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_BEAN_BY_TYPE, id = "c") })
    public void testNoBindingImpliesCurrent() {
        Set<Bean<?>> beans = getCurrentManager().getBeans(SimpleBean.class);
        assertEquals(beans.size(), 1);
        assertTrue(beans.iterator().next().getQualifiers().contains(new DefaultLiteral()));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_BEAN_BY_TYPE, id = "ab") })
    public void testGetBeansDoesNotResolveAlternatives() {
        Set<Bean<?>> beans = getCurrentManager().getBeans(Connector.class);
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
    public void testTypeVariable() {
        TypeVariable<?> t = new TypeVariable<GenericDeclaration>() {

            public Type[] getBounds() {
                return new Type[0];
            }

            public GenericDeclaration getGenericDeclaration() {
                return null;
            }

            public String getName() {
                return "";
            }
        };
        getCurrentManager().getBeans(t);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_BEAN_BY_TYPE, id = "e") })
    public void testSameBindingTwice() {
        getCurrentManager().getBeans(SimpleBean.class, new TameLiteral(), new TameLiteral());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_BEAN_BY_TYPE, id = "f") })
    public void testNonBindingType() {
        getCurrentManager().getBeans(SimpleBean.class, new NonBindingTypeLiteral());
    }

}
