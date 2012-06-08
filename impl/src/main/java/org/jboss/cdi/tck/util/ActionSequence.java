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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple data holder for sequence of actions identified with {@link String}.
 * 
 * @author Martin Kouba
 */
public final class ActionSequence {

    private static final String DEFAULT_SEQUENCE = "default";

    /**
     * Static sequences holder
     */
    private static Map<String, ActionSequence> sequences = null;

    private List<String> data = Collections.synchronizedList(new ArrayList<String>());

    public ActionSequence add(String actionId) {
        this.data.add(actionId);
        return this;
    }

    public List<String> getData() {
        return data;
    }

    /**
     * Remove all sequences.
     */
    public static void reset() {
        if (sequences != null)
            sequences.clear();
    }

    /**
     * Add actionId to specified sequence. Add new sequence if needed.
     * 
     * @param sequence
     * @param actionId
     */
    public static void addAction(String sequence, String actionId) {

        if (sequences == null)
            sequences = new HashMap<String, ActionSequence>();

        if (!sequences.containsKey(sequence))
            sequences.put(sequence, new ActionSequence());

        sequences.get(sequence).add(actionId);
    }

    /**
     * Add actionId to default sequence.
     * 
     * @param actionId
     */
    public static void addAction(String actionId) {
        addAction(DEFAULT_SEQUENCE, actionId);
    }

    /**
     * @return default sequence
     */
    public static ActionSequence getSequence() {
        return getSequence(DEFAULT_SEQUENCE);
    }

    /**
     * @param name
     * @return specified sequence
     */
    public static ActionSequence getSequence(String name) {
        return sequences.get(name);
    }

    /**
     * @return data of default sequence
     */
    public static List<String> getSequenceData() {
        return getSequenceData(DEFAULT_SEQUENCE);
    }

    /**
     * @param sequence
     * @return data of specified sequence
     */
    public static List<String> getSequenceData(String sequence) {
        return sequences.containsKey(sequence) ? sequences.get(sequence).getData() : null;
    }

    /**
     * @return size of default sequence
     */
    public static int getSequenceSize() {
        return getSequenceSize(DEFAULT_SEQUENCE);
    }

    /**
     * @param sequence
     * @return size of specified sequence
     */
    public static int getSequenceSize(String sequence) {
        return sequences.containsKey(sequence) ? sequences.get(sequence).getData().size() : 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ActionSequence other = (ActionSequence) obj;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ActionSequence [");
        if (data != null) {
            builder.append("data=");
            builder.append(data);
        }
        builder.append("]");
        return builder.toString();
    }

}
