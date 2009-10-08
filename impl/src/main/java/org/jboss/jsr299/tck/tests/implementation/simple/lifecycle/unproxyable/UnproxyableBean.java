package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle.unproxyable;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class UnproxyableBean
{
   private UnproxyableBean() {}
}
