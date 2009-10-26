package org.jboss.jsr299.tck.tests.lookup.injection.non.contextual;

import java.io.IOException;

import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

@Any
public class TestTagHandler extends SimpleTagSupport
{
   private static final long serialVersionUID = -3048065065359948044L;
   public static final String INJECTION_SUCCESS = "Injection works.";
   public static final String INITIALIZER_SUCCESS = "Initializer works.";
   
   @Inject
   private Sheep sheep;
   private boolean initializerCalled = false;
   
   @Inject
   public void initialize(Sheep sheep) {
      initializerCalled = sheep != null;
   }
   
   @Override
   public void doTag() throws JspException, IOException
   {
      if (sheep != null)
      {
         getJspContext().getOut().write(INJECTION_SUCCESS);
      }
      if (initializerCalled) 
      {
         getJspContext().getOut().append(INITIALIZER_SUCCESS);
      }
   }
}
