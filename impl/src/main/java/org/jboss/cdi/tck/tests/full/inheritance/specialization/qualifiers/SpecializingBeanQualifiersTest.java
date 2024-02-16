/*
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.inheritance.specialization.qualifiers;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DIRECT_AND_INDIRECT_SPECIALIZATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Set;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class SpecializingBeanQualifiersTest extends AbstractTest {

    @Inject
    BeanManager beanManager;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SpecializingBeanQualifiersTest.class).build();
    }

    @Test
    @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION, id = "j")
    public void testQuailifiersOfSpecializingdNestedClass() {
        testQualifiersOfSpecializedBean(StaticNestedClassesParent.StaticSpecializationBean.class, StaticNestedClassesParent.StaticMockSpecializationBean.class);
    }

    @Test
    @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION, id = "j")
    public void testQuailifiersOfSpecializingBean() {
        testQualifiersOfSpecializedBean(SpecializationBean.class, MockSpecializationBean.class);
    }

    @Test
    @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION, id = "j")
    public void testQualifiersOfProducedSpecializingBean() {
        testAndReturnSpecializedBeanWithQualifiers(DataProvider.class);
    }

    private void testQualifiersOfSpecializedBean(Class<?> specializedClass, Class<?> specializingClass) {
        Bean<?> bean = testAndReturnSpecializedBeanWithQualifiers(specializedClass);
        assertTrue(bean.getTypes().contains(specializingClass));
    }

    private Bean<?> testAndReturnSpecializedBeanWithQualifiers(Class<?> specializedClass) {
        Set<Bean<?>> specializationBeans = beanManager.getBeans(specializedClass, new Mock.MockLiteral());
        assertEquals(1, specializationBeans.size());

        Bean<?> bean = specializationBeans.iterator().next();
        Set<Annotation> qualifiers = bean.getQualifiers();
        assertEquals(2, qualifiers.size());
        assertTrue(qualifiers.contains(Any.Literal.INSTANCE));
        assertTrue(qualifiers.contains(new Mock.MockLiteral()));
        return bean;
    }

}
