/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.context.dependent.instance;

import static org.jboss.cdi.tck.cdi.Sections.DEPENDENT_DESTRUCTION;
import static org.jboss.cdi.tck.cdi.Sections.DEPENDENT_OBJECTS;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * An instance of a bean with scope <code>@Dependent</code> obtained by direct invocation of an <code>Instance</code> is a
 * dependent object of the instance of <code>Instance</code>.
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class BuiltinInstanceDependentObjectTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BuiltinInstanceDependentObjectTest.class).build();
    }

    @Test
    @SpecAssertion(section = DEPENDENT_OBJECTS, id = "i")
    @SpecAssertion(section = DEPENDENT_DESTRUCTION, id = "ccd")
    public void testInstanceDependentObject() {
        Foo.reset();
        getCurrentManager().getEvent().select(GoodEvent.class).fire(new GoodEvent());
        assertTrue(Foo.created);
        assertTrue(Foo.destroyed);
    }

}
