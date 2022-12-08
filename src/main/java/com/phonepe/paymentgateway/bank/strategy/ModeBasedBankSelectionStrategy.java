package com.phonepe.paymentgateway.bank.strategy;

import com.phonepe.paymentgateway.bank.BankType;
import com.phonepe.paymentgateway.bank.BankService;
import com.phonepe.paymentgateway.client.ClientBankAccount;
import com.phonepe.paymentgateway.mode.Mode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@Data
public class ModeBasedBankSelectionStrategy implements BankSelectionStrategy {

    @Autowired
    private Map<BankType, BankService> bankTypeToBankServiceMap;

    @Autowired
    private Map<Mode, BankType> modeToBankTypeMap;

    @Override
    public BankSelectionResponse selectBank(Mode mode, List<ClientBankAccount> bankAccounts) {
        log.info("Applying mode based bank selection strategy");
        Map<BankType, ClientBankAccount> bankTypeToAccountMap = new HashMap<>();
        bankAccounts.forEach(acc -> bankTypeToAccountMap.put(acc.getBank().getType(), acc));

        BankSelectionResponse bankSelectionResponse = new BankSelectionResponse();
        BankType selectedBankType = modeToBankTypeMap.get(mode);
        if (Objects.isNull(selectedBankType)) selectedBankType = BankType.SBI; // default bank

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

        log.info("Mode based distribution has selected {} bank", selectedBankType);
        return bankSelectionResponse;
    }

    @Override
    public BankSelectionStrategyType strategyType() {
        return BankSelectionStrategyType.MODE_BASED;
    }
}
