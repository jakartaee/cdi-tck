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
package org.jboss.cdi.tck.tests.event.fires;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;

@RequestScoped
class Housekeeping {
    private Set<Item> itemsTainted = new HashSet<Item>();

    private Set<Item> itemsMissing = new HashSet<Item>();

    private List<Item> itemActivity = new ArrayList<Item>();

    public void onItemRemoved(@Observes @Lifted Item item) {
        itemsMissing.add(item);
        itemsTainted.remove(item);
    }

    public void onItemRestored(@Observes @Restored Item item) {
        itemsMissing.remove(item);
        itemsTainted.add(item);
    }

    public void onItemActivity(@Observes @Any Item item) {
        itemActivity.add(item);
    }

    public Set<Item> getItemsTainted() {
        return itemsTainted;
    }

    public Set<Item> getItemsMissing() {
        return itemsMissing;
    }

    public List<Item> getItemActivity() {
        return itemActivity;
    }

    public void minibarStocked(@Observes @Any MiniBar minibar) {
        reset();
    }

    public void reset() {
        itemActivity.clear();
        itemsMissing.clear();
        itemsTainted.clear();
    }
}
