/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.cdi.tck.tests.deployment.initialization;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

/**
 * 
 * @author Martin Kouba
 */
public class LifecycleMonitoringExtension implements Extension {

    public static long createdAt = 0l;

    public static long beforeBeanDiscoveryObservedAt = 0l;

    public static long afterBeanDiscoveryObservedAt = 0l;

    public static long afterDeploymentValidationObservedAt = 0l;

    public static long beanDiscoveryObservedAt = 0l;

    public LifecycleMonitoringExtension() {
        createdAt = System.currentTimeMillis();
    }

    public void observeBeforeBeanDiscovery(@Observes BeforeBeanDiscovery event) {
        beforeBeanDiscoveryObservedAt = System.currentTimeMillis();
    }

    public void observeAfterBeanDiscovery(@Observes AfterBeanDiscovery event) {
        afterBeanDiscoveryObservedAt = System.currentTimeMillis();
    }

    public void observeAfterDeploymentValidation(@Observes AfterDeploymentValidation event) {
        afterDeploymentValidationObservedAt = System.currentTimeMillis();
    }

    public void observeBeanDiscovery(@Observes ProcessAnnotatedType<Foo> event) {
        // We only need to check bean discovery occured in specific sequence
        beanDiscoveryObservedAt = System.currentTimeMillis();
    }

}
