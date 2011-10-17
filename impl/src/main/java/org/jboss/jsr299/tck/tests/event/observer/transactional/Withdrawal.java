package org.jboss.jsr299.tck.tests.event.observer.transactional;

public class Withdrawal {

    private int amount;

    public Withdrawal(int amount) {
        super();
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

}
