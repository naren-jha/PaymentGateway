package com.phonepe.paymentgateway.bank.router;

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
public class PayModeRouterStrategy implements RouterStrategy {

    @Autowired
    private Map<BankType, BankService> bankTypeToBankServiceMap;

    @Autowired
    private Map<Mode, BankType> modeToBankTypeMap;

    @Override
    public RouterStrategyResponse selectBank(Mode mode, List<ClientBankAccount> bankAccounts) {
        log.info("Applying payment-mode based router strategy");
        Map<BankType, ClientBankAccount> bankTypeToAccountMap = new HashMap<>();
        bankAccounts.forEach(acc -> bankTypeToAccountMap.put(acc.getBank().getType(), acc));

        RouterStrategyResponse bankSelectionResponse = new RouterStrategyResponse();
        BankType selectedBankType = modeToBankTypeMap.get(mode);
        if (Objects.isNull(selectedBankType)) {
            log.info("No config provided for mode based selection for payment mode {}. Choosing default bank SBI", mode);
            selectedBankType = BankType.SBI; // default bank
        }

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

        log.info("Mode based router has selected {} bank", selectedBankType);
        return bankSelectionResponse;
    }

    @Override
    public RouterStrategyType strategyType() {
        return RouterStrategyType.MODE_BASED;
    }
}
