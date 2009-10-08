package org.jboss.jsr299.tck.tests.xml.annotationtypes.notvalidxml.interceptorbindingtype.notvalidtype;

import javax.inject.DefinitionException;

import org.hibernate.tck.annotations.SpecAssertion;
import org.hibernate.tck.annotations.SpecAssertions;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.tests.xml.annotationtypes.notvalidxml.foo.AnnotationType;
import org.jboss.jsr299.tck.tests.xml.annotationtypes.notvalidxml.foo.TestInterceptorBindingType;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@Classes( { TestInterceptorBindingType.class, AnnotationType.class })
@BeansXml("beans.xml")
@ExpectedDeploymentException(DefinitionException.class)
public class NotValidTypeTest extends AbstractJSR299Test
{
   @Test(groups = "xml")
   @SpecAssertions( { @SpecAssertion(section = "9.4.2", id = "e") })
   public void testNotValidType()
   {
      assert false;
   }
}
