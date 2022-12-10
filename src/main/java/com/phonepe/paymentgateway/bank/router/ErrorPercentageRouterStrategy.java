package com.phonepe.paymentgateway.bank.router;

import com.phonepe.paymentgateway.bank.BankService;
import com.phonepe.paymentgateway.bank.BankType;
import com.phonepe.paymentgateway.client.ClientBankAccount;
import com.phonepe.paymentgateway.exception.InvalidPaymentConfigurationException;
import com.phonepe.paymentgateway.mode.Mode;
import com.phonepe.paymentgateway.payment.TransactionRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@Data
public class ErrorPercentageRouterStrategy implements RouterStrategy {

    @Autowired
    private Map<BankType, BankService> bankTypeToBankServiceMap;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public RouterStrategyResponse selectBank(Mode mode, List<ClientBankAccount> bankAccounts) {
        log.info("Applying error percentage based router strategy");
        Map<BankType, Double> bankErrorPercentageMap = getBankErrorPercentage();
        Collections.sort(bankAccounts, (a, b) -> {
            double aError = bankErrorPercentageMap.getOrDefault(a.getBank().getType(), 0.0);
            double bError = bankErrorPercentageMap.getOrDefault(b.getBank().getType(), 0.0);
            return Double.compare(aError, bError);
        });

        // select bank with the lowest error percentage
        ClientBankAccount selectedClientAcc = bankAccounts.get(0);
        BankType selectedBankType = selectedClientAcc.getBank().getType();

        RouterStrategyResponse bankSelectionResponse = new RouterStrategyResponse();
        // set acquiring bank account of client
        bankSelectionResponse.setSelectedAccount(selectedClientAcc);

        // set corresponding bank service
        BankService bankService = bankTypeToBankServiceMap.get(selectedBankType);
        bankSelectionResponse.setBankService(bankService);

        log.info("Error percentage based router has selected {} bank", selectedBankType);
        return bankSelectionResponse;
    }

    public Map<BankType, Double> getBankErrorPercentage() {
        Map<BankType, int[]> successAndFailureCounts = transactionRepository.getSuccessAndFailureCounts();
        Map<BankType, Double> errorPercentageMap = new EnumMap<>(BankType.class);
        for (BankType bankType : successAndFailureCounts.keySet()) {
            int[] successAndFailure = successAndFailureCounts.get(bankType);
            double errorPercentage = 0;
            if (successAndFailure[1] != 0) {
                errorPercentage = ((successAndFailure[0] + successAndFailure[1]) * 100.0) / successAndFailure[1];
            }
            errorPercentageMap.put(bankType, errorPercentage);
        }
        return errorPercentageMap;
    }

    @Override
    public RouterStrategyType strategyType() {
        return RouterStrategyType.ERROR_PERCENTAGE;
    }
}