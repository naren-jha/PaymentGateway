package com.phonepe.paymentgateway.payment;

import com.phonepe.paymentgateway.mode.Mode;

public interface PaymentService {
    void makePayment(Mode mode, PaymentIssuingAccount issuingEntity, double amount, Long clientId);
}
