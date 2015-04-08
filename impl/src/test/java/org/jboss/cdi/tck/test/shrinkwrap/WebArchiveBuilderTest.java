/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.test.shrinkwrap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.test.shrinkwrap.Excluded.Ping;
import org.jboss.cdi.tck.test.shrinkwrap.Qux.Baz;
import org.jboss.cdi.tck.test.shrinkwrap.descriptors.Bar;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

public class WebArchiveBuilderTest {

    @Test
    public void testAddClasses() {

        // Optimization possible, no skipped classes
        WebArchive archive = new WebArchiveBuilder().withTestClass(TestClass.class).withClasses(Qux.class, Excluded.class).build();

        assertContainsClass(archive, Qux.class);
        assertContainsClass(archive, Excluded.class);
        assertContainsClass(archive, TestClass.class);
        assertContainsInnerClass(archive, Baz.class);
        assertContainsInnerClass(archive, Ping.class);

        // Optimization possible, skip excluded class and test class
        archive = new WebArchiveBuilder().withTestClass(TestClass.class).setAsClientMode(true).withClasses(Qux.class, Excluded.class, Engine.class)
                .withExcludedClass(Excluded.class.getName()).build();

        assertContainsClass(archive, Qux.class);
        assertDoesNotContainClass(archive, Excluded.class);
        assertDoesNotContainClass(archive, TestClass.class);
        assertContainsInnerClass(archive, Baz.class);
        assertDoesNotContainInnerClass(archive, Ping.class);
        assertDoesNotContainInnerClass(archive, EnginePowered.EnginePoweredLiteral.class);
        assertDoesNotContainInnerClass(archive, PoweredEngine.PoweredEngineLiteral.class);

        // Inner class included
        archive = new WebArchiveBuilder().withTestClass(TestClass.class).setAsClientMode(true).withClasses(EnginePowered.class).build();
        assertContainsClass(archive, EnginePowered.class);
        assertContainsInnerClass(archive, EnginePowered.EnginePoweredLiteral.class);

        archive = new WebArchiveBuilder().withTestClass(TestClass.class).setAsClientMode(true).withClasses(PoweredEngine.class).build();
        assertContainsClass(archive, PoweredEngine.class);
        assertContainsInnerClass(archive, PoweredEngine.PoweredEngineLiteral.class);

        // Optimization not possible, skip excluded class and test class
        archive = new WebArchiveBuilder().withTestClass(TestClass.class).setAsClientMode(true).withClasses(Qux.class, Excluded.class, Bar.class)
                .withExcludedClass(Excluded.class.getName()).build();

        assertContainsClass(archive, Qux.class);
        assertContainsClass(archive, Bar.class);
        assertDoesNotContainClass(archive, Excluded.class);
        assertDoesNotContainClass(archive, TestClass.class);
        assertContainsInnerClass(archive, Baz.class);
        assertDoesNotContainInnerClass(archive, Ping.class);
    }

    @Test
    public void testAddPackage() {

        WebArchive archive = new WebArchiveBuilder().withTestClassPackage(TestClass.class).build();

        assertContainsClass(archive, Qux.class);
        assertContainsClass(archive, Excluded.class);
        assertContainsClass(archive, TestClass.class);
        assertContainsInnerClass(archive, Baz.class);
        assertContainsInnerClass(archive, Ping.class);

        archive = new WebArchiveBuilder().withTestClassPackage(TestClass.class).setAsClientMode(true).withExcludedClass(Excluded.class.getName()).build();

        assertContainsClass(archive, Qux.class);
        assertDoesNotContainClass(archive, Excluded.class);
        assertDoesNotContainClass(archive, TestClass.class);
        assertContainsInnerClass(archive, Baz.class);
        assertDoesNotContainInnerClass(archive, Ping.class);

        archive = new WebArchiveBuilder().withTestClassPackage(TestClass.class).setAsClientMode(true).withClasses(Qux.class, Excluded.class, Bar.class)
                .withExcludedClass(Excluded.class.getName()).build();

        assertContainsClass(archive, Qux.class);
        assertContainsClass(archive, Bar.class);
        assertDoesNotContainClass(archive, Excluded.class);
        assertDoesNotContainClass(archive, TestClass.class);
        assertContainsInnerClass(archive, Baz.class);
        assertDoesNotContainInnerClass(archive, Ping.class);
    }
    
    @Test
    public void testGeneratedArchiveName() throws NoSuchAlgorithmException {
        WebArchive archive = new WebArchiveBuilder().withTestClass(TestClass.class).build();
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(TestClass.class.getName().getBytes());
        byte[] digest = md.digest();
        
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            hexString.append(Integer.toHexString(0xFF & digest[i]));
        }
        assertEquals(archive.getName().split("\\.")[0], hexString.toString());
    }

    private <A extends Archive<?>, T> void assertContainsClass(A archive, Class<T> clazz) {
        assertTrue(archive.contains("/WEB-INF/classes/" + StringUtils.replace(clazz.getName(), ".", "/") + ".class"));
    }

    private <A extends Archive<?>, T> void assertDoesNotContainClass(A archive, Class<T> clazz) {
        assertFalse(archive.contains("/WEB-INF/classes/" + StringUtils.replace(clazz.getName(), ".", "/") + ".class"));
    }

    private <A extends Archive<?>, T> void assertContainsInnerClass(A archive, Class<T> clazz) {
        for (ArchivePath path : archive.getContent().keySet()) {
            if (path.get().contains(clazz.getSimpleName())) {
                return;
            }
        }
        fail(clazz.getName() + " should be there");
    }

    private <A extends Archive<?>, T> void assertDoesNotContainInnerClass(A archive, Class<T> clazz) {
        for (ArchivePath path : archive.getContent().keySet()) {
            if (path.get().contains(clazz.getSimpleName())) {
                fail(clazz.getName() + " should not be there");
            }
        }
    }

}
