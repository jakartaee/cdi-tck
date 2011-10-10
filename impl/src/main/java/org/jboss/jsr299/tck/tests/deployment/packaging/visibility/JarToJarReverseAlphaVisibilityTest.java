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
package org.jboss.jsr299.tck.tests.deployment.packaging.visibility;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Same as {@link JarToJarAlphaVisibilityTest} but the content of alpha and bravo jars is swapped to detect ordering issues.
 * 
 * <p>
 * This test was originally part of Seam Compatibility project.
 * <p>
 * 
 * TODO verify assertions
 * 
 * @author <a href="mailto:jharting@redhat.com">Jozef Hartinger</a>
 * @author Martin Kouba
 * @see <a href="http://java.net/jira/browse/GLASSFISH-15735">GLASSFISH-15735</a>
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class JarToJarReverseAlphaVisibilityTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(JarToJarReverseAlphaVisibilityTest.class)
                .withBeanLibrary("bravo.jar", Foo.class).withBeanLibrary("alpha.jar", Bar.class).build();
    }

    @Test
    @SpecAssertion(section = "12.1", id = "bcb")
    public void testDeployment() {
    }
}
