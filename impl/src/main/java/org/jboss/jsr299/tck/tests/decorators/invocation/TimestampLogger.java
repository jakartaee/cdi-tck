/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.jsr299.tck.tests.decorators.invocation;

import javax.decorator.Delegate;
import javax.decorator.Decorator;


/**
 * @author pmuir
 *
 */
@Decorator
public class TimestampLogger
{
   
   public static final String PREFIX = TimestampLogger.class.getSimpleName();
   
   private static String message;
   
   private static boolean initializeCalled;
   
   @Delegate
   private Logger logger;
   
   public void log(String message)
   {
      TimestampLogger.message = message;
      logger.log(PREFIX + message);
   }
   
   public void initialize()
   {
      initializeCalled = true;
   }
   
   /**
    * @return the message
    */
   public static String getMessage()
   {
      return message;
   }
   
   /**
    * @param message the message to set
    */
   public static void reset()
   {
      TimestampLogger.message = null;
      initializeCalled = false;
   }
   
   /**
    * @return the initializeCalled
    */
   public static boolean isInitializeCalled()
   {
      return initializeCalled;
   }


}
