package com.phonepe.paymentgateway.payment;

import com.phonepe.paymentgateway.client.ClientBankAccount;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Transaction {
    private ClientBankAccount issuingAccount;
    private ClientBankAccount acquiringAccount;
    private Double amount;
    private LocalDateTime timestamp;
}
