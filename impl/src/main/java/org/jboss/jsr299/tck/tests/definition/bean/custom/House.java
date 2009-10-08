package org.jboss.jsr299.tck.tests.definition.bean.custom;

import javax.inject.Inject;

class House
{
   @SuppressWarnings("unused")
   @Inject private Cat cat;
}
