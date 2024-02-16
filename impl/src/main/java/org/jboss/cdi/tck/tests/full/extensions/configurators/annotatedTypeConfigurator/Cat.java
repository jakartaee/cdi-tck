/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.configurators.annotatedTypeConfigurator;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@RequestScoped
public class Cat {

    private boolean feedObserved = false;
    private String name;

    @Inject
    Feed feed;

    public Cat() {

    }

    public Cat(String name) {
        this.name = name;
    }

    @Inject
    public Cat(@Cats Feed feed) {

    }

    public Feed getFeed() {
        return feed;
    }

    public boolean isFeedObserved() {
        return feedObserved;
    }

    @Produces
    @Cats
    public Feed produceCatFeed() {
        return new Feed();
    }

    public void observesCatsFeed(@Observes Feed feed) {
        feedObserved = true;
    }

    public String getName() {
        return name;
    }

}
