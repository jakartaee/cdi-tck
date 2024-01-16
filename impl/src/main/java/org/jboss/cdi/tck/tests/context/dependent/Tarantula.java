/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.context.dependent;

import jakarta.annotation.PreDestroy;

import jakarta.enterprise.context.Dependent;
import org.jboss.cdi.tck.impl.ConfigurationFactory;

@Dependent
public class Tarantula extends Spider implements DeadlySpider {

    private static boolean destroyed;

    private static boolean dependentContextActive = false;

    private Integer producerInstanceHashcode = null;

    public Tarantula() {
        dependentContextActive = ConfigurationFactory.get().getContexts().getDependentContext().isActive();
    }

    public Tarantula(Integer producerInstanceHashcode) {
        dependentContextActive = ConfigurationFactory.get().getContexts().getDependentContext().isActive();
        this.producerInstanceHashcode = producerInstanceHashcode;
    }

    @PreDestroy
    public void preDestroy() {
        destroyed = true;
    }

    public Integer getProducerInstanceHashcode() {
        return producerInstanceHashcode;
    }

    public static boolean isDependentContextActive() {
        return dependentContextActive;
    }

    public static boolean isDestroyed() {
        return destroyed;
    }

    public static void reset() {
        destroyed = false;
        dependentContextActive = false;
    }

}
