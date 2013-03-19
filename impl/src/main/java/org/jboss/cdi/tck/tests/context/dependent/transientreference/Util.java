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
package org.jboss.cdi.tck.tests.context.dependent.transientreference;

public final class Util {

    public static final String TYPE_CONSTRUCTOR = "constructor";

    public static final String TYPE_INIT = "init";

    public static final String TYPE_PRODUCER = "producer";

    private Util() {
    }

    public static String buildOwnerId(Class<?> clazz, boolean isTransientReference, String type) {
        return String.format("%s_%s_%s", clazz.getName(), (isTransientReference ? "normal" : "transient"), type);
    }

}
