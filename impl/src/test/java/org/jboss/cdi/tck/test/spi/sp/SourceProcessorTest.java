package org.jboss.cdi.tck.test.spi.sp;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.cdi.tck.impl.ConfigurationFactory;
import org.jboss.cdi.tck.shrinkwrap.JarArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test that a CDI-lite mode archive triggers the SourceProcessor and VersionProcessor annotation processor.
 */
public class SourceProcessorTest extends Arquillian {
    @Deployment
    public static JavaArchive createTestArchive() {
        ConfigurationFactory.get().setCDILiteModeFlag(Boolean.TRUE);
        System.out.printf("SourceProcessorTest.createTestArchive()\n");
        JavaArchive jar = new JarArchiveBuilder()
                .debugMode()
                .withTestClassPackage(SourceProcessorTest.class)
                .build();
        return jar;
    }

    @Test
    public void testSrcProcRan() throws Exception {
        // This generated class is not visible to the test framework yet
        //Class<?> initClass = Class.forName("org.jboss.cdi.tck.test.spi.sp.VersionSetter");
        System.out.printf("SourceProcessorTest#testSrcProcRan(%s)\n", SomeBean.VERSION);
        // The VersionProcessor sets this flag if it runs successfully
        Boolean processorRan = Boolean.valueOf(System.getProperty("org.jboss.cdi.tck.test.spi.container.VersionProcessor.ran", "false"));
        Assert.assertTrue(processorRan, "VersionProcessor ran");
    }
}
