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
package org.jboss.cdi.tck.tests.interceptors.definition.inheritance;

import java.util.ArrayList;
import java.util.List;

public abstract class Plant implements Ping {

    private static List<String> inspections = new ArrayList<String>();

    public static void clearInspections() {
        inspections.clear();
    }

    public static void inspect(String id) {
        inspections.add(id);
    }

    public static boolean inspectedBy(String id) {
        return inspections.contains(id);
    }

}
