/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.configurators.beanAttributes;

import static org.jboss.cdi.tck.cdi.Sections.BEAN_ATTRIBUTES_CONFIGURATOR;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_BEAN_ATTRIBUTES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanAttributes;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@Test
@SpecVersion(spec = "cdi", version = "2.0-PFD")
public class BeanAttributesConfiguratorTest extends AbstractTest {

    public static final String SWORD_NAME = "Frostmourne";

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BeanAttributesConfiguratorTest.class)
                .withExtension(ProcessBeanAttributesObserver.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).getOrCreateAlternatives()
                                .clazz(Axe.class.getName()).up())
                .build();
    }

    @Test
    @SpecAssertions({
            @SpecAssertion(section = BEAN_ATTRIBUTES_CONFIGURATOR, id = "a"),
            @SpecAssertion(section = BEAN_ATTRIBUTES_CONFIGURATOR, id = "ba"),
            @SpecAssertion(section = BEAN_ATTRIBUTES_CONFIGURATOR, id = "bd"),
            @SpecAssertion(section = BEAN_ATTRIBUTES_CONFIGURATOR, id = "bf"),
            @SpecAssertion(section = BEAN_ATTRIBUTES_CONFIGURATOR, id = "bh") })
    public void testSingleAdditionMethods() {
        Bean<Sword> bean = getUniqueBean(Sword.class, TwoHanded.TwoHandedLiteral.INSTANCE);

        assertTrue(bean.getQualifiers().contains(TwoHanded.TwoHandedLiteral.INSTANCE));
        assertTrue(bean.getName().equals(SWORD_NAME));
        assertTrue(bean.getTypes().contains(Weapon.class));
        assertTrue(bean.getStereotypes().contains(Equipment.class));
    }

    @Test
    @SpecAssertions({
            @SpecAssertion(section = BEAN_ATTRIBUTES_CONFIGURATOR, id = "a"),
            @SpecAssertion(section = BEAN_ATTRIBUTES_CONFIGURATOR, id = "bb"),
            @SpecAssertion(section = BEAN_ATTRIBUTES_CONFIGURATOR, id = "bc"),
            @SpecAssertion(section = BEAN_ATTRIBUTES_CONFIGURATOR, id = "be"),
            @SpecAssertion(section = BEAN_ATTRIBUTES_CONFIGURATOR, id = "bg"),
            @SpecAssertion(section = BEAN_ATTRIBUTES_CONFIGURATOR, id = "bi") })
    public void testMultiAdditionMethods() {
        Bean<Axe> bean = getUniqueBean(Axe.class, TwoHanded.TwoHandedLiteral.INSTANCE, Reforged.ReforgedLiteral.INSTANCE);

        Set<Annotation> qualifiers = bean.getQualifiers();
        Set<Class<? extends Annotation>> stereotypes = bean.getStereotypes();
        Set<Type> types = bean.getTypes();

        assertTrue(bean.getScope().equals(RequestScoped.class));
        assertTrue(qualifiers.containsAll(new HashSet<>(Arrays.asList(Reforged.ReforgedLiteral.INSTANCE, TwoHanded.TwoHandedLiteral.INSTANCE))));
        assertTrue(stereotypes.containsAll(new HashSet<>(Arrays.asList(Melee.class, Equipment.class))));
        assertTrue(types.containsAll(new HashSet<>(Arrays.asList(Weapon.class, Tool.class))));
        assertTrue(bean.isAlternative());
    }

    @Test
    @SpecAssertions({
            @SpecAssertion(section = BEAN_ATTRIBUTES_CONFIGURATOR, id = "a"),
            @SpecAssertion(section = BEAN_ATTRIBUTES_CONFIGURATOR, id = "ba"),
            @SpecAssertion(section = BEAN_ATTRIBUTES_CONFIGURATOR, id = "be"),
            @SpecAssertion(section = BEAN_ATTRIBUTES_CONFIGURATOR, id = "bg") })
    public void testReplacementMethods() {
        Bean<Hoe> bean = getUniqueBean(Hoe.class, Reforged.ReforgedLiteral.INSTANCE);

        Set<Type> types = bean.getTypes();
        Set<Class<? extends Annotation>> stereotypes = bean.getStereotypes();

        // list will contain the replaced Qualifier + Any
        assertTrue(bean.getQualifiers().equals(new HashSet<>(Arrays.asList(Reforged.ReforgedLiteral.INSTANCE, Any.Literal.INSTANCE))));
        assertTrue(types.containsAll(new HashSet<>(Arrays.asList(Tool.class, UsableItem.class))));
        assertTrue(stereotypes.equals(new HashSet<>(Arrays.asList(Equipment.class))));
    }

    @Test
    @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "bca")
    public void configuratorInitializedWithOriginalBeanAttributes() {
        Bean<Mace> configuredBean = getUniqueBean(Mace.class);
        BeanAttributes<Mace> originalBA = getCurrentManager().getExtension(ProcessBeanAttributesObserver.class).getOriginalBA();
        assertEquals(configuredBean.getTypes(), originalBA.getTypes());
        assertEquals(configuredBean.getQualifiers(), originalBA.getQualifiers());
        assertEquals(configuredBean.getStereotypes(), originalBA.getStereotypes());
        assertEquals(configuredBean.getScope(), originalBA.getScope());
    }
}
