package com.njha.paymentgateway.bank.router;

import com.njha.paymentgateway.bank.BankService;
import com.njha.paymentgateway.client.ClientBankAccount;
import lombok.Data;

@Data
public class RouterStrategyResponse {
    private ClientBankAccount selectedAccount;
    private BankService bankService;
}
