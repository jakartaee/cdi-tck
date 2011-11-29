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
package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;

import static org.jboss.jsr299.tck.TestGroups.INTEGRATION;
import static org.jboss.jsr299.tck.TestGroups.NEW;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.New;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.jsr299.tck.literals.DefaultLiteral;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class NewEnterpriseBeanTest extends AbstractJSR299Test {

    private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(NewEnterpriseBeanTest.class).build();
    }

    @Test(groups = { INTEGRATION, NEW })
    @SpecAssertion(section = "3.14", id = "p")
    public void testNewBeanIsDependentScoped() {
        Set<Bean<WrappedEnterpriseBeanLocal>> beans = getBeans(WrappedEnterpriseBeanLocal.class, WrappedEnterpriseBeanLocal.NEW);
        assert beans.size() == 1;
        Bean<WrappedEnterpriseBeanLocal> newEnterpriseBean = beans.iterator().next();
        assert Dependent.class.equals(newEnterpriseBean.getScope());
    }

    @Test(groups = { INTEGRATION, NEW })
    @SpecAssertion(section = "3.14", id = "r")
    public void testNewBeanIsHasOnlyNewBinding() {
        Set<Bean<WrappedEnterpriseBeanLocal>> beans = getBeans(WrappedEnterpriseBeanLocal.class, WrappedEnterpriseBeanLocal.NEW);
        assert beans.size() == 1;
        Bean<WrappedEnterpriseBeanLocal> newEnterpriseBean = beans.iterator().next();
        assert newEnterpriseBean.getQualifiers().size() == 1;
        assert newEnterpriseBean.getQualifiers().iterator().next().annotationType().equals(New.class);
    }

    @Test(groups = { INTEGRATION, NEW })
    @SpecAssertion(section = "3.14", id = "s")
    public void testNewBeanHasNoBeanELName() {
        Set<Bean<WrappedEnterpriseBeanLocal>> beans = getBeans(WrappedEnterpriseBeanLocal.class, WrappedEnterpriseBeanLocal.NEW);
        assert beans.size() == 1;
        Bean<WrappedEnterpriseBeanLocal> newEnterpriseBean = beans.iterator().next();
        assert newEnterpriseBean.getName() == null;
    }

    @Test(groups = { INTEGRATION, NEW })
    @SpecAssertion(section = "3.14", id = "t")
    public void testNewBeanHasNoStereotypes() {
        Bean<MonkeyLocal> monkeyBean = getBeans(MonkeyLocal.class).iterator().next();
        Bean<MonkeyLocal> newMonkeyBean = getBeans(MonkeyLocal.class, MonkeyLocal.NEW).iterator().next();
        assert monkeyBean.getScope().equals(RequestScoped.class);
        assert newMonkeyBean.getScope().equals(Dependent.class);
        assert monkeyBean.getName().equals("monkey");
        assert newMonkeyBean.getName() == null;
    }

    @Test(groups = { INTEGRATION, NEW })
    @SpecAssertion(section = "3.14", id = "u")
    public void testNewBeanHasNoObservers() {
        // Should just be 1 observer from bean, not new bean
        assert getCurrentManager().resolveObserverMethods("event").size() == 1;
    }

    @Test(groups = { INTEGRATION })
    @SpecAssertions({ @SpecAssertion(section = "3.14", id = "j"), @SpecAssertion(section = "3.14", id = "k") })
    public void testForEachEnterpriseBeanANewBeanExists() {
        Bean<OrderLocal> orderBean = getBeans(OrderLocal.class).iterator().next();
        Bean<OrderLocal> newOrderBean = getBeans(OrderLocal.class, OrderLocal.NEW).iterator().next();
        assert orderBean.getQualifiers().size() == 2;
        assert orderBean.getQualifiers().contains(new DefaultLiteral());
        assert orderBean.getQualifiers().contains(new AnyLiteral());

        assert getBeans(OrderLocal.class, OrderLocal.NEW).size() == 1;
        assert newOrderBean.getQualifiers().size() == 1;
        assert newOrderBean.getQualifiers().iterator().next().annotationType().equals(New.class);

        assert orderBean.getTypes().equals(newOrderBean.getTypes());
        assert orderBean.getBeanClass().equals(newOrderBean.getBeanClass());

        Bean<LionLocal> lionBean = getBeans(LionLocal.class, TAME_LITERAL).iterator().next();
        Bean<LionLocal> newLionBean = getBeans(LionLocal.class, LionLocal.NEW).iterator().next();
        assert getBeans(LionLocal.class, TAME_LITERAL).size() == 1;
        assert lionBean.getQualifiers().size() == 2;
        assert lionBean.getQualifiers().contains(TAME_LITERAL);
        assert lionBean.getQualifiers().contains(new AnyLiteral());

        assert getBeans(LionLocal.class, LionLocal.NEW).size() == 1;
        assert newLionBean.getQualifiers().size() == 1;
        assert newLionBean.getQualifiers().iterator().next().annotationType().equals(New.class);

        assert lionBean.getTypes().equals(newLionBean.getTypes());
        assert lionBean.getBeanClass().equals(newLionBean.getBeanClass());
    }

    @Test(groups = { INTEGRATION, NEW })
    @SpecAssertion(section = "3.14", id = "h")
    public void testNewBeanHasSameInjectedFields() {
        Bean<InitializerSimpleBeanLocal> simpleBean = getBeans(InitializerSimpleBeanLocal.class).iterator().next();
        Bean<InitializerSimpleBeanLocal> newSimpleBean = getBeans(InitializerSimpleBeanLocal.class,
                InitializerSimpleBeanLocal.NEW).iterator().next();
        assert !newSimpleBean.getInjectionPoints().isEmpty();
        assert simpleBean.getInjectionPoints().equals(newSimpleBean.getInjectionPoints());
    }
}
