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
package org.jboss.cdi.tck.interceptors.tests.contract.invocationContext;

import jakarta.enterprise.context.Dependent;

@SimplePCBinding
@Dependent
class SimpleBean {
    private int id = 0;
    private static boolean echoCalled = false;

    @Binding1
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Binding2
    public boolean testGetTimer() {
        return false;
    }

    @Binding3
    public boolean testGetMethod() {
        return false;
    }

    @Binding4
    public int add(int i, int j) {
        return i + j;
    }

    @Binding5
    public int add2(int i, int j) {
        return i + j;
    }

    @Binding6
    public int add3(int i, int j) {
        return i + j;
    }

    @Binding7
    public void voidMethod() {
    }

    @SimpleBinding
    public void foo() {
    }

    @Binding10
    public String echo(String s) {
        echoCalled = true;
        return s;
    }

    public static boolean isEchoCalled() {
        return echoCalled;
    }
}
