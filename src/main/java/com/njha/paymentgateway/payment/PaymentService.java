package com.njha.paymentgateway.payment;

import com.njha.paymentgateway.bank.router.RouterStrategyType;
import com.njha.paymentgateway.mode.Mode;

import java.util.List;

public interface PaymentService {
    List<String> showDistribution();
    Transaction makePayment(Mode mode, RouterStrategyType routerStrategy, PaymentIssuingAccount issuingEntity, double amount, Long clientId);
}
