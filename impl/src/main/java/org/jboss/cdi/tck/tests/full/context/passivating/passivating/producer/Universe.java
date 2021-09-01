package org.jboss.cdi.tck.tests.full.context.passivating.passivating.producer;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;

@SuppressWarnings("serial")
@SessionScoped
public class Universe implements Serializable {

    @Inject
    @AnswerToTheUltimateQuestion
    private int answer;

    public int getAnswer() {
        return answer;
    }

}
