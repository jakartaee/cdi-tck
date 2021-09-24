/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.context.dependent.transientreference;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.TransientReference;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

@Dependent
public class Spoon {

    @Inject
    public Spoon(BeanManager beanManager, @TransientReference Chef transientChef, Chef normalChef) {
        transientChef.setOwner(Util.buildOwnerId(Spoon.class, true, Util.TYPE_CONSTRUCTOR));
        normalChef.setOwner(Util.buildOwnerId(Spoon.class, false, Util.TYPE_CONSTRUCTOR));
    }

    @Inject
    public void initFoos(@TransientReference Chef transientChef, Chef normalChef, BeanManager beanManager) {
        transientChef.setOwner(Util.buildOwnerId(Spoon.class, true, Util.TYPE_INIT));
        normalChef.setOwner(Util.buildOwnerId(Spoon.class, false, Util.TYPE_INIT));
    }

    public void ping() {
    }

}
