package com.phonepe.paymentgateway.bank.strategy;

import com.phonepe.paymentgateway.bank.BankService;
import com.phonepe.paymentgateway.client.ClientBankAccount;
import lombok.Builder;
import lombok.Data;

@Data
public class BankSelectionResponse {
    private ClientBankAccount selectedAccount;
    private BankService bankService;
}
