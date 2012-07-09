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

    /**
     * Name of sequence.
     */
    private String name;

    /**
     * Data - list of actions
     */
    private List<String> data = Collections.synchronizedList(new ArrayList<String>());

    public ActionSequence() {
        super();
        this.name = DEFAULT_SEQUENCE;
    }

    public ActionSequence(String name) {
        super();
        this.name = name;
    }

    /**
     * @param actionId
     * @return data holder
     */
    public ActionSequence add(String actionId) {
        this.data.add(actionId);
        return this;
    }

    /**
     * @return read-only view of sequence data
     */
    public List<String> getData() {
        return Collections.unmodifiableList(this.data);
    }

    /**
     * @return name of sequence
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("ActionSequence [name=%s, data=%s]", name, data);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    // Static members

    private static final String DEFAULT_SEQUENCE = "default";

    /**
     * Static sequence map
     */
    private static Map<String, ActionSequence> sequences = new HashMap<String, ActionSequence>();

    /**
     * Remove all sequences.
     */
    public static void reset() {
        synchronized (sequences) {
            sequences.clear();
        }
    }

    /**
     * Add actionId to specified sequence. Add new sequence if needed.
     * 
     * @param sequence
     * @param actionId
     * @return <code>true</code> if a new sequence was added, <code>false</code> otherwise
     */
    public static boolean addAction(String sequenceName, String actionId) {

        boolean newSequenceAdded = false;

        synchronized (sequences) {

            if (!sequences.containsKey(sequenceName)) {
                sequences.put(sequenceName, new ActionSequence(sequenceName));
                newSequenceAdded = true;
            }
            sequences.get(sequenceName).add(actionId);
        }
        return newSequenceAdded;
    }

    /**
     * Add actionId to default sequence.
     * 
     * @param actionId
     * @return <code>true</code> if a new sequence was added, <code>false</code> otherwise
     */
    public static boolean addAction(String actionId) {
        return addAction(DEFAULT_SEQUENCE, actionId);
    }

    /**
     * @return default sequence or <code>null</code> if no such sequence exists
     */
    public static ActionSequence getSequence() {
        return getSequence(DEFAULT_SEQUENCE);
    }

    /**
     * @param name
     * @return specified sequence or <code>null</code> if no such sequence exists
     */
    public static ActionSequence getSequence(String sequenceName) {
        synchronized (sequences) {
            return sequences.get(sequenceName);
        }
    }

    /**
     * @return data of default sequence or <code>null</code> if no such sequence exists
     */
    public static List<String> getSequenceData() {
        return getSequenceData(DEFAULT_SEQUENCE);
    }

    /**
     * @param sequenceName
     * @return data of specified sequence or <code>null</code> if no such sequence exists
     */
    public static List<String> getSequenceData(String sequenceName) {
        synchronized (sequences) {
            return sequences.containsKey(sequenceName) ? sequences.get(sequenceName).getData() : null;
        }
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
    public static int getSequenceSize(String sequenceName) {
        synchronized (sequences) {
            return sequences.containsKey(sequenceName) ? sequences.get(sequenceName).getData().size() : 0;
        }
    }

}
