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

import static org.jboss.cdi.tck.cdi.Sections.BEAN;
import static org.jboss.cdi.tck.cdi.Sections.CONTEXT;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0-PFD")
public class DestroyedInstanceReturnedByGetTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DestroyedInstanceReturnedByGetTest.class).build();
    }

    @Test
    @SpecAssertion(section = CONTEXT, id = "q")
    @SpecAssertion(section = BEAN, id = "aa")
    public void testDestroyedInstanceMustNotBeReturnedByGet() {
        assert getBeans(MySessionBean.class).size() == 1;
        Bean<MySessionBean> mySessionBean = getBeans(MySessionBean.class).iterator().next();
        CreationalContext<MySessionBean> sessionCreationalContext = getCurrentManager().createCreationalContext(mySessionBean);
        MySessionBean beanInstance = mySessionBean.create(sessionCreationalContext);
        assert beanInstance != null;
        Context sessionContext = getCurrentManager().getContext(SessionScoped.class);
        destroyContext(sessionContext);
        setContextActive(sessionContext);

        beanInstance = sessionContext.get(mySessionBean);
        assert beanInstance == null;
    }

}
