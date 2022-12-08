package com.phonepe.paymentgateway.bank.strategy;

import com.phonepe.paymentgateway.bank.HDFCBankService;
import com.phonepe.paymentgateway.bank.ICICIBankService;
import com.phonepe.paymentgateway.client.ClientBankAccount;
import com.phonepe.paymentgateway.mode.Mode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Data
public class PercentageBasedBankSelectionStrategy implements BankSelectionStrategy {

    @Override
    public BankSelectionResponse selectBank(Mode mode, List<ClientBankAccount> bankAccounts) {
        BankSelectionResponse bankSelectionResponse = new BankSelectionResponse();
        bankSelectionResponse.setBankService(new HDFCBankService());
        bankSelectionResponse.setSelectedAccount(bankAccounts.get(0));
        return bankSelectionResponse;
    }

    @Override
    public BankSelectionStrategyType strategyType() {
        return BankSelectionStrategyType.PERCENTAGE;
    }
}
