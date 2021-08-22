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
package org.jboss.cdi.tck.tests.event.fires;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;

@RequestScoped
public class DoggiePoints {
    private int numPraiseReceived;

    private int numTamed;

    public void praiseReceived(@Observes @Any Praise praise) {
        numPraiseReceived++;
    }

    public void tamed(@Observes @Tame @Role("Master") TamingCommand tamed) {
        numTamed++;
    }

    public int getNumPraiseReceived() {
        return numPraiseReceived;
    }

    public int getNumTamed() {
        return numTamed;
    }

    public void reset() {
        numPraiseReceived = 0;
        numTamed = 0;
    }
}
