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
package org.jboss.cdi.tck.spi;

import jakarta.el.ELContext;
import jakarta.enterprise.inject.spi.BeanManager;

/**
 * This interface provides operations relating to EL.
 *
 * For the purpose of TCK, EL function and variable mapping is not required and therefore may be disabled.
 *
 * The TCK porting package must provide an implementation of this interface which is suitable for the target Web Beans
 * implementation.
 *
 * @author Pete Muir
 */
public interface EL {

    public static final String PROPERTY_NAME = EL.class.getName();

    /**
     *
     *
     * @param <T>
     * @param expression
     * @param expectedType
     * @return
     */
    public <T> T evaluateValueExpression(BeanManager beanManager, String expression, Class<T> expectedType);

    /**
     *
     * @param <T>
     * @param expression
     * @param expectedType
     * @param expectedParamTypes
     * @return
     */
    public <T> T evaluateMethodExpression(BeanManager beanManager, String expression, Class<T> expectedType, Class<?>[] expectedParamTypes,
            Object[] expectedParams);

    /**
     *
     * @param beanManager
     * @return
     */
    public ELContext createELContext(BeanManager beanManager);

}
