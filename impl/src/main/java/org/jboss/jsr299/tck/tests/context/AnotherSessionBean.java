package org.jboss.jsr299.tck.tests.context;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

@SessionScoped class AnotherSessionBean implements Serializable
{
   private static final long serialVersionUID = 1L;
   
   public void ping() {}
}
