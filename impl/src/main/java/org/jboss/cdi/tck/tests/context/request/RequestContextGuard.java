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

package org.jboss.cdi.tck.tests.context.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.servlet.ServletRequestEvent;

/**
 * @author Martin Kouba
 */
@ApplicationScoped
public class RequestContextGuard {

    private long servletRequestListenerCheckpoint = 0l;

    private long filterCheckpoint = 0l;

    private long servletCheckpoint = 0l;

    private long requestContextDestroyedCheckpoint = 0l;

    public void observeRequestDestroyed(@Observes @Destroyed(RequestScoped.class) ServletRequestEvent event) {
        requestContextDestroyedCheckpoint = System.currentTimeMillis();
    }

    public long getServletRequestListenerCheckpoint() {
        return servletRequestListenerCheckpoint;
    }

    public void setServletRequestListenerCheckpoint(long servletRequestListenerCheckpoint) {
        this.servletRequestListenerCheckpoint = servletRequestListenerCheckpoint;
    }

    public long getFilterCheckpoint() {
        return filterCheckpoint;
    }

    public void setFilterCheckpoint(long filterCheckpoint) {
        this.filterCheckpoint = filterCheckpoint;
    }

    public long getServletCheckpoint() {
        return servletCheckpoint;
    }

    public void setServletCheckpoint(long servletCheckpoint) {
        this.servletCheckpoint = servletCheckpoint;
    }

    public long getRequestContextDestroyedCheckpoint() {
        return requestContextDestroyedCheckpoint;
    }

    public void setRequestContextDestroyedAt(long requestContextDestroyedAt) {
        this.requestContextDestroyedCheckpoint = requestContextDestroyedAt;
    }

    public boolean isCheckpointSequenceOk() {

        if (servletCheckpoint <= 0l || filterCheckpoint <= 0l || servletRequestListenerCheckpoint <= 0l
                || requestContextDestroyedCheckpoint <= 0l)
            return false;

        List<Long> sequence = new ArrayList<Long>();
        sequence.add(servletCheckpoint);
        sequence.add(filterCheckpoint);
        sequence.add(servletRequestListenerCheckpoint);
        sequence.add(requestContextDestroyedCheckpoint);

        List<Long> ascSequence = new ArrayList<Long>();
        ascSequence.addAll(sequence);
        // Sort asc
        Collections.sort(ascSequence);
        // See java.util.List.equals(Object)
        return sequence.equals(ascSequence);
    }

}
