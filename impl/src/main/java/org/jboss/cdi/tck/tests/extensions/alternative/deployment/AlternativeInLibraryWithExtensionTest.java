/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.cdi.tck.tests.extensions.alternative.deployment;

import static org.jboss.cdi.tck.cdi.Sections.ALTERNATIVES;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_ALTERNATIVE;
import static org.jboss.cdi.tck.cdi.Sections.INITIALIZATION;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * TODO check assertions since this test is not aimed to specific assertion (is a result of practical issue)
 * 
 * This test verifies that alternatives work correctly in BDAs that contain at least one extension.
 * 
 * <p>
 * This test was originally part of Seam Compatibility project.
 * <p>
 * 
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 * @author Martin Kouba
 * @see <a href="http://java.net/jira/browse/GLASSFISH-15791">GLASSFISH-15791</a>
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class AlternativeInLibraryWithExtensionTest extends AbstractTest {

    @Inject
    private Foo foo;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(AlternativeInLibraryWithExtensionTest.class)
                .withBeanLibrary(
                        Descriptors.create(BeansDescriptor.class).createAlternatives().clazz(BarAlternative.class.getName())
                                .up(), Foo.class, Bar.class, BarAlternative.class, NoopExtension.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ALTERNATIVES, id = "a"), @SpecAssertion(section = DECLARING_ALTERNATIVE, id = "a"),
            @SpecAssertion(section = INITIALIZATION, id = "b") })
    public void testAlternative() {
        Assert.assertEquals("barAlternative", foo.getBar().ping());
    }
}
