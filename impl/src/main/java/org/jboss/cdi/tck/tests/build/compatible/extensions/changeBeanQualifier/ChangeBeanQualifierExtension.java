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
package org.jboss.cdi.tck.tests.build.compatible.extensions.changeBeanQualifier;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.FieldConfig;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;

public class ChangeBeanQualifierExtension implements BuildCompatibleExtension {
    @Discovery
    public void discovery(ScannedClasses scan) {
        scan.add(MyServiceFoo.class.getName());
        scan.add(MyServiceBar.class.getName());
        scan.add(MyServiceBaz.class.getName());
    }

    @Enhancement(types = MyServiceFoo.class)
    public void foo(ClassConfig clazz) {
        clazz.removeAnnotation(ann -> ann.name().equals(MyQualifier.class.getName()));
    }

    @Enhancement(types = MyServiceBar.class)
    public void bar(ClassConfig clazz) {
        clazz.addAnnotation(MyQualifier.class);
    }

    @Enhancement(types = MyOtherService.class)
    public void service(FieldConfig field) {
        if ("myService".equals(field.info().name())) {
            field.addAnnotation(MyQualifier.class);
        }
    }
}
