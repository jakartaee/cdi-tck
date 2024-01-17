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
package org.jboss.cdi.tck.tests.build.compatible.extensions.customInterceptorBinding;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.MetaAnnotations;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;
import jakarta.enterprise.util.Nonbinding;

public class CustomInterceptorBindingExtension implements BuildCompatibleExtension {
    @Discovery
    public void discovery(MetaAnnotations meta) {
        ClassConfig cfg = meta.addInterceptorBinding(MyCustomInterceptorBinding.class);
        cfg.methods()
                .stream()
                .filter(it -> "value".equals(it.info().name()))
                .forEach(it -> it.addAnnotation(Nonbinding.class));
    }

    @Enhancement(types = MyCustomInterceptor.class)
    public void interceptorPriority(ClassConfig clazz) {
        clazz.addAnnotation(AnnotationBuilder.of(Priority.class).value(1).build());
    }
}
