package org.jboss.jsr299.tck.tests.xml.annotationtypes.notvalidxml.interceptorbindingtype.childnotannotation;

import javax.inject.DefinitionException;

import org.hibernate.tck.annotations.SpecAssertion;
import org.hibernate.tck.annotations.SpecAssertions;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@BeansXml("beans.xml")
@ExpectedDeploymentException(DefinitionException.class)
public class NotAnnotationTypeTest extends AbstractJSR299Test
{
   @Test(groups = "xml")
   @SpecAssertions( { 
      @SpecAssertion(section = "9.4.2", id = "c") 
   })
   public void testChildNotAnnotationType()
   {
      assert false;
   }
}
