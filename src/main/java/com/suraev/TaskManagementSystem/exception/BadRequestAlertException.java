package com.suraev.TaskManagementSystem.exception;

import lombok.Getter;
import lombok.Setter;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

@Getter
@Setter
public class BadRequestAlertException  extends AbstractThrowableProblem {

    public BadRequestAlertException(String defaultMessage, Status status) {
        this(ErrorConstants.DEFAULT_TYPE,defaultMessage, status);

    }

    public BadRequestAlertException(URI type,String defaultMessage, Status status) {
        super(type, defaultMessage, status, null, null, null);
    }

}
