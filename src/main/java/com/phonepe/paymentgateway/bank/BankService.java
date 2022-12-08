package com.phonepe.paymentgateway.bank;

import com.phonepe.paymentgateway.client.ClientBankAccount;
import com.phonepe.paymentgateway.payment.PaymentIssuingAccount;

public interface BankService {
    PaymentBankResponse makePayment(PaymentIssuingAccount issuingAccount, ClientBankAccount acquiringAccount, double amount);
}
