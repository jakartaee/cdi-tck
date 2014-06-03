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
package org.jboss.cdi.tck.tests.lookup.typesafe.resolution.parameterized;

import static org.jboss.cdi.tck.cdi.Sections.ASSIGNABLE_PARAMETERS;
import static org.jboss.cdi.tck.cdi.Sections.LEGAL_BEAN_TYPES;
import static org.jboss.cdi.tck.cdi.Sections.LEGAL_INJECTION_POINT_TYPES;
import static org.jboss.cdi.tck.cdi.Sections.PERFORMING_TYPESAFE_RESOLUTION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SuppressWarnings("serial")
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class AssignabilityOfRawAndParameterizedTypesTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AssignabilityOfRawAndParameterizedTypesTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "kb"), @SpecAssertion(section = LEGAL_BEAN_TYPES, id = "f"),
            @SpecAssertion(section = LEGAL_BEAN_TYPES, id = "g"), @SpecAssertion(section = ASSIGNABLE_PARAMETERS, id = "a") })
    public void testAssignabilityToRawType() {
        // Dao, DaoProducer.getDao(), DaoProducer.getRawDao and ObjectDao
        // IntegerDao is not assignable to the raw required type Dao
        assertEquals(getBeans(Dao.class).size(), 4);
    }

    @Test
    @SpecAssertion(section = ASSIGNABLE_PARAMETERS, id = "ba")
    public void testAssignabilityOfParameterizedTypeWithActualTypesToParameterizedTypeWithActualTypes() {
        assert getBeans(new TypeLiteral<Map<Integer, Integer>>() {
        }).size() == 2;
        assert getBeans(new TypeLiteral<HashMap<Integer, Integer>>() {
        }).iterator().next().getTypes().contains(IntegerHashMap.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ASSIGNABLE_PARAMETERS, id = "c") })
    public void testAssignabilityOfParameterizedTypeWithActualTypesToParameterizedTypeWithWildcards() {
        assert getBeans(new TypeLiteral<HashMap<? extends Number, ? super Integer>>() {
        }).size() == 1;
        assert getBeans(new TypeLiteral<HashMap<? extends Number, ? super Integer>>() {
        }).iterator().next().getTypes().contains(IntegerHashMap.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = LEGAL_INJECTION_POINT_TYPES, id = "b") })
    public void testAssignabilityOfParameterizedTypeWithActualTypesToParameterizedTypeWithWildcardsAtInjectionPoint() {
        assert getContextualReference(InjectedBean.class).getMap() instanceof IntegerHashMap;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ASSIGNABLE_PARAMETERS, id = "da"),
            @SpecAssertion(section = ASSIGNABLE_PARAMETERS, id = "dc") })
    public void testAssignabilityOfParameterizedTypeWithTypeVariablesToParameterizedTypeWithWildcards() {
        Set<Bean<Result<? extends Throwable, ? super Exception>>> beans = getBeans(new TypeLiteral<Result<? extends Throwable, ? super Exception>>() {
        });
        assert beans.size() == 1;
        assert rawTypeSetMatches(beans.iterator().next().getTypes(), ResultImpl.class, Result.class, Object.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ASSIGNABLE_PARAMETERS, id = "db"),
            @SpecAssertion(section = ASSIGNABLE_PARAMETERS, id = "dc") })
    public void testAssignabilityOfParameterizedTypeWithTypeVariablesToParameterizedTypeWithWildcards2() {
        Set<Bean<Result<? extends RuntimeException, ? super RuntimeException>>> beans = getBeans(new TypeLiteral<Result<? extends RuntimeException, ? super RuntimeException>>() {
        });
        assert beans.size() == 1;
        assert rawTypeSetMatches(beans.iterator().next().getTypes(), ResultImpl.class, Result.class, Object.class);
    }

    @Test
    @SpecAssertion(section = ASSIGNABLE_PARAMETERS, id = "dc")
    public void testAssignabilityOfParameterizedTypeWithTypeVariablesToParameterizedTypeWithWildcardsBroken() {
        Set<Bean<Result<? extends Exception, ? super Throwable>>> beans = getBeans(new TypeLiteral<Result<? extends Exception, ? super Throwable>>() {
        });
        assertEquals(beans.size(), 0);
    }

    @Test
    @SpecAssertion(section = ASSIGNABLE_PARAMETERS, id = "e")
    public void testAssignabilityOfParameterizedTypeWithTypeVariablesToParameterizedTypeWithActualTypes() {
        Set<Bean<Result<RuntimeException, IllegalStateException>>> beans = getBeans(new TypeLiteral<Result<RuntimeException, IllegalStateException>>() {
        });
        assert beans.size() == 1;
        assert rawTypeSetMatches(beans.iterator().next().getTypes(), ResultImpl.class, Result.class, Object.class);

        Set<Bean<Result<RuntimeException, Throwable>>> noBeans = getBeans(new TypeLiteral<Result<RuntimeException, Throwable>>() {
        });
        assertEquals(noBeans.size(), 0);
    }

    @Test
    @SpecAssertion(section = ASSIGNABLE_PARAMETERS, id = "f")
    public <T1 extends RuntimeException, T2 extends T1, T3> void testAssignabilityOfParameterizedTypeWithTypeVariablesToParameterizedTypeWithTypeVariable() {
        Set<Bean<Result<T1, T2>>> beans = getBeans(new TypeLiteral<Result<T1, T2>>() {
        });
        assert beans.size() == 1;
        assert rawTypeSetMatches(beans.iterator().next().getTypes(), ResultImpl.class, Result.class, Object.class);

        Set<Bean<Result<T1, T3>>> noBeans = getBeans(new TypeLiteral<Result<T1, T3>>() {
        });
        assertEquals(noBeans.size(), 0);

        Set<Bean<Dao<T1, T3>>> daoBeans = getBeans(new TypeLiteral<Dao<T1, T3>>() {
        });
        assertEquals(daoBeans.size(), 1);
        assertTrue(rawTypeSetMatches(daoBeans.iterator().next().getTypes(), Dao.class, Object.class));
    }

}
