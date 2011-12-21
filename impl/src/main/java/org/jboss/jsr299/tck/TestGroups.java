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
package org.jboss.jsr299.tck;

/**
 * TestNG groups used in CDI TCK. Most of them are useless because of not used consistently.
 * 
 * The most important groups (used by TCK runner) are:
 * <ul>
 * <li>{@link #INTEGRATION} - used to exclude integration tests when running standalone container</li>
 * <li>{@link #JAVAEE_FULL} - subset of integration tests used to exclude tests that require full Java EE platform; tests that
 * belong to this group are also considered to be integration tests</li>
 * </ul>
 * 
 * @author Martin Kouba
 */
public final class TestGroups {

    /**
     * Integration test - cannot run in embedded arquillian container
     */
    public static final String INTEGRATION = "integration";

    /**
     * Integration test that requires full Java EE platform (EAR packaging, JAX-WS, EJB timers, ...)
     */
    public static final String JAVAEE_FULL = "javaee-full";

    /**
     * Contexts related test
     */
    public static final String CONTEXTS = "contexts";

    public static final String PASSIVATION = "passivation";

    public static final String LIFECYCLE = "lifecycle";

    public static final String ALTERNATIVES = "alternatives";

    /**
     * Probably needs to be rewritten
     */
    public static final String REWRITE = "rewrite";

    public static final String RESOLUTION = "resolution";

    public static final String POLICY = "policy";

    public static final String EL = "el";

    public static final String EVENTS = "events";

    public static final String INHERITANCE = "inheritance";

    public static final String NEW = "new";

    public static final String DISPOSAL = "disposal";

    public static final String INITIALIZER_METHOD = "initializerMethod";

    public static final String SPECIALIZATION = "specialization";

    public static final String MANAGER = "manager";

    public static final String SERVLET = "servlet";

    public static final String INJECTION = "injection";

    public static final String INJECTION_POINT = "injectionPoint";

    public static final String PRODUCER_METHOD = "producerMethod";

    public static final String PRODUCER_FIELD = "producerField";

    public static final String OBSERVER_METHOD = "observerMethod";

    public static final String ANNOTATION_DEFINITION = "annotationDefinition";

    public static final String INNER_CLASS = "innerClass";

    /**
     * Requires basic JMS configuration
     */
    public static final String JMS = "innerClass";

    /**
     * No instance
     */
    private TestGroups() {
    }

}
