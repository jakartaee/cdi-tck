/*
 * JBoss, Home of Professional Open Source
 * Copyright 2017, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.definition.bean.types.illegal;

import static org.jboss.cdi.tck.cdi.Sections.MANAGED_BEAN_TYPES;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_FIELD_TYPES;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_METHOD_TYPES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class BeanTypesWithIllegalTypeTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BeanTypesWithIllegalTypeTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = MANAGED_BEAN_TYPES, id = "b") })
    public void beanSetOfBeanTypesContainsOnlyLegalTypes() {
        getUniqueEagleAndCheckItsTypes();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_FIELD_TYPES, id = "d") })
    public void producerFieldsetOfBeanTypesContainsOnlyLegalTypes() {
        getUniqueEagleAndCheckItsTypes(ProducedWithField.ProducedWithFieldLiteral.INSTANCE);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD_TYPES, id = "d") })
    public void producerMethodsetOfBeanTypesContainsOnlyLegalTypes() {
        getUniqueEagleAndCheckItsTypes(ProducedWithMethod.ProducedWithMethoddLiteral.INSTANCE);
    }

    private void getUniqueEagleAndCheckItsTypes(Annotation... annotations){
        Bean<Eagle> eagleBean = getUniqueBean(Eagle.class, annotations);
        for (Type type : eagleBean.getTypes()) {
            if(type instanceof ParameterizedType){
                assertNotEquals(((ParameterizedType) type).getRawType(), AnimalHolder.class);
            }
        }
        assertEquals(eagleBean.getTypes().size(), 3);
    }

}
