package org.jboss.cdi.tck.interceptors.tests.contract.aroundInvoke;

import static org.testng.Assert.assertEquals;

import jakarta.interceptor.Interceptors;
import javax.naming.InitialContext;
import jakarta.transaction.TransactionSynchronizationRegistry;

public class Baz {

    private static Object key;

    public static Object getKey() {
        return key;
    }

    @Interceptors(BazInterceptor.class)
    void ping() throws Exception {
        TransactionSynchronizationRegistry tsr = (TransactionSynchronizationRegistry) InitialContext
                .doLookup("java:comp/TransactionSynchronizationRegistry");
        key = tsr.getTransactionKey();
        assertEquals(key, Bar.getKey());
    }
}
