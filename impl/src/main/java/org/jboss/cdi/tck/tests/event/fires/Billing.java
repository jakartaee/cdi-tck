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

import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;

@RequestScoped
public class Billing {

    private boolean active = false;

    private int charge = 0;

    private int miniBarValue = 0;

    private Set<Item> itemsPurchased = new HashSet<Item>();

    public void billForItem(@Observes @Lifted Item item) {
        if (itemsPurchased.add(item)) {
            charge += item.getPrice();
        }
    }

    public int getCharge() {
        return charge;
    }

    public int getMiniBarValue() {
        return miniBarValue;
    }

    public boolean isActive() {
        return active;
    }

    public void activate(@Observes @Any MiniBar minibar) {
        active = true;
        miniBarValue = 0;
        for (Item item : minibar.getItems()) {
            miniBarValue += item.getPrice();
        }
    }

    public void reset() {
        active = false;
        itemsPurchased.clear();
        charge = 0;
    }
}
