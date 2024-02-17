/*
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.context.dependent.transientreference;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.TransientReference;
import jakarta.enterprise.inject.spi.BeanManager;

@RequestScoped
public class Kitchen {

    @Produces
    public Meal cook(@TransientReference Chef transientChef, Chef normalChef, BeanManager beanManager) {
        transientChef.setOwner(Util.buildOwnerId(Kitchen.class, true, Util.TYPE_PRODUCER));
        normalChef.setOwner(Util.buildOwnerId(Kitchen.class, false, Util.TYPE_PRODUCER));
        return new Meal("soup");
    }

}
