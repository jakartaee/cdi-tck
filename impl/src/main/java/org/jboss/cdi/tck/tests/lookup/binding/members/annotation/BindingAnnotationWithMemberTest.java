/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.lookup.binding.members.annotation;

import static org.jboss.cdi.tck.cdi.Sections.QUALIFIER_ANNOTATION_MEMBERS;
import static org.testng.Assert.assertNotNull;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * This test originally tested non-portable behavior (qualifier annotation with annotation-valued member) which does not make
 * sense since non-portable behavior is not testable.
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class BindingAnnotationWithMemberTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BindingAnnotationWithMemberTest.class).build();
    }

    @Inject
    TheBeatles theBeatles;

    @Test
    @SpecAssertion(section = QUALIFIER_ANNOTATION_MEMBERS, id = "b")
    public void testAnnotationMemberWithNonBinding() {
        assertNotNull(theBeatles.getWatch());
    }

}
