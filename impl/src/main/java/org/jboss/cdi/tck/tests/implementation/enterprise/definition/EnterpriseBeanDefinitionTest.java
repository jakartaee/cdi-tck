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
package org.jboss.cdi.tck.tests.implementation.enterprise.definition;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Nicklas Karlsson
 * @author Martin Kouba
 */
@Test(groups = { INTEGRATION })
@SpecVersion(spec = "cdi", version = "20091101")
public class EnterpriseBeanDefinitionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnterpriseBeanDefinitionTest.class).withBeansXml("beans.xml")
                .build();
    }

    @Test
    @SpecAssertion(section = "3.2", id = "b")
    public void testStatelessMustBeDependentScoped() {
        assert getBeans(GiraffeLocal.class).size() == 1;
        assert getBeans(GiraffeLocal.class).iterator().next().getScope().equals(Dependent.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.8.1", id = "ab"), @SpecAssertion(section = "5.5.1", id = "ab") })
    public void testConstructorAnnotatedInjectCalled() {
        ExplicitConstructor bean = getInstanceByType(ExplicitConstructor.class);
        assert bean.getConstructorCalls() == 1;
        assert bean.getInjectedSimpleBean() instanceof SimpleBean;
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = "5.5.1", id = "bb")
    public void testConstructorWithNoParamsUsed(NoParamConstructorSessionBean noParamConstructorSessionBean) {
        noParamConstructorSessionBean.ping();
        assertTrue(NoParamConstructorSessionBean.constructedCorrectly);
    }

    @Test
    @SpecAssertion(section = "3.2", id = "c")
    public void testSingletonWithDependentScopeOK() {
        assert getBeans(Labrador.class).size() == 1;
    }

    @Test
    @SpecAssertion(section = "3.2", id = "c")
    public void testSingletonWithApplicationScopeOK() {
        assert getBeans(Laika.class).size() == 1;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.2.2", id = "aa"), @SpecAssertion(section = "3.2.3", id = "c") })
    public void testBeanTypesAreLocalInterfacesWithoutWildcardTypesOrTypeVariablesWithSuperInterfaces() {
        Bean<DogLocal> dogBean = getBeans(DogLocal.class).iterator().next();
        assert dogBean.getTypes().contains(DogLocal.class);
        assert dogBean.getTypes().contains(PitbullLocal.class);
        assert !dogBean.getTypes().contains(Pitbull.class);
    }

    @Test
    @SpecAssertion(section = "3.2.2", id = "ba")
    public void testEnterpriseBeanClassLocalView() {
        Bean<Retriever> dogBean = getUniqueBean(Retriever.class);
        assert dogBean.getTypes().contains(Retriever.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.2.2", id = "c"), @SpecAssertion(section = "3.2.3", id = "aa"),
            @SpecAssertion(section = "2.2", id = "l") })
    public void testObjectIsInAPITypes() {
        assert getBeans(GiraffeLocal.class).size() == 1;
        assert getBeans(GiraffeLocal.class).iterator().next().getTypes().contains(Object.class);
    }

    @Test
    @SpecAssertion(section = "3.2.2", id = "d")
    public void testRemoteInterfacesAreNotInAPITypes() {
        Bean<DogLocal> dogBean = getBeans(DogLocal.class).iterator().next();
        assert !dogBean.getTypes().contains(DogRemote.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.2.3", id = "ba"), @SpecAssertion(section = "3.2", id = "e") })
    public void testBeanWithScopeAnnotation() {
        Bean<LionLocal> lionBean = getBeans(LionLocal.class).iterator().next();
        assert lionBean.getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertion(section = "3.2.3", id = "bb")
    public void testBeanWithNamedAnnotation() {
        Bean<MonkeyLocal> monkeyBean = getBeans(MonkeyLocal.class).iterator().next();
        assert monkeyBean.getName().equals("Monkey");
    }

    @Test
    @SpecAssertion(section = "3.2.3", id = "bd")
    public void testBeanWithStereotype() {
        Bean<PolarBearLocal> polarBearBean = getBeans(PolarBearLocal.class).iterator().next();
        assert polarBearBean.getScope().equals(RequestScoped.class);
        assert polarBearBean.getName().equals("polarBear");
    }

    @Test
    @SpecAssertion(section = "3.2.3", id = "be")
    public void testBeanWithQualifiers() {
        Annotation tame = new AnnotationLiteral<Tame>() {
        };
        Bean<ApeLocal> apeBean = getBeans(ApeLocal.class, tame).iterator().next();
        assert apeBean.getQualifiers().contains(tame);
    }

    @Test
    @SpecAssertion(section = "3.2.5", id = "a")
    public void testDefaultName() {
        assert getBeans(PitbullLocal.class).size() == 1;
        assert getBeans(PitbullLocal.class).iterator().next().getName().equals("pitbull");
    }

}
