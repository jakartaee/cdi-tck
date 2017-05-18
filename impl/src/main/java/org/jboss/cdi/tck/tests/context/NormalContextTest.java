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
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.MockCreationalContext;
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
@SpecVersion(spec = "cdi", version = "2.0")
public class NormalContextTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(NormalContextTest.class)
                .withExtension(AfterBeanDiscoveryObserver.class).build();
    }

    @Test
    @SpecAssertion(section = CONTEXT, id = "j")
    @SpecAssertion(section = CONTEXT, id = "l")
    @SpecAssertion(section = NORMAL_SCOPE, id = "c")
    public void testGetReturnsExistingInstance() {
        Bean<MySessionBean> mySessionBean = getBeans(MySessionBean.class).iterator().next();
        CreationalContext<MySessionBean> creationalContext = getCurrentManager().createCreationalContext(mySessionBean);
        MySessionBean first = getCurrentManager().getContext(SessionScoped.class).get(mySessionBean, creationalContext);
        first.setId(10);
        MySessionBean second = getCurrentManager().getContext(SessionScoped.class).get(mySessionBean, creationalContext);
        assert second.getId() == 10;
        MySessionBean third = getCurrentManager().getContext(SessionScoped.class).get(mySessionBean);
        assert third.getId() == 10;
        MySessionBean fourth = getCurrentManager().getContext(SessionScoped.class).get(mySessionBean,
                getCurrentManager().createCreationalContext(mySessionBean));
        assert fourth.getId() == 10;
    }

    @Test
    @SpecAssertion(section = CONTEXT, id = "l")
    public void testGetWithCreationalContextReturnsNewInstance() {
        MyContextual bean = AfterBeanDiscoveryObserver.getBean();
        bean.setShouldReturnNullInstances(false);

        CreationalContext<MySessionBean> creationalContext = new MockCreationalContext<MySessionBean>();
        MySessionBean newBean = getCurrentManager().getContext(SessionScoped.class).get(bean, creationalContext);
        assert newBean != null;
        assert bean.isCreateCalled();
    }

    @Test
    @SpecAssertion(section = CONTEXT, id = "nb")
    public void testGetMayNotReturnNullUnlessContextualCreateReturnsNull() {
        // The case of no creational context is already tested where a null is
        // returned. Here we just test that the contextual create can return null.
        MyContextual bean = AfterBeanDiscoveryObserver.getBean();
        bean.setShouldReturnNullInstances(true);

        CreationalContext<MySessionBean> creationalContext = new MockCreationalContext<MySessionBean>();
        assert getCurrentManager().getContext(SessionScoped.class).get(bean, creationalContext) == null;
        assert bean.isCreateCalled();
    }

    @Test
    @SpecAssertion(section = NORMAL_SCOPE, id = "e")
    public void testSameNormalScopeBeanInjectedEverywhere() {
        SimpleBeanA instanceOfA = getContextualReference(SimpleBeanA.class);
        SimpleBeanB instanceOfB = getContextualReference(SimpleBeanB.class);
        instanceOfA.getZ().setName("Ben");
        assert instanceOfA.getZ().getName().equals("Ben");
        assert instanceOfB.getZ().getName().equals("Ben");
    }
}
