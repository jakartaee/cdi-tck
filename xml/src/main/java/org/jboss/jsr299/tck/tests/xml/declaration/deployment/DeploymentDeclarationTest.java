package org.jboss.jsr299.tck.tests.xml.declaration.deployment;

import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.hibernate.tck.annotations.SpecAssertion;
import org.hibernate.tck.annotations.SpecAssertions;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.tests.xml.declaration.deployment.foo.Order;
import org.jboss.jsr299.tck.tests.xml.declaration.deployment.foo.TestDeploymentType;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@Classes({Order.class, TestDeploymentType.class})
@BeansXml("beans.xml")
public class DeploymentDeclarationTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertions({
	  @SpecAssertion(section="9.12", id="a"),
	  @SpecAssertion(section="9.12.1", id="a"),
	  @SpecAssertion(section="11.2", id = "a")
   })
   public void testDeploymentDeclaration()
   {
      BeanManager beanManager = getCurrentManager();
      Set<Bean<Order>> beans = getBeans(Order.class);
      
      assert !beans.isEmpty() : "There are no registered beans of type '" + Order.class + "'";
      assert beans.iterator().next().getDeploymentType().equals(TestDeploymentType.class) : "Deployment type of bean '" + Order.class.getName() + "' is not '" + TestDeploymentType.class.getName() + "'";
   }
}
