package org.jboss.jsr299.tck.tests.decorators.definition.broken.decoratorListedTwiceInBeansXml;

import javax.decorator.Decorates;
import javax.decorator.Decorator;

@Decorator
class PresentDecorator
{
   @Decorates Present present;
}
