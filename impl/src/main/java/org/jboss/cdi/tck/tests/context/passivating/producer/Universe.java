package org.jboss.cdi.tck.tests.context.passivating.producer;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

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
