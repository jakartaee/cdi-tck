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

package org.jboss.cdi.tck.util;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A very simple JDK logger wrapper. It should be used in complex tests only as logging in integration tests is not a good idea.
 *
 * @author Martin Kouba
 */
public final class SimpleLogger implements Serializable {

    private static final long serialVersionUID = -2461635783455781420L;
    private final Logger logger;

    public SimpleLogger(String name) {
        this.logger = Logger.getLogger(name);
    }

    public SimpleLogger(Class<?> clazz) {
        this.logger = Logger.getLogger(clazz.getName());
    }

    /**
     * Log message with specified parameters. {@link Level#FINE} is always used since the message is considered to be tracing
     * information.
     *
     * @param message - log message
     * @param parameters - message parameters
     */
    public void log(String message, Object... parameters) {
        logger.log(Level.FINE, message, parameters);
    }

    /**
     * Log message with specified throwed exception. {@link Level#FINE} is always used since the message is considered to be
     * tracing information.
     *
     * @param thrown - exception to log
     */
    public void log(Throwable thrown) {
        logger.log(Level.FINE, thrown.getMessage(), thrown);
    }
}
