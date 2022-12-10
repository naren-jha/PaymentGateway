package com.phonepe.paymentgateway.exception;

public class PayModeNotSupportedException extends RuntimeException {
    public PayModeNotSupportedException(String message) {
        super(message);
    }
}
