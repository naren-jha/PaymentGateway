package com.njha.paymentgateway.bank.router;

import com.njha.paymentgateway.client.ClientBankAccount;
import com.njha.paymentgateway.mode.Mode;

import java.util.List;

public interface RouterStrategy {

    RouterStrategyResponse selectBank(Mode mode, List<ClientBankAccount> bankAccounts);
    RouterStrategyType strategyType();
}
