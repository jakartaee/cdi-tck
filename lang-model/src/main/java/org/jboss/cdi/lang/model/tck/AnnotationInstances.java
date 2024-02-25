/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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

import jakarta.enterprise.lang.model.AnnotationInfo;
import jakarta.enterprise.lang.model.declarations.ClassInfo;
import jakarta.enterprise.lang.model.declarations.MethodInfo;

public class AnnotationInstances {
    @AnnotationMembers
    void defaultValues() {
    }

    @AnnotationMembers(booleanMember = false, byteMember = -1, shortMember = -2, intMember = -3, longMember = -4, floatMember = -5.0F, doubleMember = -6.0, charMember = 'a', stringMember = "bb", classMember = AnnotationInstances.class, enumMember = SimpleEnum.NO, annotationMember = @SimpleAnnotation("ccc"),

            booleanArrayMember = { false, true }, byteArrayMember = { -1, -2 }, shortArrayMember = { -3,
                    -4 }, intArrayMember = { -5, -6 }, longArrayMember = { -7, -8 }, floatArrayMember = { -9.0F,
                            -10.0F }, doubleArrayMember = { -11.0, -12.0 }, charArrayMember = { 'a',
                                    'b' }, stringArrayMember = { "cc", "dd" }, classArrayMember = { AnnotationInstances.class,
                                            AnnotationMembers.class }, enumArrayMember = { SimpleEnum.NO,
                                                    SimpleEnum.YES }, annotationArrayMember = { @SimpleAnnotation("eee"),
                                                            @SimpleAnnotation("fff") })
    void nondefaultValues() {
    }

    public static void verify(ClassInfo clazz) {
        verifyDefaultValues(clazz);
        verifyNondefaultValues(clazz);
    }

    private static void verifyDefaultValues(ClassInfo clazz) {
        MethodInfo method = LangModelUtils.singleMethod(clazz, "defaultValues");
        assert method.annotations().size() == 1;
        assert method.annotations(it -> it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnotationMembers"))
                .size() == 1;
        assert method.annotations(it -> !it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnotationMembers"))
                .size() == 0;
        assert method.hasAnnotation(AnnotationMembers.class);
        assert method.hasAnnotation(it -> it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnotationMembers"));
        assert !method.hasAnnotation(it -> !it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnotationMembers"));

        AnnotationInfo ann = method.annotation(AnnotationMembers.class);

        // boolean booleanMember() default true;
        assert ann.hasMember("booleanMember");
        assert ann.member("booleanMember").isBoolean();
        assert ann.member("booleanMember").asBoolean();

        // byte byteMember() default 1;
        assert ann.hasMember("byteMember");
        assert ann.member("byteMember").isByte();
        assert ann.member("byteMember").asByte() == 1;

        // short shortMember() default 2;
        assert ann.hasMember("shortMember");
        assert ann.member("shortMember").isShort();
        assert ann.member("shortMember").asShort() == 2;

        // int intMember() default 3;
        assert ann.hasMember("intMember");
        assert ann.member("intMember").isInt();
        assert ann.member("intMember").asInt() == 3;

        // long longMember() default 4;
        assert ann.hasMember("longMember");
        assert ann.member("longMember").isLong();
        assert ann.member("longMember").asLong() == 4;

        // float floatMember() default 5.0F;
        assert ann.hasMember("floatMember");
        assert ann.member("floatMember").isFloat();
        assert ann.member("floatMember").asFloat() == 5.0F;

        // double doubleMember() default 6.0;
        assert ann.hasMember("doubleMember");
        assert ann.member("doubleMember").isDouble();
        assert ann.member("doubleMember").asDouble() == 6.0;

        // char charMember() default 'A';
        assert ann.hasMember("charMember");
        assert ann.member("charMember").isChar();
        assert ann.member("charMember").asChar() == 'A';

        // String stringMember() default "BB";
        assert ann.hasMember("stringMember");
        assert ann.member("stringMember").isString();
        assert ann.member("stringMember").asString().equals("BB");

        // Class<?> classMember() default AnnotationMembers.class;
        assert ann.hasMember("classMember");
        assert ann.member("classMember").isClass();
        assert ann.member("classMember").asType().isClass();
        assert ann.member("classMember").asType().asClass().declaration().name()
                .equals("org.jboss.cdi.lang.model.tck.AnnotationMembers");

        // SimpleEnum enumMember() default SimpleEnum.YES;
        assert ann.hasMember("enumMember");
        assert ann.member("enumMember").isEnum();
        assert ann.member("enumMember").asEnum(SimpleEnum.class) == SimpleEnum.YES;
        assert ann.member("enumMember").asEnumClass().name().equals("org.jboss.cdi.lang.model.tck.SimpleEnum");
        assert ann.member("enumMember").asEnumConstant().equals("YES");

        // SimpleAnnotation annotationMember() default @SimpleAnnotation("CCC");
        assert ann.hasMember("annotationMember");
        assert ann.member("annotationMember").isNestedAnnotation();
        assert ann.member("annotationMember").asNestedAnnotation().declaration().name()
                .equals("org.jboss.cdi.lang.model.tck.SimpleAnnotation");
        assert ann.member("annotationMember").asNestedAnnotation().hasValue();
        assert ann.member("annotationMember").asNestedAnnotation().value().isString();
        assert ann.member("annotationMember").asNestedAnnotation().value().asString().equals("CCC");

        // boolean[] booleanArrayMember() default { true, false };
        assert ann.hasMember("booleanArrayMember");
        assert ann.member("booleanArrayMember").isArray();
        assert ann.member("booleanArrayMember").asArray().size() == 2;
        assert ann.member("booleanArrayMember").asArray().get(0).isBoolean();
        assert ann.member("booleanArrayMember").asArray().get(0).asBoolean();
        assert ann.member("booleanArrayMember").asArray().get(1).isBoolean();
        assert !ann.member("booleanArrayMember").asArray().get(1).asBoolean();

        // byte[] byteArrayMember() default { 1, 2 };
        assert ann.hasMember("byteArrayMember");
        assert ann.member("byteArrayMember").isArray();
        assert ann.member("byteArrayMember").asArray().size() == 2;
        assert ann.member("byteArrayMember").asArray().get(0).isByte();
        assert ann.member("byteArrayMember").asArray().get(0).asByte() == 1;
        assert ann.member("byteArrayMember").asArray().get(1).isByte();
        assert ann.member("byteArrayMember").asArray().get(1).asByte() == 2;

        // short[] shortArrayMember() default { 3, 4 };
        assert ann.hasMember("shortArrayMember");
        assert ann.member("shortArrayMember").isArray();
        assert ann.member("shortArrayMember").asArray().size() == 2;
        assert ann.member("shortArrayMember").asArray().get(0).isShort();
        assert ann.member("shortArrayMember").asArray().get(0).asShort() == 3;
        assert ann.member("shortArrayMember").asArray().get(1).isShort();
        assert ann.member("shortArrayMember").asArray().get(1).asShort() == 4;

        // int[] intArrayMember() default { 5, 6 };
        assert ann.hasMember("intArrayMember");
        assert ann.member("intArrayMember").isArray();
        assert ann.member("intArrayMember").asArray().size() == 2;
        assert ann.member("intArrayMember").asArray().get(0).isInt();
        assert ann.member("intArrayMember").asArray().get(0).asInt() == 5;
        assert ann.member("intArrayMember").asArray().get(1).isInt();
        assert ann.member("intArrayMember").asArray().get(1).asInt() == 6;

        // long[] longArrayMember() default { 7, 8 };
        assert ann.hasMember("longArrayMember");
        assert ann.member("longArrayMember").isArray();
        assert ann.member("longArrayMember").asArray().size() == 2;
        assert ann.member("longArrayMember").asArray().get(0).isLong();
        assert ann.member("longArrayMember").asArray().get(0).asLong() == 7;
        assert ann.member("longArrayMember").asArray().get(1).isLong();
        assert ann.member("longArrayMember").asArray().get(1).asLong() == 8;

        // float[] floatArrayMember() default { 9.0F, 10.0F };
        assert ann.hasMember("floatArrayMember");
        assert ann.member("floatArrayMember").isArray();
        assert ann.member("floatArrayMember").asArray().size() == 2;
        assert ann.member("floatArrayMember").asArray().get(0).isFloat();
        assert ann.member("floatArrayMember").asArray().get(0).asFloat() == 9.0F;
        assert ann.member("floatArrayMember").asArray().get(1).isFloat();
        assert ann.member("floatArrayMember").asArray().get(1).asFloat() == 10.0F;

        // double[] doubleArrayMember() default { 11.0, 12.0 };
        assert ann.hasMember("doubleArrayMember");
        assert ann.member("doubleArrayMember").isArray();
        assert ann.member("doubleArrayMember").asArray().size() == 2;
        assert ann.member("doubleArrayMember").asArray().get(0).isDouble();
        assert ann.member("doubleArrayMember").asArray().get(0).asDouble() == 11.0;
        assert ann.member("doubleArrayMember").asArray().get(1).isDouble();
        assert ann.member("doubleArrayMember").asArray().get(1).asDouble() == 12.0;

        // char[] charArrayMember() default { 'A', 'B' };
        assert ann.hasMember("charArrayMember");
        assert ann.member("charArrayMember").isArray();
        assert ann.member("charArrayMember").asArray().size() == 2;
        assert ann.member("charArrayMember").asArray().get(0).isChar();
        assert ann.member("charArrayMember").asArray().get(0).asChar() == 'A';
        assert ann.member("charArrayMember").asArray().get(1).isChar();
        assert ann.member("charArrayMember").asArray().get(1).asChar() == 'B';

        // String[] stringArrayMember() default { "CC", "DD" };
        assert ann.hasMember("stringArrayMember");
        assert ann.member("stringArrayMember").isArray();
        assert ann.member("stringArrayMember").asArray().size() == 2;
        assert ann.member("stringArrayMember").asArray().get(0).isString();
        assert ann.member("stringArrayMember").asArray().get(0).asString().equals("CC");
        assert ann.member("stringArrayMember").asArray().get(1).isString();
        assert ann.member("stringArrayMember").asArray().get(1).asString().equals("DD");

        // Class<?>[] classArrayMember() default { SimpleAnnotation.class, SimpleEnum.class };
        assert ann.hasMember("classArrayMember");
        assert ann.member("classArrayMember").isArray();
        assert ann.member("classArrayMember").asArray().size() == 2;
        assert ann.member("classArrayMember").asArray().get(0).isClass();
        assert ann.member("classArrayMember").asArray().get(0).asType().isClass();
        assert ann.member("classArrayMember").asArray().get(0).asType().asClass().declaration().name()
                .equals("org.jboss.cdi.lang.model.tck.SimpleAnnotation");
        assert ann.member("classArrayMember").asArray().get(1).isClass();
        assert ann.member("classArrayMember").asArray().get(1).asType().isClass();
        assert ann.member("classArrayMember").asArray().get(1).asType().asClass().declaration().name()
                .equals("org.jboss.cdi.lang.model.tck.SimpleEnum");

        // SimpleEnum[] enumArrayMember() default { SimpleEnum.YES, SimpleEnum.NO };
        assert ann.hasMember("enumArrayMember");
        assert ann.member("enumArrayMember").isArray();
        assert ann.member("enumArrayMember").asArray().size() == 2;
        assert ann.member("enumArrayMember").asArray().get(0).isEnum();
        assert ann.member("enumArrayMember").asArray().get(0).asEnum(SimpleEnum.class) == SimpleEnum.YES;
        assert ann.member("enumArrayMember").asArray().get(1).isEnum();
        assert ann.member("enumArrayMember").asArray().get(1).asEnumClass().name()
                .equals("org.jboss.cdi.lang.model.tck.SimpleEnum");
        assert ann.member("enumArrayMember").asArray().get(1).asEnumConstant().equals("NO");

        // SimpleAnnotation[] annotationArrayMember() default { @SimpleAnnotation("EEE"), @SimpleAnnotation("FFF") };
        assert ann.hasMember("annotationArrayMember");
        assert ann.member("annotationArrayMember").isArray();
        assert ann.member("annotationArrayMember").asArray().size() == 2;
        assert ann.member("annotationArrayMember").asArray().get(0).isNestedAnnotation();
        assert ann.member("annotationArrayMember").asArray().get(0).asNestedAnnotation().declaration().name()
                .equals("org.jboss.cdi.lang.model.tck.SimpleAnnotation");
        assert ann.member("annotationArrayMember").asArray().get(0).asNestedAnnotation().hasValue();
        assert ann.member("annotationArrayMember").asArray().get(0).asNestedAnnotation().value().isString();
        assert ann.member("annotationArrayMember").asArray().get(0).asNestedAnnotation().value().asString().equals("EEE");
        assert ann.member("annotationArrayMember").asArray().get(1).isNestedAnnotation();
        assert ann.member("annotationArrayMember").asArray().get(1).asNestedAnnotation().declaration().name()
                .equals("org.jboss.cdi.lang.model.tck.SimpleAnnotation");
        assert ann.member("annotationArrayMember").asArray().get(1).asNestedAnnotation().hasValue();
        assert ann.member("annotationArrayMember").asArray().get(1).asNestedAnnotation().value().isString();
        assert ann.member("annotationArrayMember").asArray().get(1).asNestedAnnotation().value().asString().equals("FFF");
    }

    private static void verifyNondefaultValues(ClassInfo clazz) {
        MethodInfo method = LangModelUtils.singleMethod(clazz, "nondefaultValues");
        assert method.annotations().size() == 1;
        assert method.annotations(it -> it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnotationMembers"))
                .size() == 1;
        assert method.annotations(it -> !it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnotationMembers"))
                .size() == 0;
        assert method.hasAnnotation(AnnotationMembers.class);
        assert method.hasAnnotation(it -> it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnotationMembers"));
        assert !method.hasAnnotation(it -> !it.declaration().name().equals("org.jboss.cdi.lang.model.tck.AnnotationMembers"));

        AnnotationInfo ann = method.annotation(AnnotationMembers.class);

        // booleanMember = false
        assert ann.hasMember("booleanMember");
        assert ann.member("booleanMember").isBoolean();
        assert !ann.member("booleanMember").asBoolean();

        // byteMember = -1
        assert ann.hasMember("byteMember");
        assert ann.member("byteMember").isByte();
        assert ann.member("byteMember").asByte() == -1;

        // shortMember = -2
        assert ann.hasMember("shortMember");
        assert ann.member("shortMember").isShort();
        assert ann.member("shortMember").asShort() == -2;

        // intMember = -3
        assert ann.hasMember("intMember");
        assert ann.member("intMember").isInt();
        assert ann.member("intMember").asInt() == -3;

        // longMember = -4
        assert ann.hasMember("longMember");
        assert ann.member("longMember").isLong();
        assert ann.member("longMember").asLong() == -4;

        // floatMember = -5.0F
        assert ann.hasMember("floatMember");
        assert ann.member("floatMember").isFloat();
        assert ann.member("floatMember").asFloat() == -5.0F;

        // doubleMember = -6.0
        assert ann.hasMember("doubleMember");
        assert ann.member("doubleMember").isDouble();
        assert ann.member("doubleMember").asDouble() == -6.0;

        // charMember = 'a'
        assert ann.hasMember("charMember");
        assert ann.member("charMember").isChar();
        assert ann.member("charMember").asChar() == 'a';

        // stringMember = "bb",
        assert ann.hasMember("stringMember");
        assert ann.member("stringMember").isString();
        assert ann.member("stringMember").asString().equals("bb");

        // classMember = AnnotationInstances.class
        assert ann.hasMember("classMember");
        assert ann.member("classMember").isClass();
        assert ann.member("classMember").asType().isClass();
        assert ann.member("classMember").asType().asClass().declaration().name()
                .equals("org.jboss.cdi.lang.model.tck.AnnotationInstances");

        // enumMember = SimpleEnum.NO
        assert ann.hasMember("enumMember");
        assert ann.member("enumMember").isEnum();
        assert ann.member("enumMember").asEnum(SimpleEnum.class) == SimpleEnum.NO;
        assert ann.member("enumMember").asEnumClass().name().equals("org.jboss.cdi.lang.model.tck.SimpleEnum");
        assert ann.member("enumMember").asEnumConstant().equals("NO");

        // annotationMember = @SimpleAnnotation("ccc"),
        assert ann.hasMember("annotationMember");
        assert ann.member("annotationMember").isNestedAnnotation();
        assert ann.member("annotationMember").asNestedAnnotation().declaration().name()
                .equals("org.jboss.cdi.lang.model.tck.SimpleAnnotation");
        assert ann.member("annotationMember").asNestedAnnotation().hasValue();
        assert ann.member("annotationMember").asNestedAnnotation().value().isString();
        assert ann.member("annotationMember").asNestedAnnotation().value().asString().equals("ccc");

        // booleanArrayMember = { false, true }
        assert ann.hasMember("booleanArrayMember");
        assert ann.member("booleanArrayMember").isArray();
        assert ann.member("booleanArrayMember").asArray().size() == 2;
        assert ann.member("booleanArrayMember").asArray().get(0).isBoolean();
        assert !ann.member("booleanArrayMember").asArray().get(0).asBoolean();
        assert ann.member("booleanArrayMember").asArray().get(1).isBoolean();
        assert ann.member("booleanArrayMember").asArray().get(1).asBoolean();

        // byteArrayMember = { -1, -2 }
        assert ann.hasMember("byteArrayMember");
        assert ann.member("byteArrayMember").isArray();
        assert ann.member("byteArrayMember").asArray().size() == 2;
        assert ann.member("byteArrayMember").asArray().get(0).isByte();
        assert ann.member("byteArrayMember").asArray().get(0).asByte() == -1;
        assert ann.member("byteArrayMember").asArray().get(1).isByte();
        assert ann.member("byteArrayMember").asArray().get(1).asByte() == -2;

        // shortArrayMember = { -3, -4 }
        assert ann.hasMember("shortArrayMember");
        assert ann.member("shortArrayMember").isArray();
        assert ann.member("shortArrayMember").asArray().size() == 2;
        assert ann.member("shortArrayMember").asArray().get(0).isShort();
        assert ann.member("shortArrayMember").asArray().get(0).asShort() == -3;
        assert ann.member("shortArrayMember").asArray().get(1).isShort();
        assert ann.member("shortArrayMember").asArray().get(1).asShort() == -4;

        // intArrayMember = { -5, -6 }
        assert ann.hasMember("intArrayMember");
        assert ann.member("intArrayMember").isArray();
        assert ann.member("intArrayMember").asArray().size() == 2;
        assert ann.member("intArrayMember").asArray().get(0).isInt();
        assert ann.member("intArrayMember").asArray().get(0).asInt() == -5;
        assert ann.member("intArrayMember").asArray().get(1).isInt();
        assert ann.member("intArrayMember").asArray().get(1).asInt() == -6;

        // longArrayMember = { -7, -8 }
        assert ann.hasMember("longArrayMember");
        assert ann.member("longArrayMember").isArray();
        assert ann.member("longArrayMember").asArray().size() == 2;
        assert ann.member("longArrayMember").asArray().get(0).isLong();
        assert ann.member("longArrayMember").asArray().get(0).asLong() == -7;
        assert ann.member("longArrayMember").asArray().get(1).isLong();
        assert ann.member("longArrayMember").asArray().get(1).asLong() == -8;

        // floatArrayMember = { -9.0F, -10.0F }
        assert ann.hasMember("floatArrayMember");
        assert ann.member("floatArrayMember").isArray();
        assert ann.member("floatArrayMember").asArray().size() == 2;
        assert ann.member("floatArrayMember").asArray().get(0).isFloat();
        assert ann.member("floatArrayMember").asArray().get(0).asFloat() == -9.0F;
        assert ann.member("floatArrayMember").asArray().get(1).isFloat();
        assert ann.member("floatArrayMember").asArray().get(1).asFloat() == -10.0F;

        // doubleArrayMember = { -11.0, -12.0 }
        assert ann.hasMember("doubleArrayMember");
        assert ann.member("doubleArrayMember").isArray();
        assert ann.member("doubleArrayMember").asArray().size() == 2;
        assert ann.member("doubleArrayMember").asArray().get(0).isDouble();
        assert ann.member("doubleArrayMember").asArray().get(0).asDouble() == -11.0;
        assert ann.member("doubleArrayMember").asArray().get(1).isDouble();
        assert ann.member("doubleArrayMember").asArray().get(1).asDouble() == -12.0;

        // charArrayMember = { 'a', 'b' }
        assert ann.hasMember("charArrayMember");
        assert ann.member("charArrayMember").isArray();
        assert ann.member("charArrayMember").asArray().size() == 2;
        assert ann.member("charArrayMember").asArray().get(0).isChar();
        assert ann.member("charArrayMember").asArray().get(0).asChar() == 'a';
        assert ann.member("charArrayMember").asArray().get(1).isChar();
        assert ann.member("charArrayMember").asArray().get(1).asChar() == 'b';

        // stringArrayMember = { "cc", "dd" },
        assert ann.hasMember("stringArrayMember");
        assert ann.member("stringArrayMember").isArray();
        assert ann.member("stringArrayMember").asArray().size() == 2;
        assert ann.member("stringArrayMember").asArray().get(0).isString();
        assert ann.member("stringArrayMember").asArray().get(0).asString().equals("cc");
        assert ann.member("stringArrayMember").asArray().get(1).isString();
        assert ann.member("stringArrayMember").asArray().get(1).asString().equals("dd");

        // classArrayMember = { AnnotationInstances.class, AnnotationMembers.class }
        assert ann.hasMember("classArrayMember");
        assert ann.member("classArrayMember").isArray();
        assert ann.member("classArrayMember").asArray().size() == 2;
        assert ann.member("classArrayMember").asArray().get(0).isClass();
        assert ann.member("classArrayMember").asArray().get(0).asType().isClass();
        assert ann.member("classArrayMember").asArray().get(0).asType().asClass().declaration().name()
                .equals("org.jboss.cdi.lang.model.tck.AnnotationInstances");
        assert ann.member("classArrayMember").asArray().get(1).isClass();
        assert ann.member("classArrayMember").asArray().get(1).asType().isClass();
        assert ann.member("classArrayMember").asArray().get(1).asType().asClass().declaration().name()
                .equals("org.jboss.cdi.lang.model.tck.AnnotationMembers");

        // enumArrayMember = { SimpleEnum.NO, SimpleEnum.YES }
        assert ann.hasMember("enumArrayMember");
        assert ann.member("enumArrayMember").isArray();
        assert ann.member("enumArrayMember").asArray().size() == 2;
        assert ann.member("enumArrayMember").asArray().get(0).isEnum();
        assert ann.member("enumArrayMember").asArray().get(0).asEnum(SimpleEnum.class) == SimpleEnum.NO;
        assert ann.member("enumArrayMember").asArray().get(1).isEnum();
        assert ann.member("enumArrayMember").asArray().get(1).asEnumClass().name()
                .equals("org.jboss.cdi.lang.model.tck.SimpleEnum");
        assert ann.member("enumArrayMember").asArray().get(1).asEnumConstant().equals("YES");

        /// annotationArrayMember = { @SimpleAnnotation("eee"), @SimpleAnnotation("fff") }
        assert ann.hasMember("annotationArrayMember");
        assert ann.member("annotationArrayMember").isArray();
        assert ann.member("annotationArrayMember").asArray().size() == 2;
        assert ann.member("annotationArrayMember").asArray().get(0).isNestedAnnotation();
        assert ann.member("annotationArrayMember").asArray().get(0).asNestedAnnotation().declaration().name()
                .equals("org.jboss.cdi.lang.model.tck.SimpleAnnotation");
        assert ann.member("annotationArrayMember").asArray().get(0).asNestedAnnotation().hasValue();
        assert ann.member("annotationArrayMember").asArray().get(0).asNestedAnnotation().value().isString();
        assert ann.member("annotationArrayMember").asArray().get(0).asNestedAnnotation().value().asString().equals("eee");
        assert ann.member("annotationArrayMember").asArray().get(1).isNestedAnnotation();
        assert ann.member("annotationArrayMember").asArray().get(1).asNestedAnnotation().declaration().name()
                .equals("org.jboss.cdi.lang.model.tck.SimpleAnnotation");
        assert ann.member("annotationArrayMember").asArray().get(1).asNestedAnnotation().hasValue();
        assert ann.member("annotationArrayMember").asArray().get(1).asNestedAnnotation().value().isString();
        assert ann.member("annotationArrayMember").asArray().get(1).asNestedAnnotation().value().asString().equals("fff");
    }
}
