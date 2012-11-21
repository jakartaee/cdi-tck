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

import static org.testng.Assert.assertEquals;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.shrinkwrap.descriptors.Beans11DescriptorImpl;
import org.jboss.cdi.tck.shrinkwrap.descriptors.BeansXmlClass;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * TODO add assertions
 * 
 * Test resolution of ambiguous dependencies (see also "Unsatisfied and ambiguous dependencies" chapter in the spec).
 * 
 * WAR deployment with 2 libraries:
 * <ul>
 * <li>WEB-INF/classes - alpha - does not declare any alternative, includes {@link TestBean} implementation</li>
 * <li>lib 1 - bravo - declares {@link Foo} alternative selected for the app with priority 1000</li>
 * <li>lib 2 - charlie - declares {@link Bar} unselected alternative with default priority 2000</li>
 * </ul>
 * 
 * Expected result: {@link Foo} is resolved in all bean archives
 * 
 * @author Martin Kouba
 * 
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class SelectedAlternative03Test extends SelectedAlternativeTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return createBuilderBase()
                .withTestClass(SelectedAlternative03Test.class)
                .withClasses(Alpha.class, SimpleTestBean.class)
                .withBeanLibrary(new Beans11DescriptorImpl().alternatives(new BeansXmlClass(Foo.class, 1000)), Bravo.class,
                        Foo.class)
                .withBeanLibrary(new Beans11DescriptorImpl().alternatives(new BeansXmlClass(Bar.class, false, 2000)),
                        Charlie.class, Bar.class).build();
    }

    @Inject
    Alpha alpha;

    @Inject
    Bravo bravo;

    @Inject
    Charlie charlie;

    @Test
    public void testDependencyResolvable() {
        assertEquals(alpha.assertAvailable(TestBean.class).getId(), Foo.class.getName());
        assertEquals(bravo.assertAvailable(TestBean.class).getId(), Foo.class.getName());
        assertEquals(charlie.assertAvailable(TestBean.class).getId(), Foo.class.getName());
    }

}
