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
 * The simplest possible scenario - test various alternatives are selected for the entire application, no deselection, no
 * priority ordering during resolution.
 * 
 * WAR deployment with 2 libraries:
 * <ul>
 * <li>WEB-INF/classes - alpha - does not declare any alternative</li>
 * <li>lib 1 - bravo - declares {@link Foo} alternative selected for the app with priority 1000 and alternative stereotype
 * {@link SelectedStereotype} for the app with priority 60</li>
 * <li>lib 2 - charlie - declares {@link Bar} producer alternatives selected for the app with priority 1100</li>
 * </ul>
 * 
 * Expected results:
 * <ul>
 * <li>{@link Foo} is available for injection in all bean archives</li>
 * <li>{@link Baz} is available for injection in all bean archives</li>
 * <li>{@link Bar} with {@link Wild} qualifier is available for injection in all bean archives</li>
 * <li>{@link Bar} with {@link Tame} qualifier is available for injection in all bean archives</li>
 * </ul>
 * 
 * @author Martin Kouba
 * 
 */
@Test(groups = { INTEGRATION })
@SpecVersion(spec = "cdi", version = "20091101")
public class SelectedAlternative01Test extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return createBuilderBase()
                .withTestClass(SelectedAlternative01Test.class)
                .withClasses(Alpha.class)
                .withBeanLibrary(
                        new Beans11DescriptorImpl().alternatives(new BeansXmlClass(Foo.class, 1000), new BeansXmlStereotype(
                                SelectedStereotype.class, 60)), Bravo.class, Foo.class, Baz.class)
                .withBeanLibrary(new Beans11DescriptorImpl().alternatives(new BeansXmlClass(BarProducer.class, 1100)),
                        Charlie.class, Bar.class, BarProducer.class).build();
    }

    @Inject
    Alpha alpha;

    @Inject
    Bravo bravo;

    @Inject
    Charlie charlie;

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES, id = "ba") })
    public void testAlternativeManagedBeanSelected() {
        alpha.assertAvailable(Foo.class);
        bravo.assertAvailable(Foo.class);
        charlie.assertAvailable(Foo.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES, id = "bc"), @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES, id = "bd") })
    public void testAlternativeProducerSelected() {
        // Producer field
        alpha.assertAvailable(Bar.class, WildLiteral.INSTANCE);
        bravo.assertAvailable(Bar.class, WildLiteral.INSTANCE);
        charlie.assertAvailable(Bar.class, WildLiteral.INSTANCE);
        // Producer method
        alpha.assertAvailable(Bar.class, TameLiteral.INSTANCE);
        bravo.assertAvailable(Bar.class, TameLiteral.INSTANCE);
        charlie.assertAvailable(Bar.class, TameLiteral.INSTANCE);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES, id = "bf") })
    public void testAlternativeStereotypeSelected() {
        alpha.assertAvailable(Baz.class);
        bravo.assertAvailable(Baz.class);
        charlie.assertAvailable(Baz.class);
    }

}
