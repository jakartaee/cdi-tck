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
package org.jboss.jsr299.tck.tests.context;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.impl.MockCreationalContext;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author Nicklas Karlsson
 * @author Pete Muir
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class DestroyForSameCreationalContextTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DestroyForSameCreationalContextTest.class).build();
    }

    @Test
    @SpecAssertion(section = "6.2", id = "r")
    public void testDestroyForSameCreationalContextOnly() {
        // Check that the mock cc is called (via cc.release()) when we request a context destroyed
        // Note that this is an indirect effect
        Context sessionContext = getCurrentManager().getContext(SessionScoped.class);

        Bean<AnotherSessionBean> sessionBean = getBeans(AnotherSessionBean.class).iterator().next();

        MockCreationalContext.reset();
        CreationalContext<AnotherSessionBean> creationalContext = new MockCreationalContext<AnotherSessionBean>();
        AnotherSessionBean instance = sessionContext.get(sessionBean, creationalContext);
        instance.ping();

        destroyContext(sessionContext);
        assert MockCreationalContext.isReleaseCalled();

    }

}
