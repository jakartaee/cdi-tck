/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.impl;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.spi.CreationalContext;

public class MockCreationalContext<T> implements CreationalContext<T> {
    private static List<Object> beansPushed = new ArrayList<Object>();
    private static Object lastBeanPushed = null;
    private static boolean pushCalled = false;
    private static boolean releaseCalled = false;

    public void push(T incompleteInstance) {
        pushCalled = true;
        lastBeanPushed = incompleteInstance;
        beansPushed.add(incompleteInstance);
    }

    public static Object getLastBeanPushed() {
        return lastBeanPushed;
    }

    public static List<Object> getBeansPushed() {
        return beansPushed;
    }

    public static void setLastBeanPushed(Object lastBeanPushed) {
        MockCreationalContext.lastBeanPushed = lastBeanPushed;
    }

    public static boolean isPushCalled() {
        return pushCalled;
    }

    public static void setPushCalled(boolean pushCalled) {
        MockCreationalContext.pushCalled = pushCalled;
    }

    public static boolean isReleaseCalled() {
        return releaseCalled;
    }

    public static void setReleaseCalled(boolean releaseCalled) {
        MockCreationalContext.releaseCalled = releaseCalled;
    }

    public static void reset() {
        lastBeanPushed = null;
        beansPushed.clear();
        pushCalled = false;
        releaseCalled = false;
    }

    public void release() {
        releaseCalled = true;
    }

}
