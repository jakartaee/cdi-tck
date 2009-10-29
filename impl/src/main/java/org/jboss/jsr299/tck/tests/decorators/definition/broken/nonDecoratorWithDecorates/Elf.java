package org.jboss.jsr299.tck.tests.decorators.definition.broken.nonDecoratorWithDecorates;

import javax.decorator.Delegate;

class Elf
{
   @Delegate ChristmasTree tree;
}
