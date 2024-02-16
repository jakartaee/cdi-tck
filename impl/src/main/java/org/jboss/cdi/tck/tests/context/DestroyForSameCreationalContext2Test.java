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
package org.jboss.cdi.tck.tests.context;

import static org.jboss.cdi.tck.cdi.Sections.CONTEXT;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.spi.Context;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.spi.Contextuals;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author Nicklas Karlsson
 * @author Pete Muir
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class DestroyForSameCreationalContext2Test extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DestroyForSameCreationalContext2Test.class).build();
    }

    @Test
    @SpecAssertion(section = CONTEXT, id = "r")
    public void testDestroyForSameCreationalContextOnly() {
        // Check that the cc is called (via cc.release()) when we request a context destroyed
        // Note that this is an indirect effect
        Context requestContext = getCurrentManager().getContext(RequestScoped.class);

        // We also test this directly using an inspectable contextual, and ensuring that the same creational context is passed to both methods
        Contextuals.Inspectable<String> contextual = createInspectableContextual("123", requestContext);
        requestContext.get(contextual, getCurrentManager().createCreationalContext(contextual));
        destroyContext(requestContext);
        assert contextual.getCreationalContextPassedToCreate() == contextual.getCreationalContextPassedToDestroy();
    }

}
