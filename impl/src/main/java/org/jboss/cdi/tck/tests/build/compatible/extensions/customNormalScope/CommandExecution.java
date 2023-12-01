package org.jboss.cdi.tck.tests.build.compatible.extensions.customNormalScope;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommandExecution {
    private final Date startedAt;

    private final Map<String, Object> data;

    CommandExecution() {
        this.startedAt = new Date();
        this.data = new HashMap<>();
    }

    Date getStartedAt() {
        return startedAt;
    }

    Map<String, Object> getData() {
        return data;
    }
}
