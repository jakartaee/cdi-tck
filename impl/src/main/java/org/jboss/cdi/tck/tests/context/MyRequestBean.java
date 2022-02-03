package org.jboss.cdi.tck.tests.context;

import jakarta.enterprise.context.RequestScoped;

import java.io.Serializable;

@RequestScoped
public class MyRequestBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id = 0;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void ping() {
    }
}
