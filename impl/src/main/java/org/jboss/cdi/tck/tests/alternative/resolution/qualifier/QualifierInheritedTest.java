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

package org.jboss.cdi.tck.tests.alternative.resolution.qualifier;

import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE;
import static org.jboss.cdi.tck.cdi.Sections.PERFORMING_TYPESAFE_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_LEVEL_INHERITANCE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.lang.annotation.Inherited;

import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0-EDR1")
public class QualifierInheritedTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(QualifierInheritedTest.class)
                .withClasses(Tree.class, Larch.class, Forest.class, True.class, TrueLiteral.class)
                .withBeansXml(Descriptors.create(BeansDescriptor.class).getOrCreateAlternatives().clazz(Larch.class.getName()).up())
                .build();
    }

    @Inject
    private Forest forest;

    /**
     * {@link Larch} is enabled alternative and extends {@link Tree}, which has {@link True} qualifier that declares the
     * {@link Inherited} meta-annotation. Therefore the result of typesafe resolution for type {@link Tree} and qualifier
     * {@link True} is the {@link Larch} bean.
     *
     * @throws Exception
     */
    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE, id = "ba"), @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "la"),
            @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "aa") })
    public void testResolution() throws Exception {

        Bean<?> bean = getCurrentManager().resolve(getCurrentManager().getBeans(Tree.class, TrueLiteral.INSTANCE));
        assertEquals(bean.getBeanClass(), Larch.class);

        assertNotNull(forest);
        assertEquals(forest.getTree().ping(), 0);
    }

}
