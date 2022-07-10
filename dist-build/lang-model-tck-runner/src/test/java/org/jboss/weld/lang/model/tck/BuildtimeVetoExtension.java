/*
 * Copyright 2022, Red Hat, Inc., and individual contributors
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
package org.jboss.weld.lang.model.tck;

import jakarta.annotation.Priority;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;

/**
 * Vetos all classes for the build-time Lang Model TCK extension to avoid deployment issues with unaccessible package private members.
 * @author Ondro Mihalyi
 */
public class BuildtimeVetoExtension implements Extension {

    public void enhancement(@Priority(Integer.MAX_VALUE) @Observes ProcessAnnotatedType<?> pat) {
        if (pat.getAnnotatedType().getJavaClass().getPackageName().startsWith("org.jboss.cdi.lang.model.tck")) {
            pat.veto();
        }
    }
}