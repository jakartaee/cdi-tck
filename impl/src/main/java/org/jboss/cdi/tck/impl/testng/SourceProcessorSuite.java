package org.jboss.cdi.tck.impl.testng;

import jakarta.enterprise.inject.Vetoed;
import org.jboss.cdi.tck.api.Configuration;
import org.jboss.cdi.tck.impl.ConfigurationFactory;
import org.jboss.cdi.tck.spi.SourceProcessor;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * A TestNG before/after suite listener that creates a pass marker file to determine what pass of
 * a two pass run scenario is in effect. This is used by the {@link SourceProcessor} based runtimes to
 * first compile and perform annotation processing on the tests, followed by running the tests.
 */
@Vetoed
public class SourceProcessorSuite {
   static private final Logger logger = Logger.getLogger(SourceProcessorSuite.class.getName());
   static final String PASS_ONE_MARKER = "target/PASS_ONE_MARKER";
   static final String PASS_TWO_MARKER = "target/PASS_TWO_MARKER";
   static File passOneMarker;
   static File passTwoMarker;
   static SourceProcessor.Phase phase;

   /**
    * Accessor for the current source processing phase
    * @return The current source processing phase
    */
   public static SourceProcessor.Phase currentPhase() {
      return phase;
   }

   /**
    * Determine the source processing phase by looking for and creating the two marker files.
    * @see #PASS_ONE_MARKER
    * @see #PASS_TWO_MARKER
    * @throws IOException on failure to create a marker file
    */
   @BeforeSuite(alwaysRun = true)
   public static void beforeSuite() throws IOException {
      passOneMarker = new File(PASS_ONE_MARKER);
      logger.info("SourceProcessorSuite.beforeSuite");
      Configuration configuration = ConfigurationFactory.get();
      if(passOneMarker.exists()) {
         passTwoMarker = new File(PASS_TWO_MARKER);
         boolean ok = passTwoMarker.exists();
         if(!ok) {
            ok = passTwoMarker.createNewFile();
         }
         if(!ok) {
            throw new IOException("Failed to create marker file: "+passTwoMarker.getAbsolutePath());
         }
         logger.info("Pass one marker exists, pass two: "+passTwoMarker.getAbsolutePath());
         phase = SourceProcessor.Phase.PASS_TWO;
         configuration.setSourceProcessorPassTwo();
      } else {
         boolean ok = passOneMarker.createNewFile();
         if(!ok) {
            throw new IOException("Failed to create marker file: "+passOneMarker.getAbsolutePath());
         }
         logger.info("No pass one marker, created, pass one: "+passOneMarker.getAbsolutePath());
         phase = SourceProcessor.Phase.PASS_ONE;
         configuration.setSourceProcessorPassOne();
      }
   }
   @AfterSuite(alwaysRun = true)
   public static void afterSuite() {
      logger.info("SourceProcessorSuite.afterSuite");
   }
}
