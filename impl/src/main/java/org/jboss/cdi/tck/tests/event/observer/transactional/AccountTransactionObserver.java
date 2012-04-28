package org.jboss.cdi.tck.tests.event.observer.transactional;

import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;

import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.cdi.tck.util.SimpleLogger;

/**
 * 
 * @author Martin Kouba
 */
public class AccountTransactionObserver {

    private static final SimpleLogger logger = new SimpleLogger(AccountTransactionObserver.class);

    /**
     * 
     * @param withdrawal
     * @throws Exception
     */
    public void withdrawAfterSuccess(@Observes(during = TransactionPhase.AFTER_SUCCESS) Withdrawal withdrawal) throws Exception {
        logEventFired(TransactionPhase.AFTER_SUCCESS);
    }

    /**
     * 
     * @param withdrawal
     * @throws Exception
     */
    public void withdrawAfterCompletion(@Observes(during = TransactionPhase.AFTER_COMPLETION) Withdrawal withdrawal)
            throws Exception {
        logEventFired(TransactionPhase.AFTER_COMPLETION);
    }

    /**
     * 
     * @param withdrawal
     * @throws Exception
     */
    public void withdrawBeforeCompletion(@Observes(during = TransactionPhase.BEFORE_COMPLETION) Withdrawal withdrawal)
            throws Exception {
        logEventFired(TransactionPhase.BEFORE_COMPLETION);
    }

    /**
     * Always fire immediately.
     * 
     * @param withdrawal
     * @throws Exception
     */
    public void withdrawNoTx(@Observes(during = TransactionPhase.IN_PROGRESS) Withdrawal withdrawal) throws Exception {
        logEventFired(TransactionPhase.IN_PROGRESS);
    }

    /**
     * 
     * @param withdrawal
     * @throws Exception
     */
    public void withdrawAfterFailure(@Observes(during = TransactionPhase.AFTER_FAILURE) Withdrawal withdrawal) throws Exception {
        logEventFired(TransactionPhase.AFTER_FAILURE);
    }

    private void logEventFired(TransactionPhase phase) {
        logger.log(phase.toString());
        ActionSequence.add(phase.toString());
    }
}
