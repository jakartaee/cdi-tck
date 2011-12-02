package org.jboss.jsr299.tck.tests.event.observer.transactional;

import static org.jboss.jsr299.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
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
public class TransactionalObserverTest extends AbstractJSR299Test {

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
            @SpecAssertion(section = "10.4.4", id = "e"), @SpecAssertion(section = "10.4.4", id = "gaa") })
    public void testSucessfullTransaction() throws Exception {
        logTestMethod("testSucessfullTransaction");
        AccountTransactionObserver.reset();
        // Checkpoint is right before tx commit
        long checkpoint = accountService.withdrawSuccesTransaction(1);
        // BEFORE_COMPLETION must be fired at the beginning of the commit (after checkpoint)
        assertTrue(AccountTransactionObserver.beforeCompletionObservedTime > checkpoint);
        // AFTER_COMPLETION and AFTER_SUCCESS must be fired after BEFORE_COMPLETION
        assertTrue(AccountTransactionObserver.afterSuccessObservedTime > AccountTransactionObserver.beforeCompletionObservedTime);
        assertTrue(AccountTransactionObserver.afterCompletionObservedTime > AccountTransactionObserver.beforeCompletionObservedTime);
        // AFTER_FAILURE is not fired
        assertTrue(AccountTransactionObserver.afterFailureObservedTime == 0l);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "10.4.4", id = "a"), @SpecAssertion(section = "10.4.4", id = "b"),
            @SpecAssertion(section = "10.4.4", id = "c"), @SpecAssertion(section = "10.4.4", id = "d"),
            @SpecAssertion(section = "10.4.4", id = "e"), @SpecAssertion(section = "10.4.4", id = "gaa") })
    public void testFailedTransaction() throws Exception {
        logTestMethod("testFailedTransaction");
        AccountTransactionObserver.reset();
        // Checkpoint is right before tx rollback
        long checkpoint = accountService.withdrawFailedTransaction(2);
        // AFTER_COMPLETION and AFTER_FAILURE must be fired after checkpoint
        assertTrue(AccountTransactionObserver.afterCompletionObservedTime > checkpoint);
        assertTrue(AccountTransactionObserver.afterFailureObservedTime > checkpoint);
        // AFTER_SUCCESS and BEFORE_COMPLETION is not fired
        assertTrue(AccountTransactionObserver.afterSuccessObservedTime == 0l);
        assertTrue(AccountTransactionObserver.beforeCompletionObservedTime == 0l);
    }

    /**
     * No transaction - send all events immediately.
     * 
     * @throws Exception
     */
    @Test
    @SpecAssertion(section = "10.4.4", id = "a")
    public void testNoTransaction() throws Exception {
        logTestMethod("testNoTransaction");
        AccountTransactionObserver.reset();
        // Checkpoint is after event send
        long checkpoint = accountService.withdrawNoTransaction(3);
        // No TX is active - all events are fired immediately and thus before checkpoint
        assertTrue(AccountTransactionObserver.allEventsFiredBeforeCheckpoint(checkpoint));
    }

    private void logTestMethod(String methodName) {
        System.out.println(methodName);
    }

}
