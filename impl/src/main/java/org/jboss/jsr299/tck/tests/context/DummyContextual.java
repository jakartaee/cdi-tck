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
package org.jboss.jsr299.tck.tests.context;

import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;

/**
 * @author pmuir
 * 
 */
final class DummyContextual implements Contextual<String> {
    private CreationalContext<String> creationalContextPassedToCreate;
    private CreationalContext<String> creationalContextPassedToDestroy;

    public String create(CreationalContext<String> creationalContext) {
        this.creationalContextPassedToCreate = creationalContext;
        return "123";
    }

    public void destroy(String instance, CreationalContext<String> creationalContext) {
        this.creationalContextPassedToDestroy = creationalContext;
    }

    /**
     * @return the creationalContextPassedToCreate
     */
    public CreationalContext<String> getCreationalContextPassedToCreate() {
        return creationalContextPassedToCreate;
    }

    /**
     * @return the creationalContextPassedToDestroy
     */
    public CreationalContext<String> getCreationalContextPassedToDestroy() {
        return creationalContextPassedToDestroy;
    }
}