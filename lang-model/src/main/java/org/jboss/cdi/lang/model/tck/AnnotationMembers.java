package org.jboss.cdi.lang.model.tck;

import jakarta.enterprise.lang.model.declarations.ClassInfo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Modifier;

import static org.jboss.cdi.lang.model.tck.PlainClassMembers.Verifier.assertField;

@Retention(RetentionPolicy.RUNTIME)
@interface SimpleAnnotation {
    String value();
}

enum SimpleEnum {
    YES,
    NO,
}

@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationMembers {
    // fields

    public static final String publicStaticFinalField = "";
    public static String publicStaticField = "";
    public final String publicFinalField = "";
    public String publicField = "";

    static final String staticFinalField = "";
    static String staticField = "";
    final String finalField = "";
    String field = "";

    // methods

    boolean booleanMember() default true;
    byte byteMember() default 1;
    short shortMember() default 2;
    int intMember() default 3;
    long longMember() default 4;
    float floatMember() default 5.0F;
    double doubleMember() default 6.0;
    char charMember() default 'A';
    String stringMember() default "BB";
    Class<?> classMember() default AnnotationMembers.class;
    SimpleEnum enumMember() default SimpleEnum.YES;
    SimpleAnnotation annotationMember() default @SimpleAnnotation("CCC");

    boolean[] booleanArrayMember() default { true, false };
    byte[] byteArrayMember() default { 1, 2 };
    short[] shortArrayMember() default { 3, 4 };
    int[] intArrayMember() default { 5, 6 };
    long[] longArrayMember() default { 7, 8 };
    float[] floatArrayMember() default { 9.0F, 10.0F };
    double[] doubleArrayMember() default { 11.0, 12.0 };
    char[] charArrayMember() default { 'A', 'B' };
    String[] stringArrayMember() default { "CC", "DD" };
    Class<?>[] classArrayMember() default { SimpleAnnotation.class, SimpleEnum.class };
    SimpleEnum[] enumArrayMember() default { SimpleEnum.YES, SimpleEnum.NO };
    SimpleAnnotation[] annotationArrayMember() default { @SimpleAnnotation("EEE"), @SimpleAnnotation("FFF") };

    class Verifier {
        public static void verify(ClassInfo clazz) {
            assert clazz.name().equals("org.jboss.cdi.lang.model.tck.AnnotationMembers");
            assert clazz.simpleName().equals("AnnotationMembers");

            assert !clazz.isPlainClass();
            assert !clazz.isInterface();
            assert !clazz.isEnum();
            assert clazz.isAnnotation();
            assert !clazz.isRecord();

            assert clazz.isAbstract();
            assert !clazz.isFinal();

            assert Modifier.isPublic(clazz.modifiers());
            assert !Modifier.isProtected(clazz.modifiers());
            assert !Modifier.isPrivate(clazz.modifiers());

            verifyFields(clazz);
            verifyMethods(clazz);
            assert clazz.constructors().isEmpty();

            assert clazz.annotations().size() == 1;
            assert clazz.hasAnnotation(Retention.class);
        }

        private static void verifyFields(ClassInfo clazz) {
            assert clazz.fields().size() == 8; // no field declarations are inherited from java.lang.Annotation

            assertField(clazz, "publicStaticFinalField", Modifier.PUBLIC, true, true);
            assertField(clazz, "publicStaticField", Modifier.PUBLIC, true, true);
            assertField(clazz, "publicFinalField", Modifier.PUBLIC, true, true);
            assertField(clazz, "publicField", Modifier.PUBLIC, true, true);

            assertField(clazz, "staticFinalField", Modifier.PUBLIC, true, true);
            assertField(clazz, "staticField", Modifier.PUBLIC, true, true);
            assertField(clazz, "finalField", Modifier.PUBLIC, true, true);
            assertField(clazz, "field", Modifier.PUBLIC, true, true);
        }

        private static void verifyMethods(ClassInfo clazz) {
            assert clazz.methods().size() >= 24; // some method declarations are inherited from java.lang.Annotation

            // just verify that the methods can be found

            LangModelUtils.singleMethod(clazz, "booleanMember");
            LangModelUtils.singleMethod(clazz, "byteMember");
            LangModelUtils.singleMethod(clazz, "shortMember");
            LangModelUtils.singleMethod(clazz, "intMember");
            LangModelUtils.singleMethod(clazz, "longMember");
            LangModelUtils.singleMethod(clazz, "floatMember");
            LangModelUtils.singleMethod(clazz, "doubleMember");
            LangModelUtils.singleMethod(clazz, "charMember");
            LangModelUtils.singleMethod(clazz, "stringMember");
            LangModelUtils.singleMethod(clazz, "classMember");
            LangModelUtils.singleMethod(clazz, "enumMember");
            LangModelUtils.singleMethod(clazz, "annotationMember");

            LangModelUtils.singleMethod(clazz, "booleanArrayMember");
            LangModelUtils.singleMethod(clazz, "byteArrayMember");
            LangModelUtils.singleMethod(clazz, "shortArrayMember");
            LangModelUtils.singleMethod(clazz, "intArrayMember");
            LangModelUtils.singleMethod(clazz, "longArrayMember");
            LangModelUtils.singleMethod(clazz, "floatArrayMember");
            LangModelUtils.singleMethod(clazz, "doubleArrayMember");
            LangModelUtils.singleMethod(clazz, "charArrayMember");
            LangModelUtils.singleMethod(clazz, "stringArrayMember");
            LangModelUtils.singleMethod(clazz, "classArrayMember");
            LangModelUtils.singleMethod(clazz, "enumArrayMember");
            LangModelUtils.singleMethod(clazz, "annotationArrayMember");
        }
    }
}
