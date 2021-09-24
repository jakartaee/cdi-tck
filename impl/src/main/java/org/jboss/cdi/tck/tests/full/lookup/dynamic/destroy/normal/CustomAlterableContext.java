/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.lookup.dynamic.destroy.normal;

import jakarta.enterprise.context.spi.AlterableContext;
import jakarta.enterprise.context.spi.Contextual;

import java.lang.annotation.Annotation;

public class CustomAlterableContext extends AbstractContext implements AlterableContext {

    private static boolean destroyCalled;

    @SuppressWarnings("unchecked")
    public void destroy(Contextual<?> contextual) {
    	destroyCalled = true;
        Instance instance = storage.remove(contextual);
        if (instance != null) {
            @SuppressWarnings("rawtypes")
            Contextual rawContextual = contextual;
            rawContextual.destroy(instance.getInstance(), instance.getCtx());
        }
    }

    public static void reset() {
        destroyCalled = false;
    }

    public static boolean isDestroyCalled() {
        return destroyCalled;
    }

	@Override
	public Class<? extends Annotation> getScope() {
		return AlterableScoped.class;
	}
}
