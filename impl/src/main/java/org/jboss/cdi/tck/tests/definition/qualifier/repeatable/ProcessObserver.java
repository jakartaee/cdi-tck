/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.definition.qualifier.repeatable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.EventMetadata;

@ApplicationScoped
public class ProcessObserver {

    private int processAObserved = 0;
    private int processBObserved = 0;
    private int processABObserved = 0;

    private EventMetadata processAMetadata;
    private EventMetadata processBCMetadata;

    public void observeProcessA(@Observes @Start("A") Process process, EventMetadata metadata) {
        processAObserved++;
        processAMetadata = metadata;
    }

    public void observeProcessB(@Observes @Start("B") Process process, EventMetadata metadata) {
        processBObserved++;
    }

    public void observeProcessAB(@Observes @Start("B") @Start("C") Process process, EventMetadata metadata) {
        processABObserved++;
        processBCMetadata = metadata;

    }

    public int getProcessABObserved() {
        return processABObserved;
    }

    public int getProcessAObserved() {
        return processAObserved;
    }

    public int getProcessBObserved() {
        return processBObserved;
    }

    public EventMetadata getProcessAMetadata() {
        return processAMetadata;
    }

    public EventMetadata getProcessBCMetadata() {
        return processBCMetadata;
    }
}
