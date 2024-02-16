/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.build.compatible.extensions.invalid;

import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.InterceptorInfo;
import jakarta.enterprise.inject.build.compatible.spi.Registration;

public class RegistrationMultipleParamsExtension implements BuildCompatibleExtension {

    @Registration(types = {SomeBean.class})
    public void register(BeanInfo bi, InterceptorInfo ii) {
        // no-op, deployment should fail
    }
}
