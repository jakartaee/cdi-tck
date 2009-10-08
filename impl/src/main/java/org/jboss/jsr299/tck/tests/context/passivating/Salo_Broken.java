package org.jboss.jsr299.tck.tests.context.passivating;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

@SessionScoped
class Salo_Broken extends City implements Serializable
{
   private static final long serialVersionUID = 5500591077418621816L;
   
   @SuppressWarnings("unused")
   @Inject
   private transient Violation reference;
   
}
