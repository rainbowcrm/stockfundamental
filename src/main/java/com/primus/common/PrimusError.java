package com.primus.common;

public class PrimusError extends Exception {
    int errorCode;
    public PrimusError(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode ;
    }


}
