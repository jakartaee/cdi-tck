/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.jsr299.tck.api;

import javax.enterprise.context.spi.Context;

import org.jboss.jsr299.tck.spi.Beans;
import org.jboss.jsr299.tck.spi.Contexts;
import org.jboss.jsr299.tck.spi.EL;
import org.jboss.jsr299.tck.spi.Managers;
import org.jboss.testharness.api.Configuration;

/**
 * The configuration of the TCK.
 * 
 * The TCK may be configured using system properties or placed in a properties
 * file called META-INF/web-beans-tck.properties.
 * 
 * Porting package property names are the FQCN of the SPI class. Other property
 * names (one for each non-porting package SPI configuration option) are
 * specified here. The defaults are also listed here.
 * 
 * The TCK may also be configured programatically through this interface
 * 
 * @author Pete Muir
 *
 */
public interface JSR299Configuration extends Configuration
{
   /**
    * The implementation of {@link Beans} in use.
    */
   public Beans getBeans();
   
   /**
    * The implementation of {@link Contexts} in use.
    */
   public <T extends Context> Contexts<T> getContexts();
   
   /**
    * The implementation of {@link Managers} in use.
    */
   public Managers getManagers();

   public void setBeans(Beans beans);

   public <T extends Context> void setContexts(Contexts<T> contexts);

   public void setManagers(Managers managers);

   public void setEl(EL el);
   
   /**
    * The implementation of {@link EL} in use.
    */
   public EL getEl();
   
}