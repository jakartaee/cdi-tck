package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBean;

public class MyPojo {
    final String text;
    final MyComplexValue ann;

    MyPojo(String text, MyComplexValue ann) {
        this.text = text;
        this.ann = ann;
    }
}
