package org.jboss.cdi.tck.tests.event.observer.transactional;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.jboss.cdi.tck.util.ActionSequence;

@Named
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class AccountService {

    @Resource
    private UserTransaction userTransaction;

    @Inject
    Event<Withdrawal> event;

    @Inject
    Event<Failure> eventFailure;

    /**
     * 
     * @param amount
     * @throws Exception
     */
    public void withdrawSuccesTransaction(int amount) throws Exception {
        userTransaction.begin();
        event.fire(new Withdrawal(amount));
        ActionSequence.addAction("checkpoint");
        userTransaction.commit();
    }

    /**
     * 
     * @param amount
     * @throws Exception
     */
    public void withdrawFailedTransaction(int amount) throws Exception {
        userTransaction.begin();
        event.fire(new Withdrawal(amount));
        ActionSequence.addAction("checkpoint");
        // Failed for any reason
        userTransaction.rollback();
    }

    /**
     * 
     * @param amount
     * @throws Exception
     */
    public void withdrawNoTransaction(int amount) throws Exception {
        event.fire(new Withdrawal(amount));
        ActionSequence.addAction("checkpoint");
    }

    /**
     * 
     * @param amount
     * @throws Exception
     */
    public void withdrawObserverFailedTransaction(int amount) throws Exception {
        userTransaction.begin();
        event.fire(new Withdrawal(amount));
        eventFailure.fire(new Failure());
        ActionSequence.addAction("checkpoint");
        if (userTransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
            userTransaction.rollback();
        } else {
            userTransaction.commit();
        }
    }

}
