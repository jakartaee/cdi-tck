package org.jboss.cdi.tck.tests.alternative.selection;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class StandardDeltaProducer {

    @Produces
    public Delta produce() {
        return new Delta(SelectedAlternative03Test.DEFAULT);
    }
}
