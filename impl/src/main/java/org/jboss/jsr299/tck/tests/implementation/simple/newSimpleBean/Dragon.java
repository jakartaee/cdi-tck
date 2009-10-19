package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.New;
import javax.inject.Inject;

public class Dragon
{
   
   private Set<String> children;
   
   @Inject
   public void initialize(@New HashSet<String> children)
   {
      this.children = children;
   }
   
   public Set<String> getChildren()
   {
      return children;
   }

}
