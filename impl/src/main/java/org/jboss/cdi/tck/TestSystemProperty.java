/*
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck;

/**
 *
 * @author Martin Kouba
 */
public enum TestSystemProperty {

    EXCLUDE_DUMMY("cdiTckExcludeDummy", "true");

    TestSystemProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
