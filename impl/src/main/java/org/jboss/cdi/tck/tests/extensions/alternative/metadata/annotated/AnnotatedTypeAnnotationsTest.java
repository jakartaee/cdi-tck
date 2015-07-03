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

package org.jboss.cdi.tck.tests.extensions.alternative.metadata.annotated;

import static org.jboss.cdi.tck.cdi.Sections.ALTERNATIVE_METADATA_SOURCES;
import static org.jboss.cdi.tck.util.Assert.assertAnnotationSetMatches;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 * 
 */
@SpecVersion(spec = "cdi", version = "2.0-EDR1")
public class AnnotatedTypeAnnotationsTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AnnotatedTypeAnnotationsTest.class)
                .withExtension(ObservingExtension.class).build();
    }

    @Inject
    ObservingExtension extension;

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "b") })
    public void testCreateAnnotatedType() {
        assertAnnotationSetMatches(getCurrentManager().createAnnotatedType(Android.class).getAnnotations(),
                RequestScoped.class, InheritedQualifier.class, Fate.class);
        assertAnnotationSetMatches(getCurrentManager().createAnnotatedType(Rimmer.class).getAnnotations(), Mortal.class,
                Dependent.class, InheritedQualifier.class, Fate.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "b") })
    public void testProcessAnnotatedType() {

        AnnotatedType<Kryten> kryten = extension.getKryten();
        assertNotNull(kryten);
        assertAnnotationSetMatches(kryten.getAnnotations(), RequestScoped.class, InheritedQualifier.class, Fate.class);

        AnnotatedType<Rimmer> rimmer = extension.getRimmer();
        assertNotNull(rimmer);
        assertAnnotationSetMatches(rimmer.getAnnotations(), Mortal.class, Dependent.class, InheritedQualifier.class, Fate.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "b") })
    public void testGetAnnotatedType() {

        AnnotatedType<Android> android = extension.getAndroid();
        assertNotNull(android);
        assertAnnotationSetMatches(android.getAnnotations(), RequestScoped.class, InheritedQualifier.class, Fate.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "b") })
    public void testGetAnnotatedTypes() {

        List<AnnotatedType<Human>> humans = extension.getAllHumans();
        assertNotNull(humans);
        assertEquals(humans.size(), 1);
        assertAnnotationSetMatches(humans.iterator().next().getAnnotations(), Mortal.class, Dependent.class,
                InheritedQualifier.class, Fate.class);

        List<AnnotatedType<Android>> androids = extension.getAllAndroids();
        assertNotNull(androids);
        assertEquals(androids.size(), 1);
        assertAnnotationSetMatches(androids.iterator().next().getAnnotations(), RequestScoped.class, InheritedQualifier.class,
                Fate.class);
    }

}
