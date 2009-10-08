package org.jboss.jsr299.tck.tests.implementation.simple.definition;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MockEntity
{
   private Integer id;
   
   @Id
   public Integer getId() { return id; }   
   public void setId(Integer id) { this.id = id; }
}
