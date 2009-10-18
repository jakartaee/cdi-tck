package org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise.broken.twoBeansSpecializeTheSameBean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentError;
import org.jboss.jsr299.tck.tests.inheritance.specialization.simple.broken.two.TwoSpecializingBeansForOneSpecializedTest;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@Packaging(PackagingType.EAR)
@BeansXml("beans.xml")
@ExpectedDeploymentException(DeploymentError.class)
@SpecVersion(spec="cdi", version="20091018")
public class TwoBeansSpecializeTheSameBeanTest extends AbstractJSR299Test
{
   /**
    * This method tests session beans
    * 
    * @see org.jboss.jsr299.tck.tests.inheritance.specialization.producer.method.broken.twoBeansSpecializeTheSameBean.TwoBeansSpecializeTheSameBeanTest#testTwoBeansSpecializeTheSameBean()
    * @see TwoSpecializingBeansForOneSpecializedTest#testTwoBeansSpecializeTheSameBean()
    * 
    */
   //@Test
   //@SpecAssertion(section = "4.3.3", id = "c") removed from spec
   public void testTwoBeansSpecializeTheSameBean()
   {
      assert false;
   }
}
