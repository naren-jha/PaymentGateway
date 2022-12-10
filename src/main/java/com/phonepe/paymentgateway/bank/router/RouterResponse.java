package com.phonepe.paymentgateway.bank.router;

import com.phonepe.paymentgateway.bank.BankService;
import com.phonepe.paymentgateway.client.ClientBankAccount;
import lombok.Data;

@Data
public class RouterResponse {
    private ClientBankAccount selectedAccount;
    private BankService bankService;
}
