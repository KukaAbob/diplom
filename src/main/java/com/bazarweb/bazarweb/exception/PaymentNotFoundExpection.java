package com.bazarweb.bazarweb.exception;

public class PaymentNotFoundExpection extends RuntimeException{
    public PaymentNotFoundExpection(String message) {
        super(message);
    }
}
