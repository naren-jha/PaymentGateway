package com.phonepe.paymentgateway.payment;

import com.phonepe.paymentgateway.client.ClientBankAccount;

public interface TransactionRepository {
    Transaction saveTransaction(PaymentIssuingAccount issuingAccount, ClientBankAccount acquiringAccount, Double amount);
}
