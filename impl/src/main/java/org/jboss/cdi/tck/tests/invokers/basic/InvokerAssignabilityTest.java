/*
 * Copyright 2024, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.invokers.basic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.InvokerFactory;
import jakarta.enterprise.inject.build.compatible.spi.Registration;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import jakarta.enterprise.invoke.Invoker;
import jakarta.enterprise.lang.model.declarations.MethodInfo;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.invokers.InvokerHolder;
import org.jboss.cdi.tck.tests.invokers.InvokerHolderCreator;
import org.jboss.cdi.tck.tests.invokers.InvokerHolderExtensionBase;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.fail;

@SpecVersion(spec = "cdi", version = "4.1")
public class InvokerAssignabilityTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(InvokerAssignabilityTest.class)
                .withClasses(MyService.class)
                .withBuildCompatibleExtension(TestExtension.class)
                .withClasses(InvokerHolder.class, InvokerHolderCreator.class, InvokerHolderExtensionBase.class)
                .build();
    }

    public static class TestExtension extends InvokerHolderExtensionBase implements BuildCompatibleExtension {
        @Registration(types = MyService.class)
        public void myServiceRegistration(BeanInfo bean, InvokerFactory invokers) {
            for (MethodInfo method : bean.declaringClass().methods()) {
                registerInvoker(method.name(), invokers.createInvoker(bean, method).build());
            }
        }

        @Synthesis
        public void synthesis(SyntheticComponents syn) {
            synthesizeInvokerHolder(syn);
        }
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.BEHAVIOR_OF_INVOKE, id = "g")
    @SpecAssertion(section = Sections.BEHAVIOR_OF_INVOKE, id = "l")
    public void testNonNull(MyService service, InvokerHolder invokers) {
        assertOK(invokers, service, "helloBoolean", array(true));
        assertOK(invokers, service, "helloByte", array((byte) 1));
        assertOK(invokers, service, "helloShort", array((short) 1));
        assertOK(invokers, service, "helloInt", array(1));
        assertOK(invokers, service, "helloLong", array(1L));
        assertOK(invokers, service, "helloFloat", array(1.0F));
        assertOK(invokers, service, "helloDouble", array(1.0));
        assertOK(invokers, service, "helloChar", array('a'));

        assertOK(invokers, service, "helloShort", array((byte) 1));
        assertOK(invokers, service, "helloInt", array((byte) 1));
        assertOK(invokers, service, "helloInt", array((short) 1));
        assertOK(invokers, service, "helloLong", array((byte) 1));
        assertOK(invokers, service, "helloLong", array((short) 1));
        assertOK(invokers, service, "helloLong", array(1));
        assertOK(invokers, service, "helloFloat", array(1));
        assertOK(invokers, service, "helloFloat", array(1L));
        assertOK(invokers, service, "helloDouble", array(1));
        assertOK(invokers, service, "helloDouble", array(1L));
        assertOK(invokers, service, "helloDouble", array(1.0F));

        assertOK(invokers, service, "helloBooleanWrapper", array(true));
        assertOK(invokers, service, "helloByteWrapper", array((byte) 1));
        assertOK(invokers, service, "helloShortWrapper", array((short) 1));
        assertOK(invokers, service, "helloIntWrapper", array(1));
        assertOK(invokers, service, "helloLongWrapper", array(1L));
        assertOK(invokers, service, "helloFloatWrapper", array(1.0F));
        assertOK(invokers, service, "helloDoubleWrapper", array(1.0));
        assertOK(invokers, service, "helloCharWrapper", array('a'));
        assertOK(invokers, service, "helloString", array(""));
        assertOK(invokers, service, "helloCollection", array(List.of()));
        assertOK(invokers, service, "helloCollection", array(Set.of()));
        assertOK(invokers, service, "helloSerializable", array(1)); // Number is Serializable
        assertOK(invokers, service, "helloSerializable", array(new Object[]{}));
        assertOK(invokers, service, "helloSerializable", array(new String[]{}));
        assertOK(invokers, service, "helloObject", array(""));
        assertOK(invokers, service, "helloObject", array(List.of()));
        assertOK(invokers, service, "helloObject", array(new Object()));
        assertOK(invokers, service, "helloObject", array(new int[]{}));
        assertOK(invokers, service, "helloObject", array(new String[]{}));
        assertOK(invokers, service, "helloObject", array(new List<?>[]{}));
        assertOK(invokers, service, "helloObject", array(new Object[]{}));

        assertOK(invokers, service, "helloBooleanArray", array(new boolean[]{true}));
        assertOK(invokers, service, "helloByteArray", array(new byte[]{(byte) 1}));
        assertOK(invokers, service, "helloShortArray", array(new short[]{(short) 1}));
        assertOK(invokers, service, "helloIntArray", array(new int[]{1}));
        assertOK(invokers, service, "helloLongArray", array(new long[]{1L}));
        assertOK(invokers, service, "helloFloatArray", array(new float[]{1.0F}));
        assertOK(invokers, service, "helloDoubleArray", array(new double[]{1.0}));
        assertOK(invokers, service, "helloCharArray", array(new char[]{'a'}));
        assertOK(invokers, service, "helloStringArray", array(new String[]{}));
        assertOK(invokers, service, "helloObjectArray", array(new String[]{}));
        assertOK(invokers, service, "helloObjectArray", array(new Object[]{}));

        assertOK(invokers, service, "helloCollectionArrayArray", array(new List<?>[][]{}));
        assertOK(invokers, service, "helloCollectionArrayArray", array(new Set<?>[][]{}));
        assertOK(invokers, service, "helloObjectArrayArray", array(new List<?>[][]{}));
        assertOK(invokers, service, "helloObjectArrayArray", array(new Set<?>[][]{}));
        assertOK(invokers, service, "helloObjectArrayArray", array(new String[][]{}));
        assertOK(invokers, service, "helloObjectArrayArray", array(new Object[][]{}));
        assertOK(invokers, service, "helloObjectArrayArray", array(new Object[][][]{}));

        assertFail(invokers, service, "helloBoolean", array(1));
        assertFail(invokers, service, "helloByte", array(1));
        assertFail(invokers, service, "helloShort", array(1));
        assertFail(invokers, service, "helloInt", array(1L));
        assertFail(invokers, service, "helloInt", array(1.0));
        assertFail(invokers, service, "helloLong", array(1.0F));
        assertFail(invokers, service, "helloLong", array(1.0));
        assertFail(invokers, service, "helloFloat", array(1.0));
        assertFail(invokers, service, "helloChar", array(false));
        assertFail(invokers, service, "helloChar", array((byte) 1));
        assertFail(invokers, service, "helloChar", array((short) 1));
        assertFail(invokers, service, "helloChar", array(1));
        assertFail(invokers, service, "helloChar", array(1L));
        assertFail(invokers, service, "helloChar", array(1.0F));
        assertFail(invokers, service, "helloChar", array(1.0));
        assertFail(invokers, service, "helloLong", array(new BigInteger("1")));
        assertFail(invokers, service, "helloDouble", array(new BigInteger("1")));
        assertFail(invokers, service, "helloDouble", array(new BigDecimal("1.0")));

        assertFail(invokers, service, "helloBooleanWrapper", array(1));
        assertFail(invokers, service, "helloByteWrapper", array(1));
        assertFail(invokers, service, "helloShortWrapper", array((byte) 1));
        assertFail(invokers, service, "helloShortWrapper", array(1));
        assertFail(invokers, service, "helloIntWrapper", array((short) 1));
        assertFail(invokers, service, "helloIntWrapper", array(1L));
        assertFail(invokers, service, "helloLongWrapper", array(1));
        assertFail(invokers, service, "helloLongWrapper", array(1.0));
        assertFail(invokers, service, "helloFloatWrapper", array(1));
        assertFail(invokers, service, "helloFloatWrapper", array(1L));
        assertFail(invokers, service, "helloFloatWrapper", array(1.0));
        assertFail(invokers, service, "helloDoubleWrapper", array(1));
        assertFail(invokers, service, "helloDoubleWrapper", array(1L));
        assertFail(invokers, service, "helloDoubleWrapper", array(1.0F));
        assertFail(invokers, service, "helloCharWrapper", array((byte) 1));
        assertFail(invokers, service, "helloCharWrapper", array(1));
        assertFail(invokers, service, "helloCharWrapper", array(1L));
        assertFail(invokers, service, "helloCharWrapper", array(1.0F));
        assertFail(invokers, service, "helloCharWrapper", array(1.0));
        assertFail(invokers, service, "helloLongWrapper", array(new BigInteger("1")));
        assertFail(invokers, service, "helloDoubleWrapper", array(new BigInteger("1")));
        assertFail(invokers, service, "helloDoubleWrapper", array(new BigDecimal("1.0")));
        assertFail(invokers, service, "helloString", array(new Object()));
        assertFail(invokers, service, "helloCollection", array(1));
        assertFail(invokers, service, "helloCollection", array(new Object()));
        assertFail(invokers, service, "helloSerializable", array(new Object()));

        assertFail(invokers, service, "helloBooleanArray", array(new int[]{1}));
        assertFail(invokers, service, "helloByteArray", array(new int[]{1}));
        assertFail(invokers, service, "helloShortArray", array(new int[]{1}));
        assertFail(invokers, service, "helloIntArray", array(new long[]{1L}));
        assertFail(invokers, service, "helloLongArray", array(new int[]{1}));
        assertFail(invokers, service, "helloFloatArray", array(new double[]{1.0}));
        assertFail(invokers, service, "helloDoubleArray", array(new float[]{1.0F}));
        assertFail(invokers, service, "helloCharArray", array(new int[]{1}));
        assertFail(invokers, service, "helloStringArray", array(""));
        assertFail(invokers, service, "helloStringArray", array(new Object()));
        assertFail(invokers, service, "helloStringArray", array(new String[][]{}));
        assertFail(invokers, service, "helloStringArray", array(new Object[]{}));
        assertFail(invokers, service, "helloObjectArray", array(""));
        assertFail(invokers, service, "helloObjectArray", array(new Object()));

        assertFail(invokers, service, "helloCollectionArrayArray", array(new List<?>[]{}));
        assertFail(invokers, service, "helloCollectionArrayArray", array(new Set<?>[]{}));
        assertFail(invokers, service, "helloCollectionArrayArray", array(new List<?>[][][]{}));
        assertFail(invokers, service, "helloCollectionArrayArray", array(new Set<?>[][][]{}));
        assertFail(invokers, service, "helloCollectionArrayArray", array(new Object[][]{}));
        assertFail(invokers, service, "helloObjectArrayArray", array(new Object[]{}));
        assertFail(invokers, service, "helloObjectArrayArray", array(new Object()));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.BEHAVIOR_OF_INVOKE, id = "g")
    @SpecAssertion(section = Sections.BEHAVIOR_OF_INVOKE, id = "l")
    public void testNull(MyService service, InvokerHolder invokers) {
        assertOK(invokers, service, "helloBooleanWrapper", array(null));
        assertOK(invokers, service, "helloByteWrapper", array(null));
        assertOK(invokers, service, "helloShortWrapper", array(null));
        assertOK(invokers, service, "helloIntWrapper", array(null));
        assertOK(invokers, service, "helloLongWrapper", array(null));
        assertOK(invokers, service, "helloFloatWrapper", array(null));
        assertOK(invokers, service, "helloDoubleWrapper", array(null));
        assertOK(invokers, service, "helloCharWrapper", array(null));
        assertOK(invokers, service, "helloString", array(null));
        assertOK(invokers, service, "helloCollection", array(null));
        assertOK(invokers, service, "helloSerializable", array(null));
        assertOK(invokers, service, "helloObject", array(null));

        assertOK(invokers, service, "helloBooleanArray", array(null));
        assertOK(invokers, service, "helloByteArray", array(null));
        assertOK(invokers, service, "helloShortArray", array(null));
        assertOK(invokers, service, "helloIntArray", array(null));
        assertOK(invokers, service, "helloLongArray", array(null));
        assertOK(invokers, service, "helloFloatArray", array(null));
        assertOK(invokers, service, "helloDoubleArray", array(null));
        assertOK(invokers, service, "helloCharArray", array(null));
        assertOK(invokers, service, "helloStringArray", array(null));
        assertOK(invokers, service, "helloObjectArray", array(null));

        assertOK(invokers, service, "helloCollectionArrayArray", array(null));
        assertOK(invokers, service, "helloObjectArrayArray", array(null));

        assertFail(invokers, service, "helloBoolean", array(null));
        assertFail(invokers, service, "helloByte", array(null));
        assertFail(invokers, service, "helloShort", array(null));
        assertFail(invokers, service, "helloInt", array(null));
        assertFail(invokers, service, "helloLong", array(null));
        assertFail(invokers, service, "helloFloat", array(null));
        assertFail(invokers, service, "helloDouble", array(null));
        assertFail(invokers, service, "helloChar", array(null));
    }

    // produces a single-element array whose only element is `obj`
    private static Object[] array(Object obj) {
        return new Object[]{obj};
    }

    private static void assertOK(InvokerHolder invokers, MyService instance, String methodName, Object[] arguments) {
        Invoker<MyService, String> invoker = invokers.get(methodName);
        String result = null;
        try {
            result = invoker.invoke(instance, arguments);
        } catch (Exception e) {
            fail("unexpected exception", e);
        }
        assertEquals("OK", result);
    }

    private static void assertFail(InvokerHolder invokers, MyService instance, String methodName, Object[] arguments) {
        Invoker<MyService, String> invoker = invokers.get(methodName);
        assertThrows(RuntimeException.class, () -> invoker.invoke(instance, arguments));
    }

    @ApplicationScoped
    public static class MyService {
        public String helloBoolean(boolean b) {
            return "OK";
        }

        public String helloByte(byte b) {
            return "OK";
        }

        public String helloShort(short s) {
            return "OK";
        }

        public String helloInt(int i) {
            return "OK";
        }

        public String helloLong(long l) {
            return "OK";
        }

        public String helloFloat(float f) {
            return "OK";
        }

        public String helloDouble(double d) {
            return "OK";
        }

        public String helloChar(char ch) {
            return "OK";
        }

        public String helloBooleanWrapper(Boolean b) {
            return "OK";
        }

        public String helloByteWrapper(Byte b) {
            return "OK";
        }

        public String helloShortWrapper(Short s) {
            return "OK";
        }

        public String helloIntWrapper(Integer i) {
            return "OK";
        }

        public String helloLongWrapper(Long l) {
            return "OK";
        }

        public String helloFloatWrapper(Float f) {
            return "OK";
        }

        public String helloDoubleWrapper(Double d) {
            return "OK";
        }

        public String helloCharWrapper(Character ch) {
            return "OK";
        }

        public String helloString(String s) {
            return "OK";
        }

        public String helloCollection(Collection<String> c) {
            return "OK";
        }

        public String helloSerializable(Serializable s) {
            return "OK";
        }

        public String helloObject(Object o) {
            return "OK";
        }

        public String helloBooleanArray(boolean[] b) {
            return "OK";
        }

        public String helloByteArray(byte[] b) {
            return "OK";
        }

        public String helloShortArray(short[] s) {
            return "OK";
        }

        public String helloIntArray(int[] i) {
            return "OK";
        }

        public String helloLongArray(long[] l) {
            return "OK";
        }

        public String helloFloatArray(float[] f) {
            return "OK";
        }

        public String helloDoubleArray(double[] d) {
            return "OK";
        }

        public String helloCharArray(char[] ch) {
            return "OK";
        }

        public String helloStringArray(String[] s) {
            return "OK";
        }

        public String helloObjectArray(Object[] o) {
            return "OK";
        }

        public String helloCollectionArrayArray(Collection<?>[][] c) {
            return "OK";
        }

        public String helloObjectArrayArray(Object[][] o) {
            return "OK";
        }
    }
}
