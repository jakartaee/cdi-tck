/*
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.shrinkwrap.descriptors.ejb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.cdi.tck.shrinkwrap.ArchiveBuilder;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.ejbjar31.ActivationConfigType;
import org.jboss.shrinkwrap.descriptor.api.ejbjar31.EjbJarDescriptor;
import org.jboss.shrinkwrap.descriptor.api.ejbjar31.EnterpriseBeansType;
import org.jboss.shrinkwrap.descriptor.api.ejbjar31.MessageDrivenBeanType;

/**
 * Simple {@link EjbJarDescriptor} builder - intended only to avoid complexity when configuring message driven beans.
 *
 * @author Martin Kouba
 */
public class EjbJarDescriptorBuilder {

    private List<MessageDriven> messageDrivenBeans;

    public EjbJarDescriptorBuilder messageDrivenBeans(MessageDriven... beans) {
        if (beans != null && beans.length > 0) {
            messageDrivenBeans = Arrays.asList(beans);
        }
        return this;
    }

    public EjbJarDescriptor build() {

        EjbJarDescriptor ejbJarDescriptor = Descriptors.create(EjbJarDescriptor.class);
        ejbJarDescriptor.version(ArchiveBuilder.DEFAULT_EJB_VERSION);
        EnterpriseBeansType<EjbJarDescriptor> enterpriseBeansType = ejbJarDescriptor.getOrCreateEnterpriseBeans();

        if (messageDrivenBeans != null && !messageDrivenBeans.isEmpty()) {
            for (MessageDriven messageDriven : messageDrivenBeans) {
                MessageDrivenBeanType<EnterpriseBeansType<EjbJarDescriptor>> messageDrivenBeanType = enterpriseBeansType
                        .createMessageDriven();
                messageDrivenBeanType.ejbName(messageDriven.getEjbName());
                messageDrivenBeanType.ejbClass(messageDriven.getEjbClass());
                ActivationConfigType<MessageDrivenBeanType<EnterpriseBeansType<EjbJarDescriptor>>> activationConfig = messageDrivenBeanType
                        .getOrCreateActivationConfig();

                for (ActivationConfigProperty activationConfigProperty : messageDriven.getActivationConfigProperties()) {
                    activationConfig.createActivationConfigProperty()
                            .activationConfigPropertyName(activationConfigProperty.getName())
                            .activationConfigPropertyValue(activationConfigProperty.getValue());
                }
            }
        }
        return ejbJarDescriptor;
    }

    public static class MessageDriven {

        private String ejbName;

        private String ejbClass;

        private List<ActivationConfigProperty> activationConfigProperties = new ArrayList<EjbJarDescriptorBuilder.ActivationConfigProperty>();

        public MessageDriven(String ejbName, String ejbClass) {
            super();
            this.ejbName = ejbName;
            this.ejbClass = ejbClass;
        }

        public MessageDriven addActivationConfigProperty(String name, String value) {
            activationConfigProperties.add(new ActivationConfigProperty(name, value));
            return this;
        }

        public String getEjbName() {
            return ejbName;
        }

        public String getEjbClass() {
            return ejbClass;
        }

        public List<ActivationConfigProperty> getActivationConfigProperties() {
            return activationConfigProperties;
        }

        public static MessageDriven newMessageDriven(String ejbName, String ejbClass) {
            return new MessageDriven(ejbName, ejbClass);
        }

    }

    public static class ActivationConfigProperty {

        private String name;

        private String value;

        public ActivationConfigProperty(String name, String value) {
            super();
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

    }

}
