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

package org.jboss.cdi.tck.tests.full.extensions.beanManager.el;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Named;

import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.cdi.tck.util.SimpleLogger;

/**
 * @author Martin Kouba
 *
 */
@Named
public class Foo {

    private SimpleLogger logger = new SimpleLogger(Foo.class);

    public Boolean getValue() {
        return Boolean.TRUE;
    }

    @PostConstruct
    public void init() {
        logger.log("Create");
        ActionSequence.addAction("create", Foo.class.getName());
    }

    @PreDestroy
    public void destroy() {
        logger.log("Destroy");
        ActionSequence.addAction("destroy", Foo.class.getName());
    }

}
