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

package org.jboss.cdi.tck.tests.extensions.communication;

import static org.jboss.cdi.tck.cdi.Sections.INIT_EVENTS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0-PFD")
public class ExtensionsCommunicationTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ExtensionsCommunicationTest.class)
                .withExtensions(ExtensionAlpha.class, ExtensionBeta.class).build();
    }

    /**
     * An extension may use BeanManager.fireEvent() to deliver events to observer methods defined on extensions.
     */
    @Test
    @SpecAssertion(section = INIT_EVENTS, id = "bc")
    public void testEvents() {

        // Fired by alpha, recorder by beta
        ActionSequence patSeq = ActionSequence.getSequence(EventBase.PAT_SEQ);
        assertNotNull(patSeq);
        patSeq.assertDataContainsAll(Foo.class.getName(), Bar.class.getName(), EventBase.class.getName(),
                PatEvent.class.getName(), PbEvent.class.getName(), ExtensionAlpha.class.getName(),
                ExtensionBeta.class.getName());

        // Fired by beta, recorder by alpha
        ActionSequence pbSeq = ActionSequence.getSequence(EventBase.PB_SEQ);
        assertNotNull(pbSeq);
        assertEquals(pbSeq.getData().size(), 2);
        pbSeq.assertDataContainsAll(Foo.class.getName(), Bar.class.getName());
    }

}
