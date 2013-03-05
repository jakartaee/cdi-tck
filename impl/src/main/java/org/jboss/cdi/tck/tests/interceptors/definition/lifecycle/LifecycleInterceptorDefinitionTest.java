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
package org.jboss.cdi.tck.tests.interceptors.definition.lifecycle;

import static org.jboss.cdi.tck.TestGroups.INTERCEPTORS_SPEC;
import static org.testng.Assert.assertEquals;

import java.util.List;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class LifecycleInterceptorDefinitionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(LifecycleInterceptorDefinitionTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createInterceptors()
                                .clazz(MissileInterceptor.class.getName(), DestructionInterceptor.class.getName()).up())
                .build();
    }

    @Test(groups = INTERCEPTORS_SPEC)
    // @SpecAssertions({ @SpecAssertion(section = BINDING_INTERCEPTOR_TO_BEAN, id = "a"), @SpecAssertion(section =
    // INTERCEPTOR_RESOLUTION, id = "a") })
    public void testLifecycleInterception() {

        ActionSequence.reset();

        Bean<Missile> bean = getUniqueBean(Missile.class);
        CreationalContext<Missile> ctx = getCurrentManager().createCreationalContext(bean);
        Missile missile = bean.create(ctx);
        missile.fire();
        bean.destroy(missile, ctx);

        assertEquals(ActionSequence.getSequenceSize("postConstruct"), 1);
        assertEquals(ActionSequence.getSequenceData("postConstruct").get(0), MissileInterceptor.class.getName());
        assertEquals(ActionSequence.getSequenceSize("preDestroy"), 1);
        assertEquals(ActionSequence.getSequenceData("preDestroy").get(0), MissileInterceptor.class.getName());
    }

    @Test(groups = INTERCEPTORS_SPEC)
    // @SpecAssertions({ @SpecAssertion(section = INTERCEPTOR_RESOLUTION, id = "a") })
    public void tesMultipleLifecycleInterceptors() {

        ActionSequence.reset();

        Bean<Rocket> bean = getUniqueBean(Rocket.class);
        CreationalContext<Rocket> ctx = getCurrentManager().createCreationalContext(bean);
        Rocket rocket = bean.create(ctx);
        rocket.fire();
        bean.destroy(rocket, ctx);

        List<String> postConstruct = ActionSequence.getSequenceData("postConstruct");
        assertEquals(postConstruct.size(), 4);
        assertEquals(postConstruct.get(0), MissileInterceptor.class.getName());
        assertEquals(postConstruct.get(1), DestructionInterceptor.class.getName());
        assertEquals(postConstruct.get(2), Weapon.class.getName());
        assertEquals(postConstruct.get(3), Rocket.class.getName());

        List<String> preDestroy = ActionSequence.getSequenceData("preDestroy");
        assertEquals(preDestroy.size(), 4);
        assertEquals(preDestroy.get(0), MissileInterceptor.class.getName());
        assertEquals(preDestroy.get(1), DestructionInterceptor.class.getName());
        assertEquals(preDestroy.get(2), Weapon.class.getName());
        assertEquals(preDestroy.get(3), Rocket.class.getName());
    }
}
