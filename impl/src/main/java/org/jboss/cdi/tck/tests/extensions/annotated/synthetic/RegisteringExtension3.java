package org.jboss.cdi.tck.tests.extensions.annotated.synthetic;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

import org.jboss.cdi.tck.util.AddForwardingAnnotatedTypeAction;

public class RegisteringExtension3 implements Extension {
	
	void registerEnum(@Observes BeforeBeanDiscovery event, final BeanManager manager){
		
        new AddForwardingAnnotatedTypeAction<Vegetables>() {

            @Override
            public String getBaseId() {
                return RegisteringExtension3.class.getName();
            }

            @Override
            public AnnotatedType<Vegetables> delegate() {
                return manager.createAnnotatedType(Vegetables.class);
            }
        }.perform(event);
		
	}

}
