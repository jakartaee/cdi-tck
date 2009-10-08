package org.jboss.jsr299.tck.tests.xml.namespace.javatypes;

import java.util.ArrayList;

import javax.enterprise.context.RequestScoped;

@RequestScoped
class PurchaseOrder
{   
   private int val;
   
   private ArrayList<String> strArr = new ArrayList<String>();
   
   public PurchaseOrder()
   {
      this.val = 0;
   }
   
   public PurchaseOrder(Integer val, ArrayList<String> strArray)
   {
      this.val = val;
      this.strArr = strArray;
   }
   
   public int getVal()
   {
      return this.val;
   }
   
   public ArrayList<String> getStrArr()
   {
      return this.strArr;
   }
}
