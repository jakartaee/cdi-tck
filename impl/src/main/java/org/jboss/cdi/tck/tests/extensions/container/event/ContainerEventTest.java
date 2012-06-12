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
package org.jboss.cdi.tck.tests.extensions.container.event;

import static javax.enterprise.inject.spi.SessionBeanType.STATEFUL;
import static javax.enterprise.inject.spi.SessionBeanType.STATELESS;
import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;

import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.AnnotatedType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.EnterpriseArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for ProcessAnnotatedType, ProcessBean and ProcessInjectionTarget events.
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = JAVAEE_FULL)
@SpecVersion(spec = "cdi", version = "20091101")
public class ContainerEventTest extends AbstractTest {

    @Deployment
    public static EnterpriseArchive createTestArchive() {
        return new EnterpriseArchiveBuilder().withTestClassPackage(ContainerEventTest.class)
                .withExtension("javax.enterprise.inject.spi.Extension").withEjbJarXml("ejb-jar.xml").build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "12.4", id = "da"), @SpecAssertion(section = "11.5.8", id = "aaa") })
    public void testProcessInjectionTargetFiredForManagedBean() {
        assert ProcessInjectionTargetObserver.getManagedBeanEvent() != null;
        validateManagedBean(ProcessInjectionTargetObserver.getManagedBeanEvent().getAnnotatedType());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.8", id = "aab"), @SpecAssertion(section = "11.5.8", id = "abb"),
            @SpecAssertion(section = "12.4", id = "db") })
    public void testProcessInjectionTargetFiredForSessionBean() {
        assert ProcessInjectionTargetObserver.getStatelessSessionBeanEvent() != null;
        assert ProcessInjectionTargetObserver.getStatefulSessionBeanEvent() != null;
        validateStatelessSessionBean(ProcessInjectionTargetObserver.getStatelessSessionBeanEvent().getAnnotatedType());
        validateStatefulSessionBean(ProcessInjectionTargetObserver.getStatefulSessionBeanEvent().getAnnotatedType());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.8", id = "aaf"), @SpecAssertion(section = "11.5.8", id = "abf"),
            @SpecAssertion(section = "12.4", id = "dh") })
    public void testProcessInjectionTargetFiredForSessionBeanInterceptor() {
        assert ProcessInjectionTargetObserver.getSessionBeanInterceptorEvent() != null;
        validateSessionBeanInterceptor(ProcessInjectionTargetObserver.getSessionBeanInterceptorEvent().getAnnotatedType());
    }

    @Test
    @SpecAssertion(section = "12.4", id = "ba")
    public void testProcessAnnotatedTypeFiredForManagedBean() {
        assert ProcessAnnotatedTypeObserver.getManagedBeanEvent() != null;
        validateManagedBean(ProcessAnnotatedTypeObserver.getManagedBeanEvent().getAnnotatedType());
    }

    @Test
    @SpecAssertion(section = "12.4", id = "bb")
    public void testProcessAnnotatedTypeFiredForSessionBean() {
        assert ProcessAnnotatedTypeObserver.getStatelessSessionBeanEvent() != null;
        assert ProcessAnnotatedTypeObserver.getStatefulSessionBeanEvent() != null;
        validateStatelessSessionBean(ProcessAnnotatedTypeObserver.getStatelessSessionBeanEvent().getAnnotatedType());
        validateStatefulSessionBean(ProcessAnnotatedTypeObserver.getStatefulSessionBeanEvent().getAnnotatedType());
    }

    @Test
    @SpecAssertion(section = "12.4", id = "bh")
    public void testProcessAnnotatedTypeFiredForSessionBeanInterceptor() {
        assert ProcessAnnotatedTypeObserver.getSessionBeanInterceptorEvent() != null;
        validateSessionBeanInterceptor(ProcessAnnotatedTypeObserver.getSessionBeanInterceptorEvent().getAnnotatedType());
    }

    @Test
    @SpecAssertion(section = "11.5.11", id = "ba")
    public void testProcessManagedBeanFired() {
        assert ProcessBeanObserver.getProcessManagedBeanEvent() != null;
        validateManagedBean(ProcessBeanObserver.getProcessManagedBeanEvent().getAnnotatedBeanClass());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.11", id = "c"), @SpecAssertion(section = "12.4", id = "fb") })
    public void testProcessSessionBeanFiredForStatelessSessionBean() {
        assert ProcessBeanObserver.getProcessStatelessSessionBeanEvent() != null;
        validateStatelessSessionBean(ProcessBeanObserver.getProcessStatelessSessionBeanEvent().getAnnotatedBeanClass());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.11", id = "c"), @SpecAssertion(section = "12.4", id = "fb") })
    public void testProcessSessionBeanFiredForStatefulSessionBean() {
        assert ProcessBeanObserver.getProcessStatefulSessionBeanEvent() != null;
        validateStatefulSessionBean(ProcessBeanObserver.getProcessStatefulSessionBeanEvent().getAnnotatedBeanClass());
    }

    @Test
    @SpecAssertion(section = "11.5.11", id = "hb")
    public void testGetEJBName() {
        assert ProcessBeanObserver.getProcessStatelessSessionBeanEvent().getEjbName().equals("sheep");
        assert ProcessBeanObserver.getProcessStatefulSessionBeanEvent().getEjbName().equals("cow");
    }

    @Test
    @SpecAssertion(section = "11.5.11", id = "hc")
    public void testGetSessionBeanType() {
        assert ProcessBeanObserver.getProcessStatelessSessionBeanEvent().getSessionBeanType().equals(STATELESS);
        assert ProcessBeanObserver.getProcessStatefulSessionBeanEvent().getSessionBeanType().equals(STATEFUL);
    }

    private void validateStatelessSessionBean(Annotated type) {
        assert type.getBaseType().equals(Sheep.class);
        assert rawTypeSetMatches(type.getTypeClosure(), Sheep.class, SheepLocal.class, Object.class);
        assert type.getAnnotations().size() == 2;
        assert annotationSetMatches(type.getAnnotations(), Tame.class, Stateless.class); // TODO
                                                                                         // Check
    }

    private void validateStatefulSessionBean(Annotated type) {
        assert type.getBaseType().equals(Cow.class);
        assert rawTypeSetMatches(type.getTypeClosure(), Cow.class, CowLocal.class, Object.class);
        assert type.getAnnotations().size() == 0;
    }

    private void validateSessionBeanInterceptor(AnnotatedType<SheepInterceptor> type) {
        assert type.getBaseType().equals(SheepInterceptor.class);
        assert rawTypeSetMatches(type.getTypeClosure(), SheepInterceptor.class, Object.class);
        assert type.getAnnotations().size() == 0;
        assert type.getFields().size() == 0;
        assert type.getMethods().size() == 1;
    }

    private void validateManagedBean(AnnotatedType<Farm> type) {
        assert type.getBaseType().equals(Farm.class);
        assert rawTypeSetMatches(type.getTypeClosure(), Farm.class, Object.class);
        assert type.getFields().size() == 1;
        assert type.getFields().iterator().next().isAnnotationPresent(Produces.class);
        assert type.getMethods().size() == 1;
        assert type.getMethods().iterator().next().isAnnotationPresent(Produces.class);
    }

}
