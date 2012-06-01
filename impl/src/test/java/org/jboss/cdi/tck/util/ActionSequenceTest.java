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

package org.jboss.cdi.tck.util;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
public class ActionSequenceTest {

    @Test
    public void testDataAndSize() {
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
    }

    @Test
    public void testReset() {
        String nextSeq = "next";
        ActionSequence.addAction("1");
        ActionSequence.addAction("2");
        ActionSequence.addAction(nextSeq, "1");
        ActionSequence.addAction(nextSeq, "2");
        ActionSequence.addAction(nextSeq, "3");
        ActionSequence.reset();
        assertEquals(ActionSequence.getSequenceSize(), 0);
        assertEquals(ActionSequence.getSequenceSize(nextSeq), 0);
    }

}
