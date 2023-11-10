package org.jboss.cdi.tck.tests.alternative.selection;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
@Alternative
@Priority(100)
public class AlternativeDeltaProducer1 {

    @Produces
    public Delta produce() {
        return new Delta(SelectedAlternative03Test.ALT1);
    }
}
