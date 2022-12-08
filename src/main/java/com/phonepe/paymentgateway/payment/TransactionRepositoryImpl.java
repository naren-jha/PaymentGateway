package com.phonepe.paymentgateway.payment;

import com.phonepe.paymentgateway.client.ClientBankAccount;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {
    private Map<Long, Transaction> transactions = new HashMap<>();

    @Override
    public Transaction saveTransaction(PaymentIssuingAccount issuingAccount, ClientBankAccount acquiringAccount, Double amount) {
        Long transactionId = Long.valueOf(transactions.size());
        Transaction transaction = Transaction
                .builder()
                .id(transactionId)
                .issuingAccount(issuingAccount)
                .acquiringAccount(acquiringAccount)
                .amount(amount)
                .createdAt(LocalDate.now())
                .build();

        transactions.put(transactionId, transaction);
        return transaction;
    }
}
