/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.full.decorators.builtin.instance;

import java.io.Serializable;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@Decorator
@SuppressWarnings("serial")
public abstract class MuleInstanceDecorator implements Instance<Mule>, Serializable {

    @Inject
    @Delegate
    Instance<Mule> instance;

    @Override
    public boolean isAmbiguous() {
        return true;
    }

}
