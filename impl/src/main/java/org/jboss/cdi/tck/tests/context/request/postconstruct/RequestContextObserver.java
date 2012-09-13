/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.context.request.postconstruct;

import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;

import org.jboss.cdi.tck.util.SimpleLogger;

/**
 * @author Martin Kouba
 * 
 */
@ApplicationScoped
public class RequestContextObserver {

    private SimpleLogger logger = new SimpleLogger(RequestContextObserver.class);

    private AtomicInteger initializations = new AtomicInteger();
    private AtomicInteger destructions = new AtomicInteger();

    public void observerRequestContextInitialized(@Observes @Initialized(RequestScoped.class) ServletRequestEvent event) {
        log("Initialized", event);
        initializations.incrementAndGet();
    }

    public void observerRequestContextDestroyed(@Observes @Destroyed(RequestScoped.class) ServletRequestEvent event) {
        log("Destroyed", event);
        destructions.incrementAndGet();
    }

    public AtomicInteger getInitializations() {
        return initializations;
    }

    public AtomicInteger getDestructions() {
        return destructions;
    }

    private void log(String action, ServletRequestEvent event) {
        HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
        logger.log(action + ": " + request.getRequestURI() + "/" + request.getQueryString());
    }

}
