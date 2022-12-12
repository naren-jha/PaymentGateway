package com.njha.paymentgateway.bank;

import com.njha.paymentgateway.client.ClientBankAccount;
import com.njha.paymentgateway.payment.PaymentIssuingAccount;

public interface BankService {
    PaymentBankResponse makePayment(PaymentIssuingAccount issuingAccount, ClientBankAccount acquiringAccount, double amount);
}
