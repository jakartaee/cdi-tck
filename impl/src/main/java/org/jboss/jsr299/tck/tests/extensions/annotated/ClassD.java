package org.jboss.jsr299.tck.tests.extensions.annotated;

import javax.enterprise.context.RequestScoped;

@RequestScoped
@Tame
class ClassD extends AbstractC implements InterfaceB
{

}
