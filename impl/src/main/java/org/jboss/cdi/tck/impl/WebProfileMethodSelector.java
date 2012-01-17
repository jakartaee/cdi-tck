package org.jboss.cdi.tck.impl;

import java.util.Arrays;
import java.util.List;

import org.testng.IMethodSelector;
import org.testng.IMethodSelectorContext;
import org.testng.ITestNGMethod;

/**
 * 
 */
public class WebProfileMethodSelector implements IMethodSelector {

    private static final long serialVersionUID = 4868366080536037160L;

    public boolean includeMethod(IMethodSelectorContext ctx, ITestNGMethod method, boolean isTestMethod) {
        if (!isFullProfileOnly(method.getGroups())) {
            return true;
        } else {
            ctx.setStopped(true);
            return false;
        }
    }

    public void setTestMethods(List<ITestNGMethod> arg0) {
        // No-op, not needed
    }

    private boolean isFullProfileOnly(String[] groups) {
        return Arrays.asList(groups).contains("javaee-full-only");
    }

}
