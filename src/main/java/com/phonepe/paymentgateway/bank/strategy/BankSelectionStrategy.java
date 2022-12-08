package com.phonepe.paymentgateway.bank.strategy;

import com.phonepe.paymentgateway.client.ClientBankAccount;
import com.phonepe.paymentgateway.mode.Mode;

import java.util.List;

public interface BankSelectionStrategy {

    BankSelectionResponse selectBank(Mode mode, List<ClientBankAccount> bankAccounts);
    BankSelectionStrategyType strategyType();
}
