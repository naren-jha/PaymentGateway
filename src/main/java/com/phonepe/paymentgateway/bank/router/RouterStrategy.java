package com.phonepe.paymentgateway.bank.router;

import com.phonepe.paymentgateway.client.ClientBankAccount;
import com.phonepe.paymentgateway.mode.Mode;

import java.util.List;

public interface RouterStrategy {

    RouterResponse selectBank(Mode mode, List<ClientBankAccount> bankAccounts);
    RouterStrategyType strategyType();
}
