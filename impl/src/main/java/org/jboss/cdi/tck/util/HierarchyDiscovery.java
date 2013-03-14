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
package org.jboss.cdi.tck.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashSet;
import java.util.Set;


public class HierarchyDiscovery {
    private final Type type;

    private Set<Type> types;

    public HierarchyDiscovery(Type type) {
        this.type = type;
    }

    protected void add(Type type) {
        types.add(type);
    }

    public Set<Type> getFlattenedTypes() {
        if (types == null) {
            this.types = new HashSet<Type>();
            discoverTypes(type);
        }
        return types;
    }

    public Type getResolvedType() {
        return resolveType(type, type);
    }

    private void discoverTypes(Type type) {
        if (type != null) {
            add(type);
            if (type instanceof Class) {
                discoverFromClass((Class<?>) type);
            } else if (type instanceof ParameterizedType) {
                Type rawType = ((ParameterizedType) type).getRawType();
                if (rawType instanceof Class) {
                    discoverFromClass((Class<?>) rawType);
                }
            }
        }
    }

    private void discoverFromClass(Class<?> clazz) {
        discoverTypes(resolveType(type, clazz.getGenericSuperclass()));
        for (Type c : clazz.getGenericInterfaces()) {
            discoverTypes(resolveType(type, c));
        }
    }

    /**
     * Gets the actual types by resolving TypeParameters.
     *
     * @param beanType
     * @param type
     * @return actual type
     */
    private Type resolveType(Type beanType, Type type) {
        if (type instanceof ParameterizedType) {
            if (beanType instanceof ParameterizedType) {
                return resolveParameterizedType((ParameterizedType) beanType, (ParameterizedType) type);
            }
            if (beanType instanceof Class) {
                return resolveType(((Class<?>) beanType).getGenericSuperclass(), type);
            }
        }

        if (type instanceof TypeVariable) {
            if (beanType instanceof ParameterizedType) {
                return resolveTypeParameter((ParameterizedType) beanType, (TypeVariable<?>) type);
            }
            if (beanType instanceof Class) {
                return resolveType(((Class<?>) beanType).getGenericSuperclass(), type);
            }
        }
        return type;
    }

    private Type resolveParameterizedType(ParameterizedType beanType, ParameterizedType parameterizedType) {
        Type rawType = parameterizedType.getRawType();
        Type[] actualTypes = parameterizedType.getActualTypeArguments();

        Type resolvedRawType = resolveType(beanType, rawType);
        Type[] resolvedActualTypes = new Type[actualTypes.length];

        for (int i = 0; i < actualTypes.length; i++) {
            resolvedActualTypes[i] = resolveType(beanType, actualTypes[i]);
        }
        // reconstruct ParameterizedType by types resolved TypeVariable.
        return new ParameterizedTypeImpl(resolvedRawType, resolvedActualTypes, parameterizedType.getOwnerType());
    }

    private Type resolveTypeParameter(ParameterizedType beanType, TypeVariable<?> typeVariable) {
        // step1. raw type
        Class<?> actualType = (Class<?>) beanType.getRawType();
        TypeVariable<?>[] typeVariables = actualType.getTypeParameters();
        Type[] actualTypes = beanType.getActualTypeArguments();
        for (int i = 0; i < typeVariables.length; i++) {
            if (typeVariables[i].equals(typeVariable)) {
                return resolveType(type, actualTypes[i]);
            }
        }

        // step2. generic super class
        Type genericSuperType = actualType.getGenericSuperclass();
        Type type = resolveType(genericSuperType, typeVariable);
        if (!(type instanceof TypeVariable<?>)) {
            return type;
        }

        // step3. generic interfaces
        for (Type interfaceType : actualType.getGenericInterfaces()) {
            Type resolvedType = resolveType(interfaceType, typeVariable);
            if (!(resolvedType instanceof TypeVariable<?>)) {
                return resolvedType;
            }
        }

        // don't resolve type variable
        return typeVariable;
    }

}
