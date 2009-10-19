package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.New;
import javax.inject.Inject;

public class Griffin
{
   
   @Inject @New ArrayList<String> list;
   
   public List<String> getList()
   {
      return list;
   }

}
