package org.jboss.cdi.tck.tests.event.observer.transactional;

import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;

import org.jboss.cdi.tck.Timer;

/**
 * 
 * @author Martin Kouba
 */
public class AccountTransactionObserver {

    public static long beforeCompletionObservedTime = 0l;
    public static long afterCompletionObservedTime = 0l;
    public static long afterSuccessObservedTime = 0l;
    public static long afterFailureObservedTime = 0l;
    public static long noTxObservedTime = 0l;

    /**
     * 
     * @param withdrawal
     * @throws Exception
     */
    public void withdrawAfterSuccess(@Observes(during = TransactionPhase.AFTER_SUCCESS) Withdrawal withdrawal) throws Exception {
        logEventFired(TransactionPhase.AFTER_SUCCESS);
        afterSuccessObservedTime = now();
        pause();
    }

    /**
     * 
     * @param withdrawal
     * @throws Exception
     */
    public void withdrawAfterCompletion(@Observes(during = TransactionPhase.AFTER_COMPLETION) Withdrawal withdrawal)
            throws Exception {
        logEventFired(TransactionPhase.AFTER_COMPLETION);
        afterCompletionObservedTime = now();
        pause();
    }

    /**
     * 
     * @param withdrawal
     * @throws Exception
     */
    public void withdrawBeforeCompletion(@Observes(during = TransactionPhase.BEFORE_COMPLETION) Withdrawal withdrawal)
            throws Exception {
        logEventFired(TransactionPhase.BEFORE_COMPLETION);
        beforeCompletionObservedTime = now();
        pause();
    }

    /**
     * Always fire immediately.
     * 
     * @param withdrawal
     * @throws Exception
     */
    public void withdrawNoTx(@Observes(during = TransactionPhase.IN_PROGRESS) Withdrawal withdrawal) throws Exception {
        noTxObservedTime = now();
        pause();
    }

    /**
     * 
     * @param withdrawal
     * @throws Exception
     */
    public void withdrawAfterFailure(@Observes(during = TransactionPhase.AFTER_FAILURE) Withdrawal withdrawal) throws Exception {
        logEventFired(TransactionPhase.AFTER_FAILURE);
        afterFailureObservedTime = now();
        pause();
    }

    private long now() {
        return System.currentTimeMillis();
    }

    private void pause() throws Exception {
        Timer.startNew(500l);
    }

    private void logEventFired(TransactionPhase phase) {
        System.out.println(phase);
    }

    public static void reset() {
        beforeCompletionObservedTime = 0l;
        afterCompletionObservedTime = 0l;
        afterSuccessObservedTime = 0l;
        afterFailureObservedTime = 0l;
        noTxObservedTime = 0l;
    }

    public static boolean allEventsFiredBeforeCheckpoint(long checkpoint) {
        return isEventFiredBeforeCheckpoint(beforeCompletionObservedTime, checkpoint)
                && isEventFiredBeforeCheckpoint(afterCompletionObservedTime, checkpoint)
                && isEventFiredBeforeCheckpoint(afterSuccessObservedTime, checkpoint)
                && isEventFiredBeforeCheckpoint(afterFailureObservedTime, checkpoint)
                && isEventFiredBeforeCheckpoint(noTxObservedTime, checkpoint);
    }

    /**
     * 
     * @param fireTime
     * @param checkpoint
     * @return
     */
    private static boolean isEventFiredBeforeCheckpoint(long fireTime, long checkpoint) {
        return fireTime > 0l && fireTime < checkpoint;
    }

}
