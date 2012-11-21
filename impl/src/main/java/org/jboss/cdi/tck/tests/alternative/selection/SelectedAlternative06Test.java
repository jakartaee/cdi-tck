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

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.shrinkwrap.descriptors.Beans11DescriptorImpl;
import org.jboss.cdi.tck.shrinkwrap.descriptors.BeansXmlClass;
import org.jboss.cdi.tck.tests.alternative.selection.Tame.TameLiteral;
import org.jboss.cdi.tck.tests.alternative.selection.Wild.WildLiteral;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecVersion;

/**
 * TODO add assertions and complete the test
 * 
 * Test a little bit more complicated scenario.
 * 
 * WAR deployment with 2 libraries:
 * <ul>
 * <li>WEB-INF/classes - alpha -</li>
 * <li>lib 1 - bravo - declares {@link Foo} alternative selected for the app with priority 1000</li>
 * <li>lib 2 - charlie - declares {@link Bar} unselected alternative with default priority 2000</li>
 * <li>lib 3 - delta - declares {@link Bar} unselected alternative with default priority 2000</li>
 * </ul>
 * 
 * Expected results:
 * <ul>
 * <li>TODO/li>
 * <li>{@link Qux} is not available for injection since {@link UnselectedStereotype} is not selected in any archive</li>
 * </ul>
 * 
 * @author Martin Kouba
 * 
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class SelectedAlternative06Test extends SelectedAlternativeTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return createBuilderBase()
                .withTestClass(SelectedAlternative06Test.class)
                .withBeansXml(new Beans11DescriptorImpl().alternatives(new BeansXmlClass(Bar.class, true)))
                .withClasses(Alpha.class, SimpleTestBean.class, Qux.class, QuxProducer.class)
                .withBeanLibrary(
                        new Beans11DescriptorImpl().alternatives(new BeansXmlClass(Foo.class, 1000), new BeansXmlClass(
                                Bar.class, false, 2000)), Bravo.class, Foo.class, Bar.class)
                .withBeanLibrary(new Beans11DescriptorImpl().alternatives(), Charlie.class).build();
    }

    @Inject
    Alpha alpha;

    @Inject
    Bravo bravo;

    @Inject
    Charlie charlie;

    // @Test
    public void testBeansWithUnselectedStereotype() {
        alpha.assertUnsatisfied(Qux.class);
        bravo.assertUnsatisfied(Qux.class);
        charlie.assertUnsatisfied(Qux.class);

        alpha.assertUnsatisfied(Qux.class, TameLiteral.INSTANCE);
        bravo.assertUnsatisfied(Qux.class, TameLiteral.INSTANCE);
        charlie.assertUnsatisfied(Qux.class, TameLiteral.INSTANCE);

        alpha.assertUnsatisfied(Qux.class, WildLiteral.INSTANCE);
        bravo.assertUnsatisfied(Qux.class, WildLiteral.INSTANCE);
        charlie.assertUnsatisfied(Qux.class, WildLiteral.INSTANCE);
    }

}
