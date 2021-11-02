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
    AnnotatedTypes annotatedTypes;
    AnnotatedSuperTypes annotatedSuperTypes;
    AnnotationInstances annotationInstances;

    PlainClassMembers plainClassMembers;
    InterfaceMembers interfaceMembers;
    AnnotationMembers annotationMembers;
    EnumMembers enumMembers;
    // TODO records -- but how?

    PrimitiveTypes primitiveTypes;

    InheritedMethods inheritedMethods;
    InheritedFields inheritedFields;

    /**
     * To run the language model TCK, this method must be called with a {@code ClassInfo} object
     * representing the {@code LangModelVerifier} class. Assertions must be enabled. The language
     * model implementation under test must only return runtime-retained annotations.
     */
    public static void verify(ClassInfo clazz) {
        ensureAssertionsEnabled();
        ensureOnlyRuntimeAnnotations(clazz);

        // some classes have an extra Verifier nested class so that verification methods do not interfere
        // with "regular" methods whose presence is tested

        AnnotatedTypes.verify(classOfField(clazz, "annotatedTypes"));
        AnnotatedSuperTypes.verify(classOfField(clazz, "annotatedSuperTypes"));
        AnnotationInstances.verify(classOfField(clazz, "annotationInstances"));

        PlainClassMembers.Verifier.verify(classOfField(clazz, "plainClassMembers"));
        InterfaceMembers.Verifier.verify(classOfField(clazz, "interfaceMembers"));
        AnnotationMembers.Verifier.verify(classOfField(clazz, "annotationMembers"));
        EnumMembers.Verifier.verify(classOfField(clazz, "enumMembers"));

        PrimitiveTypes.verify(classOfField(clazz, "primitiveTypes"));

        InheritedMethods.Verifier.verify(classOfField(clazz, "inheritedMethods"));
        InheritedFields.Verifier.verify(classOfField(clazz, "inheritedFields"));

        verifyPackageAnnotation(clazz);
    }

    private static ClassInfo classOfField(ClassInfo clazz, String fieldName) {
        return LangModelUtils.singleField(clazz, fieldName).type().asClass().declaration();
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
    }
}
