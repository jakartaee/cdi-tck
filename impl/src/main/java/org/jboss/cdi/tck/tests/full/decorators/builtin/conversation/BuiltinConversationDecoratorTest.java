/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.full.decorators.builtin.conversation;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DECORATOR_INVOCATION;
import static org.jboss.cdi.tck.cdi.Sections.DECORATOR_RESOLUTION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.lang.reflect.Type;
import java.util.Collections;

import jakarta.enterprise.context.Conversation;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.full.decorators.AbstractDecoratorTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 *
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class BuiltinConversationDecoratorTest extends AbstractDecoratorTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(BuiltinConversationDecoratorTest.class)
                .withClass(AbstractDecoratorTest.class)
                .withBeansXml(
                        new BeansXml().decorators(ConversationDecorator.class))
                .build();
    }

    @Inject
    ConversationObserver conversationObserver;

    @Inject
    Conversation conversation;

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_INVOCATION, id = "ach"),
            @SpecAssertion(section = DECORATOR_RESOLUTION, id = "aa") })
    public void testDecoratorIsResolved() {
        checkDecorator(resolveUniqueDecorator(Collections.<Type> singleton(Conversation.class)), ConversationDecorator.class,
                Collections.<Type> singleton(Conversation.class), Conversation.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_INVOCATION, id = "ach") })
    public void testDecoratorIsInvoked() {
        String cid = "foo";
        conversation.begin(cid);
        assertFalse(conversation.isTransient());
        assertEquals(conversationObserver.getDecoratedConversationId(), cid);
    }

}
