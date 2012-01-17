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
package org.jboss.cdi.tck.tests.event.resolve.typeWithParameters;

import static org.jboss.cdi.tck.TestGroups.EVENTS;

import java.util.ArrayList;

import javax.enterprise.event.Observes;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class CheckTypeParametersWhenResolvingObserversTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(CheckTypeParametersWhenResolvingObserversTest.class).build();
    }

    public static class CharacterList extends ArrayList<Character> {
        private static final long serialVersionUID = 1L;
    }

    public static class StringList extends ArrayList<String> {
        private static final long serialVersionUID = 1L;
    }

    public static class IntegerList extends ArrayList<Integer> {
        private static final long serialVersionUID = 1L;
    }

    public static class StringListObserver {
        public boolean wasNotified = false;

        public void observer(@Observes ArrayList<String> event) {
            wasNotified = true;
        }
    }

    public static class IntegerListObserver {
        public boolean wasNotified = false;

        public void observer(@Observes ArrayList<Integer> event) {
            wasNotified = true;
        }
    }

    @Test(groups = { EVENTS })
    @SpecAssertions({ @SpecAssertion(section = "10.2.1", id = "b"), @SpecAssertion(section = "11.3.11", id = "a") })
    public void testResolvingChecksTypeParameters() {
        assert getCurrentManager().resolveObserverMethods(new StringList()).size() == 1;
        assert getCurrentManager().resolveObserverMethods(new IntegerList()).size() == 1;
        assert getCurrentManager().resolveObserverMethods(new CharacterList()).size() == 0;
    }

    @Test(groups = { EVENTS })
    @SpecAssertions({ @SpecAssertion(section = "10.2.1", id = "a"), @SpecAssertion(section = "10.4", id = "aa")
    // FIXME also 10.3.1, which does not yet have spec assertions cataloged
    })
    public void testResolvingChecksTypeParametersOnObservesMethod() {
        Foo<String> fooEvent = new Foo<String>() {
        };
        getCurrentManager().fireEvent(fooEvent);
        assert fooEvent.isObserved();
    }
}
