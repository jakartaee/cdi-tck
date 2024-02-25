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
package org.jboss.cdi.tck;

/**
 * TestNG groups used in the CDI TCK.
 *
 * The most important groups (used by TCK runner) are:
 * <ul>
 * <li>{@link #CDI_FULL} - used to exclude the superset of CDI tests that do not apply to CDI-lite implementations</li>
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
    public static final String CDI_FULL = "cdi-full";

    /**
     * Integration test - cannot run in embedded arquillian container
     */
    public static final String INTEGRATION = "integration";

    /**
     * Integration test that requires full Jakarta EE platform (EAR packaging, JAX-WS, EJB timers, ...)
     */
    public static final String JAVAEE_FULL = "javaee-full";

    /**
     * Probably needs to be rewritten
     */
    public static final String REWRITE = "rewrite";

    /**
     * Requires basic JMS configuration
     */
    public static final String JMS = "jms";

    /**
     * Requires test data source
     */
    public static final String PERSISTENCE = "persistence";

    /**
     * Requires installed library
     */
    public static final String INSTALLED_LIB = "installedLib";

    /**
     * Test contains JAX-RS web service.
     */
    public static final String JAX_RS = "jaxrs";

    /**
     * Test contains JAX-WS web service.
     */
    public static final String JAX_WS = "jaxws";

    /**
     * Tests Servlet asynchronous processing.
     */
    public static final String ASYNC_SERVLET = "asyncServlet";

    /**
     * Requires test system properties.
     *
     * @see TestSystemProperty
     */
    public static final String SYSTEM_PROPERTIES = "systemProperties";

    /**
     * Requires following mapping of roles to principals:
     * --------------------------------
     * | Principal | Group |
     * --------------------------------
     * | student | student |
     * | alarm | student, alarm |
     * | printer | printer |
     * --------------------------------
     */
    public static final String SECURITY = "security";

    /**
     * Requires manual bootstrapping of CDI SE container
     */
    public static final String SE = "se";

    /**
     * No instance
     */
    private TestGroups() {
    }

}
