package com.jabguru.axondemo.exceptions;

import org.axonframework.messaging.annotation.MessageHandlerInvocationException;

public class UnconfirmedOrderException extends MessageHandlerInvocationException {
    public UnconfirmedOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
