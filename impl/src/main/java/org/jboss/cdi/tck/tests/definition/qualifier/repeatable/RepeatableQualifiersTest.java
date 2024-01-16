/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.definition.qualifier.repeatable;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class RepeatableQualifiersTest extends AbstractTest {

    private static final Start.StartLiteral A = new Start.StartLiteral("A");
    private static final Start.StartLiteral B = new Start.StartLiteral("B");
    private static final Start.StartLiteral C = new Start.StartLiteral("C");

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(RepeatableQualifiersTest.class).build();
    }

    @Inject
    @Any
    Instance<Process> processInstance;

    @Inject
    ProcessObserver observer;

    @Test
    @SpecAssertion(section = Sections.REPEATING_QUALIFIERS, id = "a")
    public void resolutionWithRepeatableQualifiers() {
        assertEquals(processInstance.stream().count(), 4);
        assertTrue(processInstance.select(A).isResolvable());
        assertFalse(processInstance.select(B).isResolvable());
        assertTrue(processInstance.select(C).isResolvable());
        assertTrue(processInstance.select(B, C).isResolvable());

        assertEquals(observer.getProcessAObserved(), 1);
        assertEquals(observer.getProcessBObserved(), 2);
        assertEquals(observer.getProcessABObserved(), 1);

        assertEquals(observer.getProcessAMetadata().getQualifiers().size(), 3);
        assertTrue(observer.getProcessAMetadata().getQualifiers().contains(A));

        assertEquals(observer.getProcessBCMetadata().getQualifiers().size(), 4);
        assertTrue(observer.getProcessBCMetadata().getQualifiers().contains(B));
        assertTrue(observer.getProcessBCMetadata().getQualifiers().contains(C));
    }
}
