package org.jboss.cdi.tck.test.spi.container;

import java.io.PrintWriter;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;


import static javax.lang.model.SourceVersion.RELEASE_11;

@SupportedAnnotationTypes({
        "org.jboss.cdi.tck.test.spi.sp.VersionInfo",
        "org.jboss.cdi.tck.test.spi.broken.BadBean"
})
@SupportedSourceVersion(RELEASE_11)
//@SupportedOptions({CoverageProcessor.OUTDIR_OPTION_FLAG})
public class TckAnnotationProcessor extends AbstractProcessor {
    public static final String VERSION_PROCESSOR_PROPERTY = "org.jboss.cdi.tck.test.spi.container.VersionProcessor.ran";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        System.out.printf("Options: %s\n", processingEnv.getOptions());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.printf("VersionProcessor.process, %s\n", annotations);
        boolean foundVersionInfo = false;
        for (TypeElement annotation : annotations) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            for(Element element : elements) {
                if(element instanceof TypeElement) {
                    TypeElement typeElement = (TypeElement) element;
                    System.out.printf("Class(%s) is annotated with %s\n", typeElement.getSimpleName(), annotation.getQualifiedName());
                    if(annotation.getQualifiedName().contentEquals("org.jboss.cdi.tck.test.spi.broken.BadBean")) {
                        // Generate an error
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Found a BadBean", element);
                    }
                }
                else if(element instanceof VariableElement) {
                    VariableElement varElement = (VariableElement) element;
                    System.out.printf("Class(%s).%s is annotated with %s\n", varElement.getEnclosingElement().getSimpleName(), varElement.getSimpleName(), annotation.getQualifiedName());
                    if(annotation.getQualifiedName().contentEquals("org.jboss.cdi.tck.test.spi.sp.VersionInfo")) {
                        foundVersionInfo = true;
                        TypeElement enclosingClass = (TypeElement) varElement.getEnclosingElement();
                        String pkg = enclosingClass.getQualifiedName().toString();
                        pkg = pkg.substring(0, pkg.length() - enclosingClass.getSimpleName().length()-1);
                        String versionSetterClass = pkg + ".VersionSetter";
                        try {
                            JavaFileObject setterFile = processingEnv.getFiler().createSourceFile(versionSetterClass);
                            PrintWriter writer = new PrintWriter(setterFile.openWriter());
                            writer.println("package "+pkg+";");
                            writer.println("public class VersionSetter {");
                                writer.println("  static {");
                                    writer.print("    "+enclosingClass.getQualifiedName().toString()+"."+varElement.getSimpleName());
                                        writer.println(" = \"2.0\";");
                                writer.println("}");
                            writer.println("}");
                            writer.close();
                            System.setProperty(VERSION_PROCESSOR_PROPERTY, "true");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return foundVersionInfo;
    }
}
