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
package org.jboss.cdi.tck.tests.lookup.typesafe.resolution.primitive;

import jakarta.inject.Inject;

public class Game {

    @Inject
    @ProducedPrimitive
    private byte injectedByte;

    @Inject
    @ProducedPrimitive
    private short injectedShort;

    private int injectedInt;

    @Inject
    @ProducedPrimitive
    private long injectedLong;

    @Inject
    @ProducedPrimitive
    private float injectedFloat;

    private double injectedDouble;

    @Inject
    @ProducedPrimitive
    private char injectedChar;

    private boolean injectedBoolean;

    @Inject
    public Game(@ProducedPrimitive boolean booleanValue, @ProducedPrimitive double doubleValue) {
        this.injectedBoolean = booleanValue;
        this.injectedDouble = doubleValue;
    }

    public byte getInjectedByte() {
        return injectedByte;
    }

    public short getInjectedShort() {
        return injectedShort;
    }

    public int getInjectedInt() {
        return injectedInt;
    }

    @Inject
    public void setInjectedInt(@ProducedPrimitive int intValue) {
        this.injectedInt = intValue;
    }

    public long getInjectedLong() {
        return injectedLong;
    }

    public float getInjectedFloat() {
        return injectedFloat;
    }

    public double getInjectedDouble() {
        return injectedDouble;
    }

    public char getInjectedChar() {
        return injectedChar;
    }

    public boolean isInjectedBoolean() {
        return injectedBoolean;
    }

}
