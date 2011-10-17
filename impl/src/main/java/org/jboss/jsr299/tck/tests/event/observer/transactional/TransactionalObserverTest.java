package org.jboss.jsr299.tck.tests.event.observer.transactional;

import static org.testng.AssertJUnit.assertTrue;

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
        AccountTransactionObserver.reset();
        long checkpoint = accountService.withdrawSuccesTransaction(1);
        assertTrue(AccountTransactionObserver.afterSuccessObservedTime > checkpoint);
        assertTrue(AccountTransactionObserver.afterCompletionObservedTime < AccountTransactionObserver.afterSuccessObservedTime);
        assertTrue(AccountTransactionObserver.beforeCompletionObservedTime < AccountTransactionObserver.afterCompletionObservedTime);
        assertTrue(AccountTransactionObserver.afterFailureObservedTime == 0l);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "10.4.4", id = "a"), @SpecAssertion(section = "10.4.4", id = "b"),
            @SpecAssertion(section = "10.4.4", id = "c"), @SpecAssertion(section = "10.4.4", id = "d"),
            @SpecAssertion(section = "10.4.4", id = "e"), @SpecAssertion(section = "10.4.4", id = "gaa") })
    public void testFailedTransaction() throws Exception {
        AccountTransactionObserver.reset();
        long checkpoint = accountService.withdrawSuccesTransaction(1);
        assertTrue(AccountTransactionObserver.afterFailureObservedTime > checkpoint);
        assertTrue(AccountTransactionObserver.afterCompletionObservedTime < AccountTransactionObserver.afterFailureObservedTime);
        assertTrue(AccountTransactionObserver.beforeCompletionObservedTime < AccountTransactionObserver.afterCompletionObservedTime);
        assertTrue(AccountTransactionObserver.afterSuccessObservedTime == 0l);
    }

    /**
     * No transaction - send all events immediately.
     * 
     * @throws Exception
     */
    @Test
    @SpecAssertion(section = "10.4.4", id = "a")
    public void testNoTransaction() throws Exception {
        AccountTransactionObserver.reset();
        long checkpoint = accountService.withdrawNoTransaction(1);
        assertTrue(AccountTransactionObserver.allEventsFiredBeforeCheckpoint(checkpoint));
    }

}
