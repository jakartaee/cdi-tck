package org.jboss.jsr299.tck.tests.lookup.injection.non.contextual;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class TestTagHandler extends SimpleTagSupport
{
   private static final long serialVersionUID = -3048065065359948044L;
   public static final String SUCCESS = "It works.";
   public static final String FAILURE = "It is broken.";
   
   @Inject
   private Sheep sheep;
   
   @Override
   public void doTag() throws JspException, IOException
   {
      if (sheep == null)
      {
         getJspContext().getOut().write(FAILURE);
      }
      else
      {
         getJspContext().getOut().write(SUCCESS);
      }
   }
}
