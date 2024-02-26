/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.context.dependent.instance;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Dependent;

import org.jboss.cdi.tck.util.SimpleLogger;

@Dependent
public class Foo {

    private static final SimpleLogger logger = new SimpleLogger(Foo.class);

    public static boolean created = false;
    public static boolean destroyed = false;

    public void ping() {
        logger.log("Ping");
    }

    @PostConstruct
    public void create() {
        created = true;
    }

    @PreDestroy
    public void destroy() {
        destroyed = true;
    }

    public static void reset() {
        created = false;
        destroyed = false;
    }

}
