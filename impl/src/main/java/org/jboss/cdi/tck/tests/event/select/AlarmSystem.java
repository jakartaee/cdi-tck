/*
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
package org.jboss.cdi.tck.tests.event.select;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;

@RequestScoped
class AlarmSystem {
    private int numSecurityEvents = 0;

    private int numSystemTests = 0;

    private int numBreakIns = 0;

    private int numViolentBreakIns = 0;

    public void securityEventOccurred(@Observes @Any SecurityEvent event) {
        numSecurityEvents++;
    }

    public void selfTest(@Observes @SystemTest SecurityEvent event) {
        numSystemTests++;
    }

    public void breakInOccurred(@Observes @Any BreakInEvent event) {
        numBreakIns++;
    }

    public void securityBreeched(@Observes @Violent BreakInEvent event) {
        numViolentBreakIns++;
    }

    public int getNumSystemTests() {
        return numSystemTests;
    }

    public int getNumSecurityEvents() {
        return numSecurityEvents;
    }

    public int getNumBreakIns() {
        return numBreakIns;
    }

    public int getNumViolentBreakIns() {
        return numViolentBreakIns;
    }

    public void reset() {
        numBreakIns = 0;
        numViolentBreakIns = 0;
        numSecurityEvents = 0;
        numSystemTests = 0;
    }
}
