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
@SpecVersion(spec = "cdi", version = "20091101")
public class BeanByTypeTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BeanByTypeTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.3.5", id = "aa"), @SpecAssertion(section = "11.3.5", id = "b") })
    public void testGetBeans() {
        Set<Bean<?>> beans = getCurrentManager().getBeans(SimpleBean.class);
        assert beans.size() == 1;
        assert beans.iterator().next().getBeanClass().equals(SimpleBean.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.3.5", id = "c") })
    public void testNoBindingImpliesCurrent() {
        Set<Bean<?>> beans = getCurrentManager().getBeans(SimpleBean.class);
        assert beans.size() == 1;
        assert beans.iterator().next().getQualifiers().contains(new DefaultLiteral());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertions({ @SpecAssertion(section = "11.3.5", id = "da") })
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
    @SpecAssertions({ @SpecAssertion(section = "11.3.5", id = "e") })
    public void testSameBindingTwice() {
        getCurrentManager().getBeans(SimpleBean.class, new TameLiteral(), new TameLiteral());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertions({ @SpecAssertion(section = "11.3.5", id = "f") })
    public void testNonBindingType() {
        getCurrentManager().getBeans(SimpleBean.class, new NonBindingTypeLiteral());
    }

}
