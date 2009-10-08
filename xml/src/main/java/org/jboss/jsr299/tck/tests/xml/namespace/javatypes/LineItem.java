package org.jboss.jsr299.tck.tests.xml.namespace.javatypes;

class LineItem
{
   private String description = "";
   private String productId = "";
   private Integer quantity = 0;
   private Double amount = 0.0d;

   public String getDescription()
   {
      return description;
   }
   public void setDescription(String description)
   {
      this.description = description;
   }
   public String getProductId()
   {
      return productId;
   }
   public void setProductId(String productId)
   {
      this.productId = productId;
   }
   public Integer getQuantity()
   {
      return quantity;
   }
   public void setQuantity(Integer quantity)
   {
      this.quantity = quantity;
   }
   public Double getAmount()
   {
      return amount;
   }
   public void setAmount(Double amount)
   {
      this.amount = amount;
   } 
}
