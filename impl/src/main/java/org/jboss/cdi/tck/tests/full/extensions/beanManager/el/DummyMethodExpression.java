/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.full.extensions.beanManager.el;

import jakarta.el.ELContext;
import jakarta.el.MethodExpression;
import jakarta.el.MethodInfo;

/**
 * @author Martin Kouba
 * 
 */
@SuppressWarnings("serial")
public class DummyMethodExpression extends MethodExpression {

    @Override
    public MethodInfo getMethodInfo(ELContext context) {
        return null;
    }

    @Override
    public Object invoke(ELContext context, Object[] params) {
        // Create dependent bean with the name "foo"
        Object foo = context.getELResolver().getValue(context, null, "foo");
        context.getELResolver().getValue(context, foo, "value");
        // The resulting instance is reused for every appearance of the EL name
        context.getELResolver().getValue(context, null, "foo");
        return Integer.valueOf(-1);
    }

    @Override
    public String getExpressionString() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean isLiteralText() {
        return false;
    }

}
