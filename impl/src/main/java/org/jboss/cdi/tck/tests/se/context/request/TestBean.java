/*
 * Copyright 2017, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.se.context.request;

import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

@ApplicationScoped
public class TestBean {

    @Inject
    BeanManager bm;

    private AtomicBoolean isRequestContextActiveDuringPostConstruct = new AtomicBoolean(false);

    @PostConstruct
    public void init() {
        try {
            isRequestContextActiveDuringPostConstruct.set(bm.getContext(RequestScoped.class).isActive());
        } catch (ContextNotActiveException e) {
            isRequestContextActiveDuringPostConstruct.set(false);
        }
    }

    public boolean isReqContextActiveDuringPostConstruct() {
        return isRequestContextActiveDuringPostConstruct.get();
    }

    public void fail() {
        bm.getContext(RequestScoped.class).isActive();
    }
}
