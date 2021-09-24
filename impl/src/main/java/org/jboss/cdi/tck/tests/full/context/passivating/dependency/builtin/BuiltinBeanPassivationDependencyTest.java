package org.jboss.cdi.tck.tests.full.context.passivating.dependency.builtin;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE_DEPENDENCY;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author Martin Kouba
 * @author Kirill Gaevskii
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class BuiltinBeanPassivationDependencyTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BuiltinBeanPassivationDependencyTest.class).build();
    }

    @Inject
    Worker worker;

    @Inject
    Boss boss;
    
    @Inject
    InspectorAssistant inspectorAssist;


    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY, id = "ea")
    public void testInstance() throws IOException, ClassNotFoundException {
        assertNotNull(worker);
        assertNotNull(worker.getInstance());
        Hammer hammer = worker.getInstance().get();
        assertNotNull(hammer);

        String workerId = worker.getId();
        String hammerId = hammer.getId();

        byte[] serializedWorker = passivate(worker);
        Worker workerCopy = (Worker) activate(serializedWorker);

        assertNotNull(workerCopy);
        assertNotNull(workerCopy.getInstance());
        assertEquals(workerCopy.getId(), workerId);
        assertEquals(workerCopy.getInstance().get().getId(), hammerId);
    }

    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY, id = "ed")
    public void testBeanManager() throws IOException, ClassNotFoundException {
        assertNotNull(boss);
        assertNotNull(boss.getBeanManager());

        String bossId = boss.getId();

        byte[] serializedBoss = passivate(boss);
        Boss bossCopy = (Boss) activate(serializedBoss);

        assertNotNull(bossCopy);
        assertNotNull(bossCopy.getBeanManager());
        assertEquals(bossCopy.getId(), bossId);
        assertEquals(bossCopy.getBeanManager().getBeans(Boss.class).size(), 1);
    }
    
    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY, id = "ec")
    public void testInjectionPoint() throws IOException, ClassNotFoundException {

        Inspector inspector = inspectorAssist.getInspector();
        assertNotNull(inspector);
        assertNotNull(inspector.getInjectionPoint());
        String inspectorId = inspector.getId();

        byte[] serializedInspector = passivate(inspector);
        Inspector inspectorCopy = (Inspector) activate(serializedInspector);
        
        assertNotNull(inspectorCopy);
        assertNotNull(inspectorCopy.getInjectionPoint());
        assertEquals(inspectorCopy.getId(), inspectorId);
        assertEquals(inspectorCopy.getInjectionPoint().getType(), inspector.getInjectionPoint().getType());
        assertEquals(inspectorCopy.getInjectionPoint().getQualifiers(), inspector.getInjectionPoint().getQualifiers());
        assertEquals(inspectorCopy.getInjectionPoint().getBean(), inspector.getInjectionPoint().getBean());
        assertEquals(inspectorCopy.getInjectionPoint().getMember(), inspector.getInjectionPoint().getMember());
        assertEquals(inspectorCopy.getInjectionPoint().getAnnotated(), inspector.getInjectionPoint().getAnnotated());
        assertEquals(inspectorCopy.getInjectionPoint().isDelegate(), inspector.getInjectionPoint().isDelegate());
        assertEquals(inspectorCopy.getInjectionPoint().isTransient(), inspector.getInjectionPoint().isTransient());
    }

}
