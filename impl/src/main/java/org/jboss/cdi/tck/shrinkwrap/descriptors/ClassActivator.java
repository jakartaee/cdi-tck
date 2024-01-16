/*
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
package org.jboss.cdi.tck.shrinkwrap.descriptors;

public class ClassActivator implements Activator {

    private String name;

    private boolean isAvailable = true;

    public ClassActivator(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ClassActivator setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public ClassActivator setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
        return this;
    }

    @Override
    public String getElementName() {
        return isAvailable ? "if-class-available":"if-class-not-available";
    }

    @Override
    public String getNameAttribute() {
        return getName();
    }

    @Override
    public String getValueAttribute() {
        return null;
    }

    public static ClassActivator newClassAvailableActivator(String name) {
        return new ClassActivator(name);
    }

    public static ClassActivator newClassNotAvailableActivator(String name) {
        return new ClassActivator(name).setAvailable(false);
    }

}
