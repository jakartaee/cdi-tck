package org.jboss.cdi.tck.tests.context.passivating.broken.producer.method.enterprise;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

@Stateful
@SessionScoped
public class ProducerMethodParamInjectionCorralBroken extends Corral {
    @Override
    public void ping() {
        
    }
    
    @Produces
    @British
    public Herd produce(@British Cow cow){
       return new Herd();
    }
}
