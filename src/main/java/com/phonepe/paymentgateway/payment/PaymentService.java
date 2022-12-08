package com.phonepe.paymentgateway.payment;

import com.phonepe.paymentgateway.mode.Mode;

import java.util.List;

public interface PaymentService {
    List<String> showDistribution();
    Transaction makePayment(Mode mode, PaymentIssuingAccount issuingEntity, double amount, Long clientId);
}
