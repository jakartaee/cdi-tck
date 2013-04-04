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
package org.jboss.cdi.tck.tests.event.metadata.injectionpoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.EventMetadata;
import javax.enterprise.inject.spi.InjectionPoint;

@RequestScoped
public class InfoObserver {

    private Bean<?> lastBean = null;
    private boolean lastIsTransient = false;
    private Type lastType = null;
    private Set<Annotation> lastQualifiers = null;
    private Member lastMember = null;
    private Annotated lastAnnotated = null;

    public void observeInfo(@Observes @Any Info info, EventMetadata metadata) {
        InjectionPoint injectionPoint = metadata.getInjectionPoint();
        lastBean = injectionPoint.getBean();
        lastIsTransient = injectionPoint.isTransient();
        lastType = injectionPoint.getType();
        lastQualifiers = injectionPoint.getQualifiers();
        lastMember = injectionPoint.getMember();
        lastAnnotated = injectionPoint.getAnnotated();
    }

    public Bean<?> getLastBean() {
        return lastBean;
    }

    public boolean isLastIsTransient() {
        return lastIsTransient;
    }

    public Type getLastType() {
        return lastType;
    }

    public Set<Annotation> getLastQualifiers() {
        return lastQualifiers;
    }

    public Member getLastMember() {
        return lastMember;
    }

    public Annotated getLastAnnotated() {
        return lastAnnotated;
    }

    public void reset() {
        lastBean = null;
        lastIsTransient = false;
        lastType = null;
        lastQualifiers = null;
        lastMember = null;
        lastAnnotated = null;
    }

}
