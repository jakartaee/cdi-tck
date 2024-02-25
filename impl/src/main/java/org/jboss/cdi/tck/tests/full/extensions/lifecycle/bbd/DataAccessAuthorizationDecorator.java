/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.lifecycle.bbd;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

import org.jboss.cdi.tck.util.SimpleLogger;

@Decorator
public class DataAccessAuthorizationDecorator implements DataAccess {

    private static final SimpleLogger logger = new SimpleLogger(DataAccessAuthorizationDecorator.class);

    @Inject
    @Delegate
    DataAccess delegate;

    @Inject
    User user;

    public void save() {
        authorize("save");
        delegate.save();
    }

    public void delete() {
        authorize("delete");
        delegate.delete();
    }

    private void authorize(String action) {
        Object id = delegate.getId();
        Class<?> type = delegate.getDataType();
        if (user.hasPermission(action, type, id)) {
            logger.log("Authorized for " + action);
        } else {
            logger.log("Not authorized for " + action);
            throw new NotAuthorizedException(action);
        }
    }

    public Class<?> getDataType() {
        return delegate.getDataType();
    }

    public Object getId() {
        return delegate.getId();
    }

    public Object load(Object id) {
        return delegate.load(id);
    }

}
