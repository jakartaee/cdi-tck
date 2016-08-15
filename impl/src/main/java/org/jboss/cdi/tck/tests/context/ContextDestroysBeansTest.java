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
package org.jboss.cdi.tck.tests.context;

import static org.jboss.cdi.tck.cdi.Sections.CONTEXT;
import static org.jboss.cdi.tck.cdi.Sections.NORMAL_SCOPE;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author Nicklas Karlsson
 * @author Pete Muir
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class ContextDestroysBeansTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ContextDestroysBeansTest.class)
                .withExtension(AfterBeanDiscoveryObserver.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = CONTEXT, id = "p"), @SpecAssertion(section = NORMAL_SCOPE, id = "d") })
    public void testContextDestroysBeansWhenDestroyed() {
        MyContextual bean = AfterBeanDiscoveryObserver.getBean();
        bean.setShouldReturnNullInstances(false);

        Context sessionContext = getCurrentManager().getContext(SessionScoped.class);
        CreationalContext<MySessionBean> creationalContext = getCurrentManager().createCreationalContext(bean);
        MySessionBean instance = sessionContext.get(bean, creationalContext);
        instance.ping();
        assert instance != null;
        assert bean.isCreateCalled();

        destroyContext(sessionContext);
        assert bean.isDestroyCalled();
    }

}
