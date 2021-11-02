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
