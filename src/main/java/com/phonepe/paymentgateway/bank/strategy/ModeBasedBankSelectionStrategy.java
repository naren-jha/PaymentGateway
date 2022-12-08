package com.phonepe.paymentgateway.bank.strategy;

import com.phonepe.paymentgateway.BankType;
import com.phonepe.paymentgateway.bank.BankService;
import com.phonepe.paymentgateway.bank.HDFCBankService;
import com.phonepe.paymentgateway.bank.ICICIBankService;
import com.phonepe.paymentgateway.bank.SBIBankService;
import com.phonepe.paymentgateway.client.ClientBankAccount;
import com.phonepe.paymentgateway.mode.Mode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        Map<BankType, ClientBankAccount> bankTypeToAccountMap = new HashMap<>();
        bankAccounts.forEach(acc -> bankTypeToAccountMap.put(acc.getBank().getType(), acc));

        BankSelectionResponse bankSelectionResponse = new BankSelectionResponse();
        BankType selectedBankType = modeToBankTypeMap.get(mode);
        if (Objects.isNull(selectedBankType)) selectedBankType = BankType.SBI; // default bank

        // set acquiring bank account of client
        ClientBankAccount selectedClientAcc = bankTypeToAccountMap.get(selectedBankType);
        if (Objects.isNull(selectedClientAcc)) {
            // when client doesn't have account of target type
            selectedClientAcc = bankAccounts.get(0);
            selectedBankType = selectedClientAcc.getBank().getType();
        }
        bankSelectionResponse.setSelectedAccount(selectedClientAcc);

        // set corresponding bank service
        BankService bankService = bankTypeToBankServiceMap.get(selectedBankType);
        bankSelectionResponse.setBankService(bankService);

        return bankSelectionResponse;
    }

    @Override
    public BankSelectionStrategyType strategyType() {
        return BankSelectionStrategyType.MODE_BASED;
    }
}

@Configuration
class ModeToBankConfig {

    @Bean
    public Map<Mode, BankType> modeToBankTypeMap() {
        Map<Mode, BankType> modeToBankTypeMap = new EnumMap<>(Mode.class);
        modeToBankTypeMap.put(Mode.NET_BANKING, BankType.ICICI);
        modeToBankTypeMap.put(Mode.CREDIT_CARD, BankType.HDFC);
        return modeToBankTypeMap;
    }

    @Bean
    public Map<BankType, BankService> bankTypeToBankServiceMap() {
        Map<BankType, BankService> modeToBankServiceMap = new EnumMap<>(BankType.class);
        modeToBankServiceMap.put(BankType.ICICI, new ICICIBankService());
        modeToBankServiceMap.put(BankType.HDFC, new HDFCBankService());
        modeToBankServiceMap.put(BankType.SBI, new SBIBankService());
        return modeToBankServiceMap;
    }
}
