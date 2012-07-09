package org.jboss.cdi.tck.tests.event.observer.transactional;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;

import java.util.List;

import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.cdi.tck.util.SimpleLogger;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author Martin Kouba
 */
@Test(groups = { INTEGRATION })
@SpecVersion(spec = "cdi", version = "20091101")
public class TransactionalObserverTest extends AbstractTest {

    private static final SimpleLogger logger = new SimpleLogger(TransactionalObserverTest.class);

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(TransactionalObserverTest.class).withDefaultPersistenceXml()
                .build();
    }

    @Inject
    AccountService accountService;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "10.4.4", id = "a"), @SpecAssertion(section = "10.4.4", id = "b"),
            @SpecAssertion(section = "10.4.4", id = "c"), @SpecAssertion(section = "10.4.4", id = "d"),
            @SpecAssertion(section = "10.4.4", id = "e"), @SpecAssertion(section = "10.4.4", id = "gaa"),
            @SpecAssertion(section = "10.5", id = "bb") })
    public void testSucessfullTransaction() throws Exception {

        logger.log("testSucessfullTransaction");
        ActionSequence.reset();

        // Checkpoint is right before tx commit
        accountService.withdrawSuccesTransaction(1);

        // Test sequence
        // BEFORE_COMPLETION must be fired at the beginning of the commit (after checkpoint)
        // AFTER_SUCCESS and AFTER_COMPLETION must be fired after BEFORE_COMPLETION
        // AFTER_FAILURE is not fired
        ActionSequence.getSequence().beginsWith(TransactionPhase.IN_PROGRESS.toString(), "checkpoint",
                TransactionPhase.BEFORE_COMPLETION.toString());
        ActionSequence.getSequence().containsAll(TransactionPhase.AFTER_SUCCESS.toString(),
                TransactionPhase.AFTER_COMPLETION.toString());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "10.4.4", id = "a"), @SpecAssertion(section = "10.4.4", id = "b"),
            @SpecAssertion(section = "10.4.4", id = "c"), @SpecAssertion(section = "10.4.4", id = "d"),
            @SpecAssertion(section = "10.4.4", id = "e"), @SpecAssertion(section = "10.4.4", id = "gaa"),
            @SpecAssertion(section = "10.5", id = "bb") })
    public void testFailedTransaction() throws Exception {

        logger.log("testFailedTransaction");
        ActionSequence.reset();

        // Checkpoint is right before tx rollback
        accountService.withdrawFailedTransaction(2);

        // AFTER_FAILURE and AFTER_COMPLETION must be fired after checkpoint
        // AFTER_SUCCESS and BEFORE_COMPLETION is not fired
        ActionSequence.getSequence().beginsWith(TransactionPhase.IN_PROGRESS.toString(), "checkpoint");
        ActionSequence.getSequence().containsAll(TransactionPhase.AFTER_FAILURE.toString(),
                TransactionPhase.AFTER_COMPLETION.toString());
    }

    /**
     * No transaction - send all events immediately.
     * 
     * @throws Exception
     */
    @Test
    @SpecAssertions({ @SpecAssertion(section = "10.4.4", id = "a"), @SpecAssertion(section = "10.5", id = "bc") })
    public void testNoTransaction() throws Exception {

        logger.log("testNoTransaction");
        ActionSequence.reset();

        // Checkpoint is after event send
        accountService.withdrawNoTransaction(3);

        // No TX is active - all events are fired immediately and thus before checkpoint
        List<String> sequence = ActionSequence.getSequenceData();
        assertEquals(sequence.size(), 6);
        assertEquals(sequence.get(sequence.size() - 1), "checkpoint");
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "10.5", id = "bda") })
    public void testObserverFailedTransaction() throws Exception {

        logger.log("testObserverFailedTransaction");
        ActionSequence.reset();

        accountService.withdrawObserverFailedTransaction(2);

        // IN_PROGRESS is fired twice
        // AFTER_FAILURE and AFTER_COMPLETION must be fired after checkpoint
        // AFTER_SUCCESS and BEFORE_COMPLETION is not fired
        ActionSequence.getSequence().beginsWith(TransactionPhase.IN_PROGRESS.toString(),
                TransactionPhase.IN_PROGRESS.toString(), "checkpoint");
        ActionSequence.getSequence().containsAll(TransactionPhase.AFTER_FAILURE.toString(),
                TransactionPhase.AFTER_COMPLETION.toString());
    }

}
