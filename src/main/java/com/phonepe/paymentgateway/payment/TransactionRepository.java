package com.phonepe.paymentgateway.payment;

import com.phonepe.paymentgateway.bank.BankType;
import com.phonepe.paymentgateway.bank.PaymentBankResponse;
import com.phonepe.paymentgateway.bank.router.RouterStrategyType;
import com.phonepe.paymentgateway.client.ClientBankAccount;

import java.util.Map;

public interface TransactionRepository {
    Transaction saveTransaction(PaymentBankResponse paymentBankResponse, RouterStrategyType routerType, PaymentIssuingAccount issuingAccount, ClientBankAccount acquiringAccount, Double amount);
    Map<BankType, int[]> getSuccessAndFailureCounts();
}
