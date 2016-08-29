/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.configurators.invalid;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionPoint;

import org.jboss.cdi.tck.util.ForwardingInjectionPoint;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class PIPExtension implements Extension {

    public static boolean ISE_CAUGHT = false;

    void observePIP(@Observes ProcessInjectionPoint<Warehouse, Box> event) {
        event.configureInjectionPoint().addQualifier(Any.Literal.INSTANCE);

        final InjectionPoint delegate = event.getInjectionPoint();
        try {
            event.setInjectionPoint(new ForwardingInjectionPoint() {

                @Override
                protected InjectionPoint delegate() {
                    return delegate;
                }

                @Override
                public boolean isTransient() {
                    return true;
                }

                @Override
                public Type getType() {
                    return Box.class;
                }

                @Override
                public Set<Annotation> getQualifiers() {
                    return Collections.<Annotation>emptySet();
                }
            });
        } catch (IllegalStateException e) {
            // expected should blow up with ISE
            ISE_CAUGHT = true;
        }
    }
}
