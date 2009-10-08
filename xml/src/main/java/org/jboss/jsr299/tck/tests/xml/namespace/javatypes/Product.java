package org.jboss.jsr299.tck.tests.xml.namespace.javatypes;

class Product
{
   private int[]  inventories;
   private String prodId;
   
   public Product()
   {
      
   }
   
   public Product(String productId, int[] currentInventories)
   {
      this.prodId = productId;
      this.inventories = currentInventories;
   }

   public int[] getInventories()
   {
      return inventories;
   }

   public String getProdId()
   {
      return prodId;
   }
}
