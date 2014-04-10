/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.lifecycle.events;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.*;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.*;
import static org.testng.Assert.fail;

/**
 * This test was originally part of the Weld test suite.
 *
 * @author Jozef Hartinger
 * @author Tomas Remes
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class ContainerLifeCycleEventRuntimeInvocationTest extends AbstractTest {

    @Inject
    TestExtension extension;

    @Inject
    BeanManager beanManager;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(ContainerLifeCycleEventRuntimeInvocationTest.class)
                .withClasses(TestExtension.class, SimpleAnnotation.class, SimpleBean.class, SessionBean.class).withExtension(TestExtension.class)
                .build();
    }

    @Test
    @SpecAssertions({@SpecAssertion(section = BBD, id = "b")})
    public void testBeforeBeanDiscoveryEventFails() {
        final BeforeBeanDiscovery event = extension.getBeforeBeanDiscovery();
        final AnnotatedType<?> type = beanManager.createAnnotatedType(ContainerLifeCycleEventRuntimeInvocationTest.class);
        final AnnotatedType<? extends Annotation> annotation = beanManager.createAnnotatedType(SimpleAnnotation.class);

        new Invocation() {
            void execute() {
                event.addAnnotatedType(type);
            }
        }.run();
        new Invocation() {
            void execute() {
                event.addAnnotatedType(type, "foo");
            }
        }.run();
        new Invocation() {
            void execute() {
                event.addInterceptorBinding(SimpleAnnotation.class);
            }
        }.run();
        new Invocation() {
            void execute() {
                event.addInterceptorBinding(annotation);
            }
        }.run();
        new Invocation() {
            void execute() {
                event.addQualifier(SimpleAnnotation.class);
            }
        }.run();
        new Invocation() {
            void execute() {
                event.addQualifier(annotation);
            }
        }.run();
        new Invocation() {
            void execute() {
                event.addScope(SimpleAnnotation.class, true, false);
            }
        }.run();
        new Invocation() {
            void execute() {
                event.addStereotype(SimpleAnnotation.class);
            }
        }.run();

    }

    @Test
    @SpecAssertions({@SpecAssertion(section = ATD, id = "j")})
    public void testAfterTypeDiscoveryEventFails() {

        final AfterTypeDiscovery event = extension.getAfterTypeDiscovery();
        final AnnotatedType<?> type = beanManager.createAnnotatedType(ContainerLifeCycleEventRuntimeInvocationTest.class);
        new Invocation() {
            void execute() {
                event.addAnnotatedType(type, "bar");
            }
        }.run();
        new Invocation() {
            void execute() {
                event.getAlternatives();
            }
        }.run();
        new Invocation() {
            void execute() {
                event.getDecorators();
            }
        }.run();
        new Invocation() {
            void execute() {
                event.getInterceptors();
            }
        }.run();
    }

    @Test
    @SpecAssertions({@SpecAssertion(section = ABD, id = "h")})
    public void testAfterBeanDiscoveryEventFails() {

        final AfterBeanDiscovery event = extension.getAfterBeanDiscovery();
        new Invocation() {
            void execute() {
                event.addBean(beanManager.getBeans(SimpleBean.class).iterator().next());
            }
        }.run();
        new Invocation() {
            void execute() {
                event.addContext(beanManager.getContext(SessionScoped.class));
            }
        }.run();
        new Invocation() {
            void execute() {
                event.addObserverMethod(extension.getProcessObserverMethod().getObserverMethod());
            }
        }.run();
        new Invocation() {
            void execute() {
                event.getAnnotatedType(ContainerLifeCycleEventRuntimeInvocationTest.class, "foo");
            }
        }.run();
        new Invocation() {
            void execute() {
                event.getAnnotatedTypes(ContainerLifeCycleEventRuntimeInvocationTest.class);
            }
        }.run();
        new Invocation() {
            void execute() {
                event.addDefinitionError(new NullPointerException());
            }
        }.run();

    }

    @Test
    @SpecAssertions({@SpecAssertion(section = ADV, id = "e")})
    public void testAfterDeploymentValidationEventFails() {
        final AfterDeploymentValidation event = extension.getAfterDeploymentValidation();
        new Invocation() {
            void execute() {
                event.addDeploymentProblem(new NullPointerException());
            }
        }.run();
    }

    @Test
    @SpecAssertions({@SpecAssertion(section = PAT, id = "f")})
    public void testProcessAnnotatedTypeEventFails() {
        final ProcessAnnotatedType<SimpleBean> event = extension.getProcessAnnotatedType();
        final AnnotatedType<SimpleBean> type = beanManager.createAnnotatedType(SimpleBean.class);
        new Invocation() {
            void execute() {
                event.getAnnotatedType();
            }
        }.run();

        new Invocation() {
            void execute() {
                event.setAnnotatedType(type);
            }
        }.run();
    }

    @Test
    @SpecAssertions({@SpecAssertion(section = PAT, id = "f")})
    public void testProcessSyntheticAnnotatedTypeEventFails() {
        final ProcessSyntheticAnnotatedType<SimpleBean> event = extension.getProcessSyntheticAnnotatedType();
        final AnnotatedType<SimpleBean> type = beanManager.createAnnotatedType(SimpleBean.class);
        new Invocation() {
            void execute() {
                event.getAnnotatedType();
            }
        }.run();

        new Invocation() {
            void execute() {
                event.getSource();
            }
        }.run();

        new Invocation() {
            void execute() {
                event.setAnnotatedType(type);
            }
        }.run();

        new Invocation() {
            void execute() {
                event.veto();
            }
        }.run();
    }

    @Test
    @SpecAssertions({@SpecAssertion(section = PB, id = "o")})
    public void testProcessBeanEventFails() {
        final ProcessBean<SimpleBean> event = extension.getProcessBean();
        new Invocation() {
            void execute() {
                event.getAnnotated();
            }
        }.run();

        new Invocation() {
            void execute() {
                event.addDefinitionError(new NullPointerException());
            }
        }.run();

        new Invocation() {
            void execute() {
                event.getBean();
            }
        }.run();

    }

    @Test
    @SpecAssertions({@SpecAssertion(section = PB, id = "o")})
    public void testProcessManagedBeanEventFails() {
        final ProcessManagedBean<SimpleBean> event = extension.getProcessManagedBean();
        new Invocation() {
            void execute() {
                event.getAnnotated();
            }
        }.run();

        new Invocation() {
            void execute() {
                event.getAnnotatedBeanClass();
            }
        }.run();

        new Invocation() {
            void execute() {
                event.addDefinitionError(new NullPointerException());
            }
        }.run();

        new Invocation() {
            void execute() {
                event.getBean();
            }
        }.run();

    }

    @Test
    @SpecAssertions({@SpecAssertion(section = PB, id = "o")})
    public void testProcessSessionBeanEventFails() {
        final ProcessSessionBean<SessionBean> event = extension.getProcessSessionBean();
        new Invocation() {
            void execute() {
                event.getAnnotated();
            }
        }.run();

        new Invocation() {
            void execute() {
                event.getAnnotatedBeanClass();
            }
        }.run();

        new Invocation() {
            void execute() {
                event.getEjbName();
            }
        }.run();

        new Invocation() {
            void execute() {
                event.getSessionBeanType();
            }
        }.run();

        new Invocation() {
            void execute() {
                event.addDefinitionError(new NullPointerException());
            }
        }.run();

        new Invocation() {
            void execute() {
                event.getBean();
            }
        }.run();

    }

    @Test
    @SpecAssertions({@SpecAssertion(section = PB, id = "o")})
    public void testProcessProducerMethodEventFails() {
        final ProcessProducerMethod<Integer, SimpleBean> event = extension.getProcessProducerMethod();
        new Invocation() {
            void execute() {
                event.getAnnotated();
            }
        }.run();

        new Invocation() {
            void execute() {
                event.getAnnotatedProducerMethod();
            }
        }.run();

        new Invocation() {
            void execute() {
                event.getAnnotatedDisposedParameter();
            }
        }.run();

        new Invocation() {
            void execute() {
                event.addDefinitionError(new NullPointerException());
            }
        }.run();

        new Invocation() {
            void execute() {
                event.getBean();
            }
        }.run();

    }

    @Test
    @SpecAssertions({@SpecAssertion(section = PB, id = "o")})
    public void testProcessProducerFieldEventFails() {
        final ProcessProducerField<Integer, SimpleBean> event = extension.getProcessProducerField();
        new Invocation() {
            void execute() {
                event.getAnnotated();
            }
        }.run();

        new Invocation() {
            void execute() {
                event.getAnnotatedProducerField();
            }
        }.run();

        new Invocation() {
            void execute() {
                event.getAnnotatedDisposedParameter();
            }
        }.run();

        new Invocation() {
            void execute() {
                event.addDefinitionError(new NullPointerException());
            }
        }.run();

        new Invocation() {
            void execute() {
                event.getBean();
            }
        }.run();

    }

    @Test
    @SpecAssertions({@SpecAssertion(section = PBA, id = "f")})
    public void testProcessBeanAttributesEventFails() {
        final ProcessBeanAttributes<SimpleBean> event = extension.getProcessBeanAttributes();
        AnnotatedType<SimpleBean> type = beanManager.createAnnotatedType(SimpleBean.class);
        final BeanAttributes<SimpleBean> attributes =  beanManager.createBeanAttributes(type);
        new Invocation() {
            void execute() {
                event.addDefinitionError(new NullPointerException());
            }
        }.run();
        new Invocation() {
            void execute() {
                event.getAnnotated();
            }
        }.run();
        new Invocation() {
            void execute() {
                event.setBeanAttributes(attributes);
            }
        }.run();
        new Invocation() {
            void execute() {
                event.getBeanAttributes();
            }
        }.run();
        new Invocation() {
            void execute() {
                event.veto();
            }
        }.run();
    }

    @Test
    @SpecAssertions({@SpecAssertion(section = POM, id = "dc")})
    public void testProcessObserverMethodEventFails() {
        final ProcessObserverMethod<SimpleBean, ?> event = extension.getProcessObserverMethod();
        new Invocation() {
            void execute() {
                event.addDefinitionError(new NullPointerException());
            }
        }.run();
        new Invocation() {
            void execute() {
                event.getAnnotatedMethod();
            }
        }.run();
        new Invocation() {
            void execute() {
                event.getObserverMethod();
            }
        }.run();
    }

    @Test
    @SpecAssertions({@SpecAssertion(section = PIT, id = "g")})
    public void testProcessInjectionTargetEventFails() {
        final ProcessInjectionTarget<SimpleBean> event = extension.getProcessInjectionTarget();
        AnnotatedType<?> type = beanManager.createAnnotatedType(SimpleBean.class);
        final InjectionTarget target = beanManager.createInjectionTarget(type);

        new Invocation() {
            void execute() {
                event.addDefinitionError(new NullPointerException());
            }
        }.run();
        new Invocation() {
            void execute() {
                event.getAnnotatedType();
            }
        }.run();
        new Invocation() {
            void execute() {
                event.getInjectionTarget();
            }
        }.run();
        new Invocation() {
            void execute() {
                event.setInjectionTarget(target);
            }
        }.run();
    }

    @Test
    @SpecAssertions({@SpecAssertion(section = PIP, id = "e")})
    public void testProcessInjectionPointEventFails() {
        final ProcessInjectionPoint<SimpleBean, ?> event = extension.getProcessInjectionPoint();
        AnnotatedType<?> type = beanManager.createAnnotatedType(SimpleBean.class);
        AnnotatedField<?> field = type.getFields().iterator().next();
        final InjectionPoint injectionPoint = beanManager.createInjectionPoint(field);

        new Invocation() {
            void execute() {
                event.addDefinitionError(new NullPointerException());
            }
        }.run();
        new Invocation() {
            void execute() {
                event.getInjectionPoint();
            }
        }.run();
        new Invocation() {
            void execute() {
                event.setInjectionPoint(injectionPoint);
            }
        }.run();
    }

    @Test
    @SpecAssertions({@SpecAssertion(section = PP, id = "i")})
    public void testProcessProducer() {
        final ProcessProducer<SimpleBean, Integer> event = extension.getProcessProducer();
        new Invocation() {
            void execute() {
                event.addDefinitionError(new NullPointerException());
            }
        }.run();
        new Invocation() {
            void execute() {
                event.getProducer();
            }
        }.run();
        new Invocation() {
            void execute() {
                event.getAnnotatedMember();
            }
        }.run();
        new Invocation() {
            void execute() {
                event.setProducer(new Producer<Integer>() {
                    @Override
                    public Integer produce(CreationalContext<Integer> ctx) {
                        return new Integer(0);
                    }

                    @Override
                    public void dispose(Integer instance) {
                    }

                    @Override
                    public Set<InjectionPoint> getInjectionPoints() {
                        return Collections.emptySet();
                    }
                });
            }
        }.run();
    }

    private static abstract class Invocation {
        void run() {
            try {
                execute();
                fail("Expected exception not thrown");
            } catch (IllegalStateException expected) {
            }
        }

        abstract void execute();
    }


}
