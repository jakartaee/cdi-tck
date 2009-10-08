package org.jboss.jsr299.tck.tests.implementation.producer.method.definition.enterprise;

public class Apple
{
   private AppleTree tree;
   
   public Apple(AppleTree tree)
   {
      this.tree = tree;
   }
   
   public AppleTree getTree()
   {
      return tree;
   }
}