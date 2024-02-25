/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.implementation.producer.field.lifecycle;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;

@Dependent
public class BlackWidowProducer {

    public static boolean blackWidowDestroyed = false;

    public static long destroyedBlackWidowTimeOfBirth = 0l;

    public static BlackWidow blackWidow = new BlackWidow(System.currentTimeMillis());

    @Produces
    @Tame
    public BlackWidow produceBlackWidow = blackWidow;

    public void destoryTarantula(@Disposes @Tame BlackWidow blackWidow) {
        blackWidowDestroyed = true;
        destroyedBlackWidowTimeOfBirth = blackWidow.getTimeOfBirth();
    }

    public static void reset() {
        blackWidowDestroyed = false;
        destroyedBlackWidowTimeOfBirth = 0l;
    }

}
