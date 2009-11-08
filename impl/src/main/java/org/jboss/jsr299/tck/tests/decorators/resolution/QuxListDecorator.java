package org.jboss.jsr299.tck.tests.decorators.resolution;

import java.util.List;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
public class QuxListDecorator
{
   
   @Inject @Delegate
   private Qux<List<String>> qux;

}
