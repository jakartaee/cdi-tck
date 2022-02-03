package org.jboss.cdi.tck.tests.lookup.dynamic.handle;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.math.BigDecimal;

@Dependent
public class Client {

    @Inject
    Instance<Alpha> alphaInstance;

    @Inject
    Instance<Object> instance;

    @Inject
    @Juicy
    Instance<BigDecimal> bigDecimalInstance;

    Instance<Alpha> getAlphaInstance() {
        return alphaInstance;
    }

    Instance<BigDecimal> getBigDecimalInstance() {
        return bigDecimalInstance;
    }

    Instance<Object> getInstance() {
        return instance;
    }
}
