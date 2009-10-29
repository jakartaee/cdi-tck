package org.jboss.jsr299.tck.tests.decorators.definition.broken.decoratorListedTwiceInBeansXml;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
class PresentDecorator
{
   @Inject @Delegate Present present;
}
