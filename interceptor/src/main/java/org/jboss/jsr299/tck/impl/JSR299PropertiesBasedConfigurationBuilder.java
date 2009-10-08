package org.jboss.jsr299.tck.impl;

import java.io.IOException;

import javax.context.Context;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.api.JSR299Configuration;
import org.jboss.jsr299.tck.literals.NewLiteral;
import org.jboss.jsr299.tck.spi.Beans;
import org.jboss.jsr299.tck.spi.Contexts;
import org.jboss.jsr299.tck.spi.EL;
import org.jboss.jsr299.tck.spi.Managers;
import org.jboss.testharness.impl.PropertiesBasedConfigurationBuilder;

public class JSR299PropertiesBasedConfigurationBuilder extends PropertiesBasedConfigurationBuilder<JSR299Configuration>
{
   
   public JSR299PropertiesBasedConfigurationBuilder() throws IOException
   {
      super(new JSR299ConfigurationImpl());
      super.getConfiguration().getExtraPackages().add(NewLiteral.class.getPackage().getName());
      super.getConfiguration().getExtraPackages().add(AbstractJSR299Test.class.getPackage().getName());
      super.getConfiguration().getExtraPackages().add(JSR299ConfigurationImpl.class.getPackage().getName());
      super.getConfiguration().getExtraPackages().add(JSR299Configuration.class.getPackage().getName());
      super.getConfiguration().getExtraPackages().add(Managers.class.getPackage().getName());
      super.getConfiguration().getExtraDeploymentProperties().add("org.jboss.testharness.api.ConfigurationBuilder=org.jboss.jsr299.tck.impl.JSR299PropertiesBasedConfigurationBuilder");
   }
   
   @Override
   public JSR299PropertiesBasedConfigurationBuilder init()
   {
      super.init();
      getConfiguration().setManagers(getInstanceValue(Managers.PROPERTY_NAME, Managers.class, true));
      getConfiguration().setBeans(getInstanceValue(Beans.PROPERTY_NAME, Beans.class, true));
      getConfiguration().setEl(getInstanceValue(EL.PROPERTY_NAME, EL.class, true));
      
      @SuppressWarnings("unchecked")      
      Contexts<? extends Context> instanceValue = getInstanceValue(Contexts.PROPERTY_NAME, Contexts.class, true);
      
      getConfiguration().setContexts(instanceValue);
      return this;
   }
   
}
