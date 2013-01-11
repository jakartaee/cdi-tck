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

package org.jboss.cdi.tck.tests.alternative.selection;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_ALTERNATIVES;
import static org.jboss.cdi.tck.tests.alternative.selection.SelectedAlternativeTestUtil.createBuilderBase;
import static org.testng.Assert.assertEquals;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.descriptors.Beans11DescriptorImpl;
import org.jboss.cdi.tck.shrinkwrap.descriptors.BeansXmlClass;
import org.jboss.cdi.tck.shrinkwrap.descriptors.BeansXmlStereotype;
import org.jboss.cdi.tck.tests.alternative.selection.Tame.TameLiteral;
import org.jboss.cdi.tck.tests.alternative.selection.Wild.WildLiteral;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test selecting alternative with default priority.
 * 
 * WAR deployment with 2 libraries:
 * <ul>
 * <li>WEB-INF/classes - alpha - does not declare any alternative, includes {@link TestBean} implementation, has {@link Bar}
 * alternative selected</li>
 * <li>lib 1 - bravo - declares {@link Foo} unselected alternative with default priority 1000 and unselected alternative
 * stereotype {@link SelectedStereotype} with default priority 60</li>
 * <li>lib 2 - charlie - declares {@link Bar} unselected alternative with default priority 2000</li>
 * </ul>
 * 
 * Expected result:
 * <ul>
 * <li>{@link Bar} is available for injection in alpha/li>
 * <li>{@link Foo} is available for injection in bravo, charlie</li>
 * <li>{@link Bar} with {@link Wild} qualifier is available for injection in alpha</li>
 * <li>{@link Bar} with {@link Tame} qualifier is available for injection in alpha</li>
 * </ul>
 * 
 * @author Martin Kouba
 * 
 */
@Test(groups = { INTEGRATION })
@SpecVersion(spec = "cdi", version = "20091101")
public class SelectedAlternative05Test extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return createBuilderBase()
                .withTestClass(SelectedAlternative05Test.class)
                .withBeansXml(
                        new Beans11DescriptorImpl().alternatives(new BeansXmlClass(Bar.class, true), new BeansXmlClass(
                                BarProducer.class, true), new BeansXmlStereotype(SelectedStereotype.class, true)))
                .withClasses(Alpha.class, SimpleTestBean.class)
                .withBeanLibrary(
                        new Beans11DescriptorImpl().alternatives(new BeansXmlClass(Foo.class, 1000), new BeansXmlStereotype(
                                SelectedStereotype.class, false, 60)), Bravo.class, Foo.class, Baz.class)
                .withBeanLibrary(
                        new Beans11DescriptorImpl().alternatives(new BeansXmlClass(Bar.class, false, 2000), new BeansXmlClass(
                                BarProducer.class, false, 1100)), Charlie.class, Bar.class, BarProducer.class).build();
    }

    @Inject
    Alpha alpha;

    @Inject
    Bravo bravo;

    @Inject
    Charlie charlie;

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES, id = "ea"), @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES, id = "fa") })
    public void testManagedBeanWithDefaultPrioritySelected() {
        assertEquals(alpha.assertAvailable(TestBean.class).getId(), Bar.class.getName());
        assertEquals(bravo.assertAvailable(TestBean.class).getId(), Foo.class.getName());
        assertEquals(charlie.assertAvailable(TestBean.class).getId(), Foo.class.getName());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES, id = "ec"), @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES, id = "ed"),
            @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES, id = "fc"), @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES, id = "fd") })
    public void testAlternativeProducerSelected() {
        // Producer field
        alpha.assertAvailable(Bar.class, WildLiteral.INSTANCE);
        bravo.assertUnsatisfied(Bar.class, WildLiteral.INSTANCE);
        charlie.assertUnsatisfied(Bar.class, WildLiteral.INSTANCE);
        // Producer method
        alpha.assertAvailable(Bar.class, TameLiteral.INSTANCE);
        bravo.assertUnsatisfied(Bar.class, TameLiteral.INSTANCE);
        charlie.assertUnsatisfied(Bar.class, TameLiteral.INSTANCE);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES, id = "ef"), @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES, id = "ff") })
    public void testAlternativeStereotypeSelected() {
        alpha.assertAvailable(Baz.class);
        bravo.assertUnsatisfied(Baz.class);
        charlie.assertUnsatisfied(Baz.class);
    }

}
