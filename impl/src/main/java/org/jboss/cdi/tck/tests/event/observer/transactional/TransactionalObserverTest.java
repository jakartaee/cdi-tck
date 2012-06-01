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

        logTestMethod("testSucessfullTransaction");
        ActionSequence.reset();

        // Checkpoint is right before tx commit
        accountService.withdrawSuccesTransaction(1);

        // Test sequence
        // BEFORE_COMPLETION must be fired at the beginning of the commit (after checkpoint)
        // AFTER_SUCCESS must be fired after BEFORE_COMPLETION and before AFTER_COMPLETION
        // AFTER_FAILURE is not fired
        ActionSequence correctSequence = new ActionSequence();
        correctSequence.add(TransactionPhase.IN_PROGRESS.toString());
        correctSequence.add("checkpoint");
        correctSequence.add(TransactionPhase.BEFORE_COMPLETION.toString());
        correctSequence.add(TransactionPhase.AFTER_SUCCESS.toString());
        correctSequence.add(TransactionPhase.AFTER_COMPLETION.toString());

        assertEquals(ActionSequence.getSequence(), correctSequence);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "10.4.4", id = "a"), @SpecAssertion(section = "10.4.4", id = "b"),
            @SpecAssertion(section = "10.4.4", id = "c"), @SpecAssertion(section = "10.4.4", id = "d"),
            @SpecAssertion(section = "10.4.4", id = "e"), @SpecAssertion(section = "10.4.4", id = "gaa"),
            @SpecAssertion(section = "10.5", id = "bb") })
    public void testFailedTransaction() throws Exception {

        logTestMethod("testFailedTransaction");
        ActionSequence.reset();

        // Checkpoint is right before tx rollback
        accountService.withdrawFailedTransaction(2);

        // AFTER_FAILURE must be fired after checkpoint and before AFTER_COMPLETION
        // AFTER_SUCCESS and BEFORE_COMPLETION is not fired
        ActionSequence correctSequence = new ActionSequence();
        correctSequence.add(TransactionPhase.IN_PROGRESS.toString());
        correctSequence.add("checkpoint");
        correctSequence.add(TransactionPhase.AFTER_FAILURE.toString());
        correctSequence.add(TransactionPhase.AFTER_COMPLETION.toString());

        assertEquals(ActionSequence.getSequence(), correctSequence);
    }

    /**
     * No transaction - send all events immediately.
     * 
     * @throws Exception
     */
    @Test
    @SpecAssertions({ @SpecAssertion(section = "10.4.4", id = "a"), @SpecAssertion(section = "10.5", id = "bc") })
    public void testNoTransaction() throws Exception {

        logTestMethod("testNoTransaction");
        ActionSequence.reset();

        // Checkpoint is after event send
        accountService.withdrawNoTransaction(3);

        // No TX is active - all events are fired immediately and thus before checkpoint
        List<String> sequence = ActionSequence.getSequenceData();
        assertEquals(sequence.size(), 6);
        assertEquals(sequence.get(sequence.size() - 1), "checkpoint");
    }

    private void logTestMethod(String methodName) {
        logger.log(methodName);
    }

}
