/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Martin Kouba
 */
public final class ActionSequence {

    private static final String DEFAULT_SEQUENCE = "default";

    private static Map<String, List<String>> sequences;

    static {
        sequences = new HashMap<String, List<String>>();
        sequences.put(DEFAULT_SEQUENCE, new ArrayList<String>());
    }

    private ActionSequence() {
    }

    public static void reset() {
        sequences.clear();
    }

    /**
     * 
     * @param sequence
     * @param actionId
     */
    public static void add(String sequence, String actionId) {

        if (!sequences.containsKey(sequence))
            sequences.put(sequence, new ArrayList<String>());

        sequences.get(sequence).add(actionId);
    }

    /**
     * 
     * @param actionId
     */
    public static void add(String actionId) {
        add(DEFAULT_SEQUENCE, actionId);
    }

    public static List<String> getSequence() {
        return getSequence(DEFAULT_SEQUENCE);
    }

    public static List<String> getSequence(String sequence) {
        return sequences.get(sequence);
    }

    public static int getSequenceSize() {
        return getSequence().size();
    }

    public static int getSequenceSize(String sequence) {
        return sequences.containsKey(sequence) ? sequences.get(sequence).size() : 0;
    }

}
