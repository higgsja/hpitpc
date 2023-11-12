package com.hpi.tpc.ui.exceptions;

public class AccessDeniedException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 4686002741677531757L;

    public AccessDeniedException() {
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
