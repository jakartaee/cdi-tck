package org.jboss.jsr299.tck.tests.xml.annotationtypes;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.spi.Bean;

import org.hibernate.tck.annotations.SpecAssertion;
import org.hibernate.tck.annotations.SpecAssertions;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@BeansXml("beans.xml")
public class AnnotationTypesTest extends AbstractJSR299Test
{
   @Test(groups = { "ri-broken", "xml" })
   @SpecAssertions({
      @SpecAssertion(section="2.3.2", id="c"),
      @SpecAssertion(section="9.4", id="a")
   })
   public void testBindingTypeFromXML()
   {
      //TODO The binding type appears as a non-binding annotation in the call to resolveByType()
      assert getBeans(BeanWithBinding.class, new AnnotationLiteral<TestBindingType>(){}).size() > 0;
   }

   @Test(groups = { "ri-broken", "xml" })
   @SpecAssertions({
      @SpecAssertion(section="9.4", id="b")
   })
   public void testInterceptorBindingTypeFromXML()
   {
      BeanWithInterceptorBinding bean = getInstanceByType(BeanWithInterceptorBinding.class);
      assert bean != null : "Test bean not resolvable";
      Object returnValue = bean.getInterceptor();
      //TODO Bean method is not currently being intercepted due to lack of interceptor support in the RI
      assert returnValue != null : "Bean method was not intercepted";
      assert returnValue instanceof TestInterceptor : "Incorrect return type";
   }

   @Test(groups = { "ri-broken", "xml" })
   @SpecAssertions({
      @SpecAssertion(section="9.4", id="c"),
      @SpecAssertion(section="9.4.1", id="a"),
      @SpecAssertion(section="9.4.1", id="d"),
      @SpecAssertion(section="9.4.1", id="e"),
      @SpecAssertion(section="9.4.1", id="f"),
      @SpecAssertion(section="9.4.1", id="g")
   })
   public void testStereotypeFromXML()
   {
      //TODO The stereotype is not being applied to this bean
      Bean<BeanWithStereotype> bean = getBeans(BeanWithStereotype.class).iterator().next();
      assert TestScopeType.class.isAssignableFrom(bean.getScopeType());
      assert TestDeploymentType.class.isAssignableFrom(bean.getDeploymentType());
      assert bean.getName() != null : "No default name for bean";
      assert "beanWithStereotype".equals(bean.getName()) : "Incorrect name for bean";
      
      // Now check that the correct interceptor is also being applied through the
      // stereotype.
      BeanWithStereotype beanWithStereotype = (BeanWithStereotype) getInstanceByName("beanWithStereotype");
      Object interceptor = beanWithStereotype.getInterceptor();
      assert interceptor != null : "Bean method was not intercepted";
      assert interceptor instanceof AnotherTestInterceptor : "Incorrect return type";
   }

   @Test(groups = { "xml" })
   @SpecAssertions({
      @SpecAssertion(section="9.4", id="e")
   })
   public void testBindingAnnotationOverridenByXML()
   {
      assert getInstanceByType(BeanWithBindingAnnotation.class,new AnnotationLiteral<AnotherTestBindingType>(){}) != null;
   }

   @Test(groups = { "ri-broken", "xml" })
   @SpecAssertions({
      @SpecAssertion(section="9.4", id="f")
   })
   public void testInterceptorBindingAnnotationOverridenByXML()
   {
      BeanWithAnotherInterceptorBinding bean = getInstanceByType(BeanWithAnotherInterceptorBinding.class);
      assert bean != null : "Test bean not resolvable";
      Object returnValue = bean.getInterceptor();
      //TODO Bean method is not currently being intercepted due to lack of interceptor support in the RI
      assert returnValue != null : "Bean method was not intercepted";
      assert returnValue instanceof TestInterceptor : "Incorrect return type";
   }

   @Test(groups = { "ri-broken", "xml" })
   @SpecAssertions({
      @SpecAssertion(section="9.4", id="g")
   })
   public void testStereotypeAnnotationOverridenByXML()
   {
      //TODO The stereotype is not being applied to this bean
      Bean<BeanWithAnotherStereotype> bean = getBeans(BeanWithAnotherStereotype.class).iterator().next();
      assert RequestScoped.class.isAssignableFrom(bean.getScopeType());
   }

   @Test(groups = { "ri-broken", "xml" })
   @SpecAssertions({
      @SpecAssertion(section="9.4.2", id="a"),
      @SpecAssertion(section="9.4.2", id="d")
   })
   public void testInheritedInterceptorsFromXML()
   {
      InheritedInterceptorTestBean bean = getInstanceByType(InheritedInterceptorTestBean.class);
      InterceptorRecorder interceptorRecorder = bean.getInterceptors();
      assert interceptorRecorder.containsInterceptor(InterceptorType1.class);
      assert interceptorRecorder.containsInterceptor(InterceptorType2.class);
   }
}
