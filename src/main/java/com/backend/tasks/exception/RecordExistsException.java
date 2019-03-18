package com.backend.tasks.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Sarvar Nishonboyev on Mar 17, 2019
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class RecordExistsException extends Exception {

    public RecordExistsException() {
        super();
    }

    public RecordExistsException(String s) {
        super(s);
    }
}
