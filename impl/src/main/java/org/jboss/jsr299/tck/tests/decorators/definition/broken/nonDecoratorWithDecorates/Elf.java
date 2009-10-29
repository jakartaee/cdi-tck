package org.jboss.jsr299.tck.tests.decorators.definition.broken.nonDecoratorWithDecorates;

import javax.decorator.Delegate;
import javax.inject.Inject;

class Elf
{
   @Inject @Delegate ChristmasTree tree;
}
