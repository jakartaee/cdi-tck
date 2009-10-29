package org.jboss.jsr299.tck.tests.decorators.definition.broken.decoratorListedTwiceInBeansXml;

import javax.decorator.Delegate;
import javax.decorator.Decorator;

@Decorator
class PresentDecorator
{
   @Delegate Present present;
}
