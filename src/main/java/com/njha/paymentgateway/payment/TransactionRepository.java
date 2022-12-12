package com.njha.paymentgateway.payment;

import com.njha.paymentgateway.bank.BankType;

import java.util.Map;

public interface TransactionRepository {
    Transaction saveTransaction(Transaction transaction);
    Map<BankType, int[]> getSuccessAndFailureCounts();
}
