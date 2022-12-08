package com.phonepe.paymentgateway.bank.strategy;

import com.phonepe.paymentgateway.bank.BankType;
import com.phonepe.paymentgateway.bank.BankService;
import com.phonepe.paymentgateway.client.ClientBankAccount;
import com.phonepe.paymentgateway.exception.InvalidPaymentConfigurationException;
import com.phonepe.paymentgateway.mode.Mode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@Data
public class PercentageBasedBankSelectionStrategy implements BankSelectionStrategy {

    @Autowired
    private Map<BankType, Integer> bankToPercentageMap;

    @Autowired
    private Map<BankType, BankService> bankTypeToBankServiceMap;

    @Override
    public BankSelectionResponse selectBank(Mode mode, List<ClientBankAccount> bankAccounts) {
        log.info("Applying percentage based bank selection strategy");
        Map<BankType, ClientBankAccount> bankTypeToAccountMap = new HashMap<>();
        bankAccounts.forEach(acc -> bankTypeToAccountMap.put(acc.getBank().getType(), acc));

        Map<BankType, int[]> bankToRangeMap = new HashMap<>();
        int min = 0;
        for (BankType bankType : bankToPercentageMap.keySet()) {
            int max = min + bankToPercentageMap.get(bankType);
            if (max > 100) {
                log.error("invalid bank to percentage distribution config {}", bankToPercentageMap);
                throw new InvalidPaymentConfigurationException("invalid bank to percentage distribution config");
            }

            int[] range = new int[]{min / 10, max / 10};
            bankToRangeMap.put(bankType, range);
            min = max;
        }
        // bankToRangeMap = {ICICI: 4, HDFC: 7, SBI: 10}

        BankType selectedBankType = BankType.HDFC;
        int randomNumber = ThreadLocalRandom.current().nextInt(0, 10);
        for (BankType bankType : bankToRangeMap.keySet()) {
            int[] range = bankToRangeMap.get(bankType);
            if (randomNumber >= range[0] && randomNumber < range[1]) {
                selectedBankType = bankType;
            }
        }

        BankSelectionResponse bankSelectionResponse = new BankSelectionResponse();
        // set acquiring bank account of client
        ClientBankAccount selectedClientAcc = bankTypeToAccountMap.get(selectedBankType);
        if (Objects.isNull(selectedClientAcc)) {
            // when client doesn't have account of selected type
            selectedClientAcc = bankAccounts.get(0);
            selectedBankType = selectedClientAcc.getBank().getType();
        }
        bankSelectionResponse.setSelectedAccount(selectedClientAcc);

        // set corresponding bank service
        BankService bankService = bankTypeToBankServiceMap.get(selectedBankType);
        bankSelectionResponse.setBankService(bankService);

        log.info("Percentage based distribution has selected {} bank", selectedBankType);
        return bankSelectionResponse;
    }

    @Override
    public BankSelectionStrategyType strategyType() {
        return BankSelectionStrategyType.PERCENTAGE;
    }
}