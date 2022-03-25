package org.jboss.cdi.lang.model.tck;

import jakarta.enterprise.lang.model.declarations.ClassInfo;
import jakarta.enterprise.lang.model.declarations.PackageInfo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@interface SourceAnnotation {
}

@Retention(RetentionPolicy.CLASS)
@interface ClassAnnotation {
}

@Retention(RetentionPolicy.RUNTIME)
@interface RuntimeAnnotation {
}

@SourceAnnotation
@ClassAnnotation
@RuntimeAnnotation
public class LangModelVerifier {

    /**
     * The option allows to bypass the type annotation checks.
     * There is a bug in the annotation processor not providing type annotations for classes loaded from the classpath.
     * https://bugs.openjdk.java.net/browse/JDK-8225377
     */
    public static boolean RUN_TYPE_ANNOTATION_TESTS = true;

    AnnotatedTypes annotatedTypes;
    AnnotatedSuperTypes annotatedSuperTypes;
    AnnotatedThrowsTypes annotatedThrowsTypes;
    AnnotatedReceiverTypes annotatedReceiverTypes;
    AnnotationInstances annotationInstances;

    PlainClassMembers plainClassMembers;
    InterfaceMembers interfaceMembers;
    AnnotationMembers annotationMembers;
    EnumMembers enumMembers;
    // TODO records -- but how?

    InheritedMethods inheritedMethods;
    InheritedFields inheritedFields;
    InheritedAnnotations inheritedAnnotations;

    PrimitiveTypes primitiveTypes;
    BridgeMethods bridgeMethods;
    RepeatableAnnotations repeatableAnnotations;
    DefaultConstructors defaultConstructors;
    Equality equality;

    /**
     * To run the language model TCK, this method must be called with a {@code ClassInfo} object
     * representing the {@code LangModelVerifier} class. Assertions must be enabled. The language
     * model implementation under test must only return runtime-retained annotations.
     */
    public static void verify(ClassInfo clazz) {
        ensureAssertionsEnabled();
        ensureOnlyRuntimeAnnotations(clazz);

        // some classes have an extra Verifier nested class so that the verification methods
        // do not interfere with "regular" methods whose presence is tested

        AnnotatedTypes.verify(LangModelUtils.classOfField(clazz, "annotatedTypes"));
        AnnotatedSuperTypes.verify(LangModelUtils.classOfField(clazz, "annotatedSuperTypes"));
        AnnotatedThrowsTypes.verify(LangModelUtils.classOfField(clazz, "annotatedThrowsTypes"));
        AnnotatedReceiverTypes.verify(LangModelUtils.classOfField(clazz, "annotatedReceiverTypes"));
        AnnotationInstances.verify(LangModelUtils.classOfField(clazz, "annotationInstances"));

        PlainClassMembers.Verifier.verify(LangModelUtils.classOfField(clazz, "plainClassMembers"));
        InterfaceMembers.Verifier.verify(LangModelUtils.classOfField(clazz, "interfaceMembers"));
        AnnotationMembers.Verifier.verify(LangModelUtils.classOfField(clazz, "annotationMembers"));
        EnumMembers.Verifier.verify(LangModelUtils.classOfField(clazz, "enumMembers"));

        InheritedMethods.Verifier.verify(LangModelUtils.classOfField(clazz, "inheritedMethods"));
        InheritedFields.Verifier.verify(LangModelUtils.classOfField(clazz, "inheritedFields"));
        InheritedAnnotations.verify(LangModelUtils.classOfField(clazz, "inheritedAnnotations"));

        PrimitiveTypes.verify(LangModelUtils.classOfField(clazz, "primitiveTypes"));
        BridgeMethods.verify(LangModelUtils.classOfField(clazz, "bridgeMethods"));
        RepeatableAnnotations.verify(LangModelUtils.classOfField(clazz, "repeatableAnnotations"));
        DefaultConstructors.verify(LangModelUtils.classOfField(clazz, "defaultConstructors"));
        Equality.verify(LangModelUtils.classOfField(clazz, "equality"));

        verifyPackageAnnotation(clazz);

        System.out.println(LangModelVerifier.class.getSimpleName() + " succeeded");
    }

    private static void ensureAssertionsEnabled() {
        boolean assertionsEnabled = false;
        try {
            assert false;
        } catch (AssertionError ignored) {
            assertionsEnabled = true;
        }

        if (!assertionsEnabled) {
            throw new AssertionError("Assertions must be enabled to run the language model TCK");
        }
    }

    private static void ensureOnlyRuntimeAnnotations(ClassInfo clazz) {
        assert !clazz.hasAnnotation(SourceAnnotation.class);
        assert !clazz.hasAnnotation(ClassAnnotation.class);
        assert clazz.hasAnnotation(RuntimeAnnotation.class);
    }

    private static void verifyPackageAnnotation(ClassInfo clazz) {
        PackageInfo pkg = clazz.packageInfo();

        assert pkg.name().equals("org.jboss.cdi.lang.model.tck");
        assert pkg.annotations().size() == 1;
        assert pkg.hasAnnotation(AnnPackage.class);
        assert pkg.annotation(AnnPackage.class).hasValue();
        assert pkg.annotation(AnnPackage.class).value().isString();
        assert pkg.annotation(AnnPackage.class).value().asString().equals("lang-model-tck");
        assert pkg.repeatableAnnotation(AnnPackage.class).size() == 1;

        assert !pkg.hasAnnotation(MissingAnnotation.class);
        assert pkg.annotation(MissingAnnotation.class) == null;
        assert pkg.repeatableAnnotation(MissingAnnotation.class).isEmpty();
    }
}
