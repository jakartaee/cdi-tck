/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.cdi.tck.tests.implementation.builtin.servlet;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Test that servlet container built-in beans are available for injection and that it's possible to obtain an instance of
 * BeanManager from ServletContext.
 * 
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class ServletContainerBuiltinBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ServletContainerBuiltinBeanTest.class).build();
    }

    @Inject
    LowercaseConverter lowercaseConverter;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.7", id = "da"), @SpecAssertion(section = "3.7", id = "db"),
            @SpecAssertion(section = "3.7", id = "dc") })
    public void testBuiltinBeansAvailableForInjection() {

        String result = lowercaseConverter.convert("Foo");
        assertEquals(result, "foo");

        assertNotNull(lowercaseConverter.getHttpServletRequest());
        assertNotNull(lowercaseConverter.getHttpServletRequest().getRequestURL());
        assertNotNull(lowercaseConverter.getHttpSession());
        assertNotNull(lowercaseConverter.getHttpSession().getId());
        assertNotNull(lowercaseConverter.getServletContext());
        assertTrue(lowercaseConverter.getServletContext().getMajorVersion() >= 2);
    }

    @Test
    @SpecAssertion(section = "11.3.1", id = "e")
    public void testBeanManagerAvailableViaServletContext() {

        BeanManager beanManager = (BeanManager) lowercaseConverter.getServletContext()
                .getAttribute(BeanManager.class.getName());
        assertNotNull(beanManager);
        assertEquals(beanManager, getCurrentManager());
        Bean<?> bean = beanManager.resolve(beanManager.getBeans(LowercaseConverter.class));
        LowercaseConverter converter = (LowercaseConverter) beanManager.getReference(bean, LowercaseConverter.class,
                beanManager.createCreationalContext(bean));
        assertEquals(converter.getId(), lowercaseConverter.getId());
    }

    @RunAsClient
    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "3.7", id = "da"), @SpecAssertion(section = "3.7", id = "db"),
            @SpecAssertion(section = "3.7", id = "dc") })
    public void testBuiltinBeansFromClient(@ArquillianResource URL contextPath) throws Exception {

        WebClient client = new WebClient();

        TextPage requestPage = client.getPage(contextPath + "/convert-request?text=BaR");
        assertEquals(requestPage.getContent(), "bar");

        TextPage sessionPage = client.getPage(contextPath + "/convert-session");
        assertEquals(sessionPage.getContent(), "session");

        TextPage contextPage = client.getPage(contextPath + "/convert-context");
        assertEquals(contextPage.getContent(), "servletcontext");
    }

}
