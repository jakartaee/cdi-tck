/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.configurators.bean;

import jakarta.enterprise.inject.Vetoed;
import jakarta.inject.Inject;

/** {@link Vetoed} makes sure this bean does not get picked up by CDI
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@Vetoed
public class Ghost {
    
    private boolean invisible;
    
    @Inject
    private Weapon weapon;
    
    @Inject
    private DesireToHurtHumans evilDesire;
    
    public Ghost(boolean invisible) {
        this.invisible = invisible;
    }
    
    public Ghost() {
        
    }
}
