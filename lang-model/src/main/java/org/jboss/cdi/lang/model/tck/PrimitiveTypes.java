/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.lang.model.tck;

import jakarta.enterprise.lang.model.declarations.ClassInfo;
import jakarta.enterprise.lang.model.declarations.FieldInfo;
import jakarta.enterprise.lang.model.types.PrimitiveType;

public class PrimitiveTypes {
    boolean booleanField;
    byte byteField;
    short shortField;
    int intField;
    long longField;
    float floatField;
    double doubleField;
    char charField;

    String stringField;

    void voidMethod() {
    }

    public static void verify(ClassInfo clazz) {
        assertPrimitiveField(clazz, "booleanField", PrimitiveType.PrimitiveKind.BOOLEAN);
        assertPrimitiveField(clazz, "byteField", PrimitiveType.PrimitiveKind.BYTE);
        assertPrimitiveField(clazz, "shortField", PrimitiveType.PrimitiveKind.SHORT);
        assertPrimitiveField(clazz, "intField", PrimitiveType.PrimitiveKind.INT);
        assertPrimitiveField(clazz, "longField", PrimitiveType.PrimitiveKind.LONG);
        assertPrimitiveField(clazz, "floatField", PrimitiveType.PrimitiveKind.FLOAT);
        assertPrimitiveField(clazz, "doubleField", PrimitiveType.PrimitiveKind.DOUBLE);
        assertPrimitiveField(clazz, "charField", PrimitiveType.PrimitiveKind.CHAR);

        assert LangModelUtils.singleMethod(clazz, "voidMethod").returnType().isVoid();
        assert !LangModelUtils.singleMethod(clazz, "voidMethod").returnType().isPrimitive();

        assert !LangModelUtils.singleField(clazz, "stringField").type().isPrimitive();
    }

    private static void assertPrimitiveField(ClassInfo clazz, String fieldName, PrimitiveType.PrimitiveKind kind) {
        FieldInfo field = LangModelUtils.singleField(clazz, fieldName);

        assert field.type().isPrimitive();
        assert field.type().asPrimitive().primitiveKind() == kind;

        assert field.type().asPrimitive().isBoolean() == (kind == PrimitiveType.PrimitiveKind.BOOLEAN);
        assert field.type().asPrimitive().isByte() == (kind == PrimitiveType.PrimitiveKind.BYTE);
        assert field.type().asPrimitive().isShort() == (kind == PrimitiveType.PrimitiveKind.SHORT);
        assert field.type().asPrimitive().isInt() == (kind == PrimitiveType.PrimitiveKind.INT);
        assert field.type().asPrimitive().isLong() == (kind == PrimitiveType.PrimitiveKind.LONG);
        assert field.type().asPrimitive().isFloat() == (kind == PrimitiveType.PrimitiveKind.FLOAT);
        assert field.type().asPrimitive().isDouble() == (kind == PrimitiveType.PrimitiveKind.DOUBLE);
        assert field.type().asPrimitive().isChar() == (kind == PrimitiveType.PrimitiveKind.CHAR);
    }
}
