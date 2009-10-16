package org.jboss.jsr299.tck.tests.decorators.invocation;

import javax.decorator.Decorates;
import javax.decorator.Decorator;

@Decorator
public class PigStyDecorator implements PigSty {
    public static boolean decoratorCalled = false;
    
    @Decorates PigSty pigSty;
    
    public void clean() {
        decoratorCalled = true;
        pigSty.clean();
    }

    public void washDown() {
        decoratorCalled = true;
        pigSty.washDown();
    }

}
