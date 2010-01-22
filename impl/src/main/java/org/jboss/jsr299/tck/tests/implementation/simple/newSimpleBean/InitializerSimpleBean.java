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
package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.jsr299.tck.literals.NewLiteral;

@SessionScoped
@Named("Charlie") @Default
class InitializerSimpleBean implements Serializable
{
   
   public static final New NEW = new NewLiteral()
   {
      
      public Class<?> value()
      {
         return InitializerSimpleBean.class;
      }
      
   };
   
   private static final long serialVersionUID = 1L;
   private static int        initializerCalls = 0;

   @Inject
   protected Order           order;

   @Inject
   public void initializer()
   {
      initializerCalls++;
   }

   public void businessMethod()
   {
      
   }

   public static int getInitializerCalls()
   {
      return initializerCalls;
   }

   public static void setInitializerCalls(int initializerCalls)
   {
      InitializerSimpleBean.initializerCalls = initializerCalls;
   }
}
