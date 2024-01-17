/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.build.compatible.extensions.inspectAnnotatedSubtypes;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.Messages;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;
import jakarta.enterprise.inject.build.compatible.spi.Validation;

import java.util.HashSet;
import java.util.Set;

public class InspectAnnotatedSubtypesExtension implements BuildCompatibleExtension {
    private final Set<String> seenAnnotatedSubtypes = new HashSet<>();

    @Discovery
    public void discovery(ScannedClasses scan) {
        scan.add(MyServiceBaz.class.getName());
    }

    @Enhancement(types = MyService.class, withSubtypes = true, withAnnotations = MyAnnotation.class)
    public void enhancement(ClassConfig clazz) {
        seenAnnotatedSubtypes.add(clazz.info().simpleName());
    }

    @Validation
    public void validation(Messages msg) {
        if (seenAnnotatedSubtypes.size() != 2) {
            msg.error("Must have seen 2 subtypes of MyService with MyAnnotation");
        }

        if (!seenAnnotatedSubtypes.contains(MyServiceFoo.class.getSimpleName())) {
            msg.error("Must have seen MyServiceFoo");
        }

        if (seenAnnotatedSubtypes.contains(MyServiceBar.class.getSimpleName())) {
            msg.error("Must NOT have seen MyServiceBar");
        }

        if (!seenAnnotatedSubtypes.contains(MyServiceBaz.class.getSimpleName())) {
            msg.error("Must have seen MyServiceBaz");
        }
    }
}
