package com.phonepe.paymentgateway.payment;

import com.phonepe.paymentgateway.bank.BankType;
import com.phonepe.paymentgateway.bank.PaymentBankResponse;
import com.phonepe.paymentgateway.bank.router.RouterStrategyType;
import com.phonepe.paymentgateway.client.ClientBankAccount;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {
    private Map<Long, Transaction> transactions = new HashMap<>();

    @Override
    public Transaction saveTransaction(PaymentBankResponse paymentBankResponse, RouterStrategyType routerType, PaymentIssuingAccount issuingAccount, ClientBankAccount acquiringAccount, Double amount) {
        Long transactionId = Long.valueOf(transactions.size());
        Transaction transaction = Transaction
                .builder()
                .id(transactionId)
                .issuingAccount(issuingAccount)
                .acquiringAccount(acquiringAccount)
                .amount(amount)
                .status(paymentBankResponse.isStatus())
                .createdAt(LocalDate.now())
                .routerType(routerType)
                .build();

        transactions.put(transactionId, transaction);
        return transaction;
    }

    @Override
    public Map<BankType, int[]> getSuccessAndFailureCounts() {
        Map<BankType, int[]> successAndFailedCountMap = new EnumMap<>(BankType.class);
        transactions.values().forEach(trn -> {
            BankType bankType = trn.getAcquiringAccount().getBank().getType();
            successAndFailedCountMap.putIfAbsent(bankType, new int[]{0, 0});
            int[] counts = successAndFailedCountMap.get(bankType);
            if (trn.getStatus()) counts[0]++;
            else counts[1]++;
        });
        return successAndFailedCountMap;
    }
}
