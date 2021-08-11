package org.jboss.cdi.tck.test.spi.warsp;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.cdi.tck.impl.ConfigurationFactory;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test that a CDI-lite mode archive triggers the SourceProcessor and VersionProcessor annotation processor
 * for a WebArchive based test
 */
public class SourceProcessorTest extends Arquillian {
    @Deployment
    public static WebArchive createTestArchive() {
        System.setProperty("org.jboss.cdi.tck.test.spi.container.VersionProcessor.ran", "false");
        ConfigurationFactory.get().setCDILiteModeFlag(Boolean.TRUE);
        System.out.printf("SourceProcessorTest.createTestArchive()\n");
        WebArchive war = new WebArchiveBuilder()
                .debugMode()
                .withTestClassPackage(SourceProcessorTest.class)
                .withClass(org.jboss.cdi.tck.test.spi.sp.VersionInfo.class)
                .build();
        return war;
    }

    @Test
    public void testSrcProcRanOnWebArchive() throws Exception {
        // This generated class is not visible to the test framework yet
        //Class<?> initClass = Class.forName("org.jboss.cdi.tck.test.spi.sp.VersionSetter");
        System.out.printf("SourceProcessorTest#testSrcProcRanOnWebArchive(%s)\n", SomeBean.VERSION);
        // The VersionProcessor sets this flag if it runs successfully
        Boolean processorRan = Boolean.valueOf(System.getProperty("org.jboss.cdi.tck.test.spi.container.VersionProcessor.ran", "false"));
        Assert.assertTrue(processorRan, "VersionProcessor ran");
    }
}
