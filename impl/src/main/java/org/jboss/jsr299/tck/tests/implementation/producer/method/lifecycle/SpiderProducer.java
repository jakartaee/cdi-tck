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
package org.jboss.jsr299.tck.tests.implementation.producer.method.lifecycle;

import javax.enterprise.inject.Produces;

class SpiderProducer
{
   private static Tarantula tarantulaCreated;
   private static boolean tarantulaDestroyed;
   private static boolean destroyArgumentsSet;
   
   public @Produces @Pet Tarantula produceTarantula()
   {
      Tarantula tarantula = new Tarantula("Pete");
      tarantulaCreated = tarantula;
      resetTarantulaDestroyed();
      return tarantula;
   }
   
   public @Produces @Null Spider getNullSpider()
   {
      return null;
   }
   
   // FIXME this should be allowed
//   public void destroyTarantula(@Disposes Tarantula spider, @Current BeanManager beanManager)
//   {
//      tarantulaDestroyed = true;
//      if (beanManager != null)
//      {
//         destroyArgumentsSet = true;
//      }
//   }

   public static boolean isTarantulaCreated()
   {
      return tarantulaCreated != null;
   }

   public static boolean isTarantulaDestroyed()
   {
      return tarantulaDestroyed;
   }

   public static void resetTarantulaCreated()
   {
      SpiderProducer.tarantulaCreated = null;
   }

   public static void resetTarantulaDestroyed()
   {
      SpiderProducer.tarantulaDestroyed = false;
      SpiderProducer.destroyArgumentsSet = false;
   }

   public static boolean isDestroyArgumentsSet()
   {
      return destroyArgumentsSet;
   }

   public static Tarantula getTarantulaCreated()
   {
      return tarantulaCreated;
   }
   
   public static void reset()
   {
      resetTarantulaCreated();
      resetTarantulaDestroyed();
   }
}
