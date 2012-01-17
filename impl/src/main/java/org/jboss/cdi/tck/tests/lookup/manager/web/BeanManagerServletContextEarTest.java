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
package org.jboss.cdi.tck.tests.lookup.manager.web;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import javax.servlet.ServletContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.application6.ApplicationDescriptor;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Verifies that each web application obtains the correct BeanManager from the {@link ServletContext}.
 * 
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = JAVAEE_FULL)
@SpecVersion(spec = "cdi", version = "20091101")
public class BeanManagerServletContextEarTest extends AbstractTest {

    @ArquillianResource(AlphaServlet.class)
    private URL urlAlpha;

    @ArquillianResource(BravoServlet.class)
    private URL urlBravo;

    @Deployment(testable = false)
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder().withTestClass(
                BeanManagerServletContextEarTest.class).build();
        StringAsset applicationXml = new StringAsset(Descriptors.create(ApplicationDescriptor.class).applicationName("Test")
                .createModule().getOrCreateWeb().webUri("alpha.war").contextRoot("/alpha").up().up().createModule()
                .getOrCreateWeb().webUri("bravo.war").contextRoot("/bravo").up().up().exportAsString());
        enterpriseArchive.setApplicationXML(applicationXml);

        WebArchive fooArchive = new WebArchiveBuilder().notTestArchive().withName("alpha.war")
                .withClasses(Foo.class, Bar.class, AlphaServlet.class, VerifyingListener.class)
                .withDefaultEjbModuleDependency().build();
        enterpriseArchive.addAsModule(fooArchive);

        // This war is equal to alpha.war but has the Bar alternative enabled
        WebArchive barArchive = new WebArchiveBuilder().notTestArchive().withName("bravo.war")
                .withBeansXml(Descriptors.create(BeansDescriptor.class).createAlternatives().clazz(Bar.class.getName()).up())
                .withClasses(Foo.class, Bar.class, BravoServlet.class, VerifyingListener.class)
                .withDefaultEjbModuleDependency().build();
        enterpriseArchive.addAsModule(barArchive);

        return enterpriseArchive;

    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.3.1", id = "e") })
    public void testCorrectBeanManagerAvailable() throws Exception {

        WebClient client = new WebClient();

        TextPage page1 = client.getPage(urlAlpha);
        assertEquals(page1.getContent(), "foo,foo");

        TextPage page2 = client.getPage(urlBravo);
        assertEquals(page2.getContent(), "bar,bar");
    }
}
