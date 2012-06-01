package org.jboss.cdi.tck.tests.event.observer.transactional;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
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

}
