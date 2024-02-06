package com.baozi.consul.exception;

import java.io.Serial;

public class ConsulClientException extends Exception {
    @Serial
    private static final long serialVersionUID = 1842078286342810124L;

    public ConsulClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
