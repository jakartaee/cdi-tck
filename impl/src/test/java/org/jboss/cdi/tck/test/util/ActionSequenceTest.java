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

package org.jboss.cdi.tck.test.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jboss.cdi.tck.util.ActionSequence;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 * @author Matus Abaffy
 */
public class ActionSequenceTest {

    @Test
    public void testDataAndReset() {
        ActionSequence.reset();
        String nextSeq = "next";
        ActionSequence.addAction("1");
        ActionSequence.addAction("2");
        ActionSequence.addAction(nextSeq, "1");
        ActionSequence.addAction(nextSeq, "2");
        ActionSequence.addAction(nextSeq, "3");
        assertEquals(ActionSequence.getSequenceSize(), 2);
        assertEquals(ActionSequence.getSequenceSize(nextSeq), 3);
        assertEquals(ActionSequence.getSequenceData().get(1), "2");
        assertEquals(ActionSequence.getSequenceData(nextSeq).get(0), "1");
        ActionSequence.reset();
        assertEquals(ActionSequence.getSequenceSize(), 0);
        assertEquals(ActionSequence.getSequenceSize(nextSeq), 0);
    }

    @Test
    public void testToString() {
        ActionSequence.reset();
        ActionSequence.addAction("1");
        ActionSequence.addAction("2");
        assertEquals(ActionSequence.getSequence().toString(), "ActionSequence [name=default, data=[1, 2]]");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testUnmodifiableData() {
        ActionSequence.reset();
        ActionSequence.addAction("1");
        ActionSequence.addAction("2");
        List<String> data = ActionSequence.getSequenceData();
        data.clear();
    }

    /**
     * Very simple concurrency test - add data to the default sequence using several parallel threads. More complex test would
     * be overkill as {@link ActionSequence} is not intended to be used under heavy concurrent use.
     * 
     * @throws Exception
     */
    @Test
    public void testConcurrency() throws Exception {

        final int threads = 5;
        final int actions = 50;

        ActionSequence.reset();
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();

        for (int i = 0; i < threads; i++) {
            tasks.add(new Callable<Boolean>() {

                @Override
                public Boolean call() throws Exception {

                    boolean newSequenceAdded = false;

                    for (int i = 0; i < actions; i++) {
                        if (ActionSequence.addAction("" + System.nanoTime())) {
                            newSequenceAdded = true;
                        }
                    }
                    return newSequenceAdded;
                }
            });
        }

        List<Future<Boolean>> results = executorService.invokeAll(tasks);

        boolean sequenceAdded = false;
        for (Future<Boolean> future : results) {

            if (!future.isDone())
                fail();

            if (future.get()) {
                if (sequenceAdded) {
                    fail();
                }
                sequenceAdded = true;
            }
        }
        assertNotNull(ActionSequence.getSequence());
        assertEquals(ActionSequence.getSequenceSize(), threads * actions);
    }

    @Test
    public void testContainsAll() {
        ActionSequence.reset();
        ActionSequence.addAction("1");
        ActionSequence.addAction("2");
        ActionSequence.addAction("3");
        ActionSequence.addAction("4");
        assertTrue(ActionSequence.getSequence().containsAll("1", "3"));
        assertTrue(ActionSequence.getSequence().containsAll("4", "2"));
        assertFalse(ActionSequence.getSequence().containsAll("4", "2", "aaa"));
    }

    @Test
    public void testBeginsWith() {
        ActionSequence.reset();
        ActionSequence.addAction("1");
        ActionSequence.addAction("2");
        ActionSequence.addAction("3");
        ActionSequence.addAction("4");
        assertTrue(ActionSequence.getSequence().beginsWith("1", "2"));
        assertFalse(ActionSequence.getSequence().beginsWith("4"));
    }

    @Test
    public void testEndsWith() {
        ActionSequence.reset();
        ActionSequence.addAction("1");
        ActionSequence.addAction("2");
        ActionSequence.addAction("3");
        ActionSequence.addAction("4");
        assertTrue(ActionSequence.getSequence().endsWith("3", "4"));
        assertFalse(ActionSequence.getSequence().endsWith("2", "3"));
    }

    @Test
    public void testStringValues() {
        ActionSequence seq = null;
        try {
            seq = new ActionSequence("The best sequence");
        } catch (IllegalArgumentException expected) {
        }
        seq = new ActionSequence();
        seq.add("01254_55757.dd");
        seq.add(ActionSequence.class.getName());
        try {
            seq.add("^$loop");
        } catch (IllegalArgumentException expected) {
        }
        try {
            seq.add("My test");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void testCsv() {
        ActionSequence seq = new ActionSequence();
        seq.add("1");
        seq.add(ActionSequence.class.getName());
        seq.add("1_test");
        String csv = seq.dataToCsv();
        assertNotNull(csv);
        assertEquals(csv, "1," + ActionSequence.class.getName() + ",1_test");
        ActionSequence built = ActionSequence.buildFromCsvData(csv);
        assertEquals(built.getData().size(), 3);
        assertTrue(built.endsWith("1_test"));
    }

    @Test
    public void testAssertDataContainsAll() {
        ActionSequence.reset();
        ActionSequence seq = new ActionSequence();
        Set<String> set = new HashSet<String>();
        set.add("1");
        set.add("2");
        set.add("3");
        
        for (String s : set) {
            seq.add(s);
        }
        seq.add("4");

        seq.assertDataContainsAll(set);
        try {
            seq.assertDataContainsAll("1", "2", "5");
        } catch (Throwable expected) {
        }

        try {
            ActionSequence.assertSequenceDataContainsAll(set);
        } catch (Throwable expected) {
            // should fail because there is no default sequence
        }
    }

    @Test
    public void testAssertDataEquals() {
        ActionSequence seq = new ActionSequence();
        seq.add("1");
        seq.add("2");
        seq.add("3");
        seq.assertDataEquals(Arrays.asList("1", "2", "3"));
        try {
            seq.assertDataEquals("2", "1", "3");
        } catch (Throwable expected) {
            // should fail as the ordering is incorrect
        }
        seq.add("1");
        try {
            seq.assertDataEquals("1", "2", "3");
        } catch (Throwable expected) {
            // should fail as "1" is missing at the end of the expected
        }
    }

    @Test
    public void testAssertMethodsWithClassParameters() {
        class Foo {
        }
        
        class Bar {
        }
        
        ActionSequence.addAction("Foo");
        ActionSequence.addAction("Bar");
        ActionSequence.assertSequenceDataContainsAll(Bar.class, Foo.class, Bar.class);
        ActionSequence.assertSequenceDataEquals(Foo.class, Bar.class);
        try {
            ActionSequence.assertSequenceDataEquals(Foo.class, Bar.class, Foo.class);
        } catch (Throwable expected) {
        }
    }
}
