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
package org.jboss.cdi.tck.tests.decorators.definition.lifecycle;

import java.util.ArrayList;
import java.util.List;

public class CallStore {

    private static List<String> postConstructCallers = new ArrayList<String>();

    private static List<String> preDestroyCallers = new ArrayList<String>();

    private CallStore() {
    }

    public static void resetCallers() {
        postConstructCallers = new ArrayList<String>();
        preDestroyCallers = new ArrayList<String>();
    }

    public static void addPostConstructCaller(String caller) {
        postConstructCallers.add(caller);
    }

    public static void addPreDestroyCaller(String caller) {
        preDestroyCallers.add(caller);
    }

    public static List<String> getPostConstructCallers() {
        return postConstructCallers;
    }

    public static List<String> getPreDestroyCallers() {
        return preDestroyCallers;
    }

}
