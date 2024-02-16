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

package org.jboss.cdi.tck.tests.full.context.passivating.dependency.builtin;

import java.io.Serializable;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@SessionScoped
public class Worker implements Serializable {

    private static final long serialVersionUID = 1830634635545684272L;

    private String id;

    @Inject
    Instance<Hammer> instance;

    @PostConstruct
    public void init() {
        this.id = UUID.randomUUID().toString();
    }

    public Instance<Hammer> getInstance() {
        return instance;
    }

    public String getId() {
        return id;
    }

}
