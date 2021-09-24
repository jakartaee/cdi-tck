package org.jboss.cdi.tck.tests.full.context.passivating.broken.producer.method.managed.dependent;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Produces;

@SessionScoped
public class ProducerMethodParamInjectionCorralBroken extends Ranch {
    @Override
    public void ping() {
        
    }
    
    @Produces
    @British
    @SessionScoped
    public Herd produce(@British Cow cow){
       return new Herd();
    }
}
