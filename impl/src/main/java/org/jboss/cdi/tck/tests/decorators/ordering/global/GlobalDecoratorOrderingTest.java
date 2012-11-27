/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.decorators.ordering.global;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.descriptors.Beans11DescriptorImpl;
import org.jboss.cdi.tck.shrinkwrap.descriptors.BeansXmlClass;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of the Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class GlobalDecoratorOrderingTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {

        return new WebArchiveBuilder()
                .withTestClass(GlobalDecoratorOrderingTest.class)
                .withClasses(DecoratedImpl.class, LegacyDecorator1.class, LegacyDecorator2.class, LegacyDecorator3.class,
                        WebApplicationGlobalDecorator.class)
                .withBeansXml(
                        new Beans11DescriptorImpl().decorators(
                                //
                                new BeansXmlClass(LegacyDecorator1.class), new BeansXmlClass(LegacyDecorator2.class),
                                new BeansXmlClass(LegacyDecorator3.class),
                                // Disable GED3
                                new BeansXmlClass(GloballyEnabledDecorator3.class, false),
                                // Enabled GPD1
                                new BeansXmlClass(GloballyPrioritizedDecorator1.class, true),
                                //
                                new BeansXmlClass(GloballyEnabledDecorator5.class),
                                // Enabled WAGD globally
                                new BeansXmlClass(WebApplicationGlobalDecorator.class, 1008)))
                .withBeanLibrary(
                        new Beans11DescriptorImpl().decorators(
                        // globally enabled decorators
                                new BeansXmlClass(GloballyEnabledDecorator4.class, 1025), new BeansXmlClass(
                                        GloballyEnabledDecorator5.class, 800), new BeansXmlClass(
                                        GloballyEnabledDecorator1.class, 995), new BeansXmlClass(
                                        GloballyEnabledDecorator2.class, 1005), new BeansXmlClass(
                                        GloballyEnabledDecorator3.class, 1015),
                                // decorators with globally set priority (but disabled)
                                new BeansXmlClass(GloballyPrioritizedDecorator1.class, false, 1015), new BeansXmlClass(
                                        GloballyPrioritizedDecorator2.class, false, 1020)), AbstractDecorator.class,
                        Decorated.class, GloballyEnabledDecorator1.class, GloballyEnabledDecorator2.class,
                        GloballyEnabledDecorator3.class, GloballyEnabledDecorator4.class, GloballyEnabledDecorator5.class,
                        GloballyPrioritizedDecorator1.class, GloballyPrioritizedDecorator2.class).build();
    }

    @Inject
    private Decorated decorated;

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = "8.2", id = "b"), @SpecAssertion(section = "8.2", id = "c"),
            @SpecAssertion(section = "8.2", id = "d"), @SpecAssertion(section = "8.2", id = "e"),
            @SpecAssertion(section = "8.2", id = "f") })
    public void testDecoratorsInWebInfClasses() {

        List<String> expected = new ArrayList<String>();
        // 995
        expected.add(GloballyEnabledDecorator1.class.getSimpleName());
        // 1000
        expected.add(LegacyDecorator1.class.getSimpleName());
        // 1005
        expected.add(GloballyEnabledDecorator2.class.getSimpleName());
        // 1008
        expected.add(WebApplicationGlobalDecorator.class.getSimpleName());
        // 1000 + 10
        expected.add(LegacyDecorator2.class.getSimpleName());
        // 1015
        expected.add(GloballyPrioritizedDecorator1.class.getSimpleName());
        // 1000 + 20
        expected.add(LegacyDecorator3.class.getSimpleName());
        // 1025
        expected.add(GloballyEnabledDecorator4.class.getSimpleName());
        // 1000 + 30 (originally 800)
        expected.add(GloballyEnabledDecorator5.class.getSimpleName());
        // Bean itself
        expected.add(DecoratedImpl.class.getSimpleName());

        List<String> actual = new ArrayList<String>();
        decorated.getSequence(actual);
        assertEquals(actual, expected);
    }
}
