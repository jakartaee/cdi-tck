package org.jboss.jsr299.tck.tests.extensions.beanManager;

import javax.enterprise.inject.Specializes;

@Specializes @Tame
class DerivedBean extends SimpleBean
{
   protected int simpleField;
}
