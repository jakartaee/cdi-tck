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
package org.jboss.jsr299.tck.tests.lookup.injection.non.contextual;

import static org.jboss.jsr299.tck.TestGroups.INTEGRATION;

import java.util.EventListener;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * This test verifies that ProcessAnnotatedType and ProcessInjectionTarget events are fired for various Java EE components and
 * tests the AnnotatedType implementation.
 * 
 * @author Jozef Hartinger
 * 
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class ContainerEventTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ContainerEventTest.class).withWebXml("web.xml")
                .withExtension("javax.enterprise.inject.spi.Extension")
                .withWebResource("ManagedBeanTestPage.jsp", "ManagedBeanTestPage.jsp")
                .withWebResource("TagPage.jsp", "TagPage.jsp").withWebResource("faces-config.xml", "/WEB-INF/faces-config.xml")
                .withWebResource("TestLibrary.tld", "WEB-INF/TestLibrary.tld").build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.8", id = "aac"), @SpecAssertion(section = "11.5.8", id = "abc"),
            @SpecAssertion(section = "12.4", id = "de") })
    public void testProcessInjectionTargetEventFiredForServletListener() {
        assert ProcessInjectionTargetObserver.getListenerEvent() != null;
        validateServletListenerAnnotatedType(ProcessInjectionTargetObserver.getListenerEvent().getAnnotatedType());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.8", id = "aad"), @SpecAssertion(section = "11.5.8", id = "abd"),
            @SpecAssertion(section = "12.4", id = "df") })
    public void testProcessInjectionTargetEventFiredForTagHandler() {
        assert ProcessInjectionTargetObserver.getTagHandlerEvent() != null;
        validateTagHandlerAnnotatedType(ProcessInjectionTargetObserver.getTagHandlerEvent().getAnnotatedType());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.8", id = "aae"), @SpecAssertion(section = "11.5.8", id = "abe"),
            @SpecAssertion(section = "12.4", id = "dg") })
    public void testProcessInjectionTargetEventFiredForTagLibraryListener() {
        assert ProcessInjectionTargetObserver.getTagLibraryListenerEvent() != null;
        validateTagLibraryListenerAnnotatedType(ProcessInjectionTargetObserver.getTagLibraryListenerEvent().getAnnotatedType());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.8", id = "aah"), @SpecAssertion(section = "11.5.8", id = "abh"),
            @SpecAssertion(section = "12.4", id = "dj") })
    public void testProcessInjectionTargetEventFiredForServlet() {
        assert ProcessInjectionTargetObserver.getServletEvent() != null;
        validateServletAnnotatedType(ProcessInjectionTargetObserver.getServletEvent().getAnnotatedType());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.8", id = "aai"), @SpecAssertion(section = "11.5.8", id = "abi"),
            @SpecAssertion(section = "12.4", id = "dk") })
    public void testProcessInjectionTargetEventFiredForFilter() {
        assert ProcessInjectionTargetObserver.getFilterEvent() != null;
        validateFilterAnnotatedType(ProcessInjectionTargetObserver.getFilterEvent().getAnnotatedType());
    }

    @Test
    @SpecAssertion(section = "12.4", id = "dd")
    public void testProcessInjectionTargetEventFiredForJsfManagedBean() {
        assert ProcessInjectionTargetObserver.getJsfManagedBeanEvent() != null;
        validateJsfManagedBeanAnnotatedType(ProcessInjectionTargetObserver.getJsfManagedBeanEvent().getAnnotatedType());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.8", id = "aas"), @SpecAssertion(section = "11.5.8", id = "aao"),
            @SpecAssertion(section = "11.5.8", id = "aan"), @SpecAssertion(section = "11.5.8", id = "aap") })
    public void testTypeOfProcessInjectionTargetParameter() {
        assert !ProcessInjectionTargetObserver.isStringObserved();
        assert ProcessInjectionTargetObserver.isTagHandlerSubTypeObserved();
        assert !ProcessInjectionTargetObserver.isTagHandlerSuperTypeObserved();
        assert !ProcessInjectionTargetObserver.isServletSuperTypeObserved();
        assert ProcessInjectionTargetObserver.isServletSubTypeObserved();
        assert !ProcessInjectionTargetObserver.isListenerSuperTypeObserved();
    }

    @Test
    @SpecAssertion(section = "12.4", id = "be")
    public void testProcessAnnotatedTypeEventFiredForServletListener() {
        assert ProcessAnnotatedTypeObserver.getListenerEvent() != null;
        validateServletListenerAnnotatedType(ProcessAnnotatedTypeObserver.getListenerEvent().getAnnotatedType());
    }

    @Test
    @SpecAssertion(section = "12.4", id = "bf")
    public void testProcessAnnotatedTypeEventFiredForTagHandler() {
        assert ProcessAnnotatedTypeObserver.getTagHandlerEvent() != null;
        validateTagHandlerAnnotatedType(ProcessAnnotatedTypeObserver.getTagHandlerEvent().getAnnotatedType());
    }

    @Test
    @SpecAssertion(section = "12.4", id = "bg")
    public void testProcessAnnotatedTypeEventFiredForTagLibraryListener() {
        assert ProcessAnnotatedTypeObserver.getTagLibraryListenerEvent() != null;
        validateTagLibraryListenerAnnotatedType(ProcessAnnotatedTypeObserver.getTagLibraryListenerEvent().getAnnotatedType());
    }

    @Test
    @SpecAssertion(section = "12.4", id = "bj")
    public void testProcessAnnotatedTypeEventFiredForServlet() {
        assert ProcessAnnotatedTypeObserver.getServletEvent() != null;
        validateServletAnnotatedType(ProcessAnnotatedTypeObserver.getServletEvent().getAnnotatedType());
    }

    @Test
    @SpecAssertion(section = "12.4", id = "bk")
    public void testProcessAnnotatedTypeEventFiredForFilter() {
        assert ProcessAnnotatedTypeObserver.getFilterEvent() != null;
        validateFilterAnnotatedType(ProcessAnnotatedTypeObserver.getFilterEvent().getAnnotatedType());
    }

    @Test
    @SpecAssertion(section = "12.4", id = "bd")
    public void testProcessAnnotatedTypeEventFiredForJsfManagedBean() {
        assert ProcessAnnotatedTypeObserver.getJsfManagedBeanEvent() != null;
        validateJsfManagedBeanAnnotatedType(ProcessAnnotatedTypeObserver.getJsfManagedBeanEvent().getAnnotatedType());
    }

    private void validateServletListenerAnnotatedType(AnnotatedType<TestListener> type) {
        assert type.getBaseType().equals(TestListener.class);
        assert type.getAnnotations().isEmpty();
        assert type.getFields().size() == 2;
        assert type.getMethods().size() == 3;

        int initializers = 0;
        for (AnnotatedMethod<?> method : type.getMethods()) {
            assert method.getParameters().size() == 1;
            assert method.getBaseType().equals(void.class);
            if (method.isAnnotationPresent(Inject.class)) {
                initializers++;
            }
        }
        assert initializers == 1;
    }

    private void validateTagHandlerAnnotatedType(AnnotatedType<TestTagHandler> type) {
        assert type.getBaseType().equals(TestTagHandler.class);
        assert rawTypeSetMatches(type.getTypeClosure(), TestTagHandler.class, SimpleTagSupport.class, SimpleTag.class,
                JspTag.class);
        assert type.getAnnotations().size() == 1;
        assert type.isAnnotationPresent(Any.class);
    }

    private void validateTagLibraryListenerAnnotatedType(AnnotatedType<TagLibraryListener> type) {
        assert type.getBaseType().equals(TagLibraryListener.class);
        assert rawTypeSetMatches(type.getTypeClosure(), TagLibraryListener.class, ServletContextListener.class,
                EventListener.class, Object.class);
        assert type.getFields().size() == 2;
        assert type.getConstructors().size() == 1;
        assert type.getMethods().size() == 3;
    }

    private void validateServletAnnotatedType(AnnotatedType<TestServlet> type) {
        assert type.getBaseType().equals(TestServlet.class);
        assert rawTypeSetMatches(type.getTypeClosure(), TestServlet.class, HttpServlet.class, GenericServlet.class,
                Servlet.class, ServletConfig.class, Object.class);
        assert type.getAnnotations().isEmpty();
    }

    private void validateFilterAnnotatedType(AnnotatedType<TestFilter> type) {
        assert type.getBaseType().equals(TestFilter.class);
        assert rawTypeSetMatches(type.getTypeClosure(), TestFilter.class, Filter.class, Object.class);
        assert type.getFields().size() == 4;
        assert type.getConstructors().size() == 1;
        assert type.getConstructors().iterator().next().getParameters().isEmpty();
        assert type.getMethods().size() == 4;
    }

    private void validateJsfManagedBeanAnnotatedType(AnnotatedType<Farm> type) {
        assert type.getFields().size() == 2;
        for (AnnotatedField<?> field : type.getFields()) {
            if (field.getJavaMember().getName().equals("sheep")) {
                assert field.isAnnotationPresent(Inject.class);
                assert !field.isStatic();
            } else if (field.getJavaMember().getName().equals("initializerCalled")) {
                assert !field.isStatic();
                assert field.getBaseType().equals(boolean.class);
            } else {
                assert false; // there is no other field
            }
        }
        assert type.getMethods().size() == 3;
    }
}
