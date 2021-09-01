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

package org.jboss.cdi.tck.tests.lookup.injectionpoint.requiredtype;

import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * @author Martin Kouba
 */
public class Forest {

    @Inject
    Tree tree;

    @Inject
    Conifer conifer;

    @Inject
    Spruce spruce;

    @Inject
    @Named("age")
    int age;

    @Inject
    Needle[] needles;

    @Inject
    Leaf<?> leaf;

    @Inject
    Leaf<Spruce> spruceLeaf;

    public void ping() {
        tree.ping();
        conifer.ping();
        spruce.ping();
        leaf.ping();
        spruceLeaf.ping();
    }

    public int getAge() {
        return age;
    }

    public Needle[] getNeedles() {
        return needles;
    }

}
