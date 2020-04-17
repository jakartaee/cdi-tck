/*
 * JBoss, Home of Professional Open Source
 * Copyright 2017, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.se.context.request;

import static org.jboss.cdi.tck.TestGroups.SE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

import org.jboss.arquillian.container.se.api.ClassPath;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@Test(groups = SE)
@SpecVersion(spec = "cdi", version = "2.0")
public class RequestContextTest extends Arquillian {

    @Deployment
    public static Archive<?> deployment() throws IOException {
        final JavaArchive bda1 = ShrinkWrap.create(JavaArchive.class)
                .addPackage(RequestContextTest.class.getPackage())
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        return ClassPath.builder().add(bda1).build();
    }

    @Test
    @SpecAssertion(section = Sections.REQUEST_CONTEXT, id="db")
    @SpecAssertion(section = Sections.REQUEST_CONTEXT, id="eb")
    public void requestContextIsActiveDuringPostConstructCallback() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer container = seContainerInitializer
                .initialize()) {
            TestBean testBean = container.select(TestBean.class).get();
            assertTrue(testBean.isReqContextActiveDuringPostConstruct());
            try{
                testBean.fail();
                fail();
            }catch (ContextNotActiveException e){

            }
        }
    }

    @Test
    @SpecAssertion(section = Sections.REQUEST_CONTEXT, id="da")
    @SpecAssertion(section = Sections.REQUEST_CONTEXT, id="ea")
    public void requestContextIsActiveDuringAsyncObserverNotification() throws InterruptedException {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer container = seContainerInitializer
                .initialize()) {
            Event<Object> event = container.getBeanManager().getEvent();
            BlockingQueue<Payload> queue = new LinkedBlockingQueue<>();

            //fire event twice but each event gets new requestContext
            event.select(Payload.class).fireAsync(new Payload()).thenAccept(queue::offer);
            Payload payload = queue.poll(2, TimeUnit.SECONDS);

            event.select(Payload.class).fireAsync(payload).thenAccept(queue::offer);
            payload = queue.poll(2, TimeUnit.SECONDS);

            assertEquals(payload.getI(), 2);
        }
    }

}
