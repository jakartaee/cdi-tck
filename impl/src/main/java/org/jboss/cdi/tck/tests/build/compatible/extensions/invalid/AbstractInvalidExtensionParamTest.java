package org.jboss.cdi.tck.tests.build.compatible.extensions.invalid;

import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.testng.annotations.Test;

public abstract class AbstractInvalidExtensionParamTest extends AbstractTest {

    /**
     * Prepared archive builder minus the extension being tested
     */
    static WebArchiveBuilder prepareArchiveBuilder() {
        return new WebArchiveBuilder()
                // add some class that will be type-discovered
                .withClasses(SomeBean.class)
                .withTestClass(AbstractInvalidExtensionParamTest.class)
                // annotated discovery mode
                .withBeansXml(new BeansXml());
    }


    @Test()
    //@SpecAssertion(section = TODO, id = "TODO")
    public void test() {
        // deployment should fail
    }
}
