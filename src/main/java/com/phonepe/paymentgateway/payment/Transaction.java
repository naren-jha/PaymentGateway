package com.phonepe.paymentgateway.payment;

import com.phonepe.paymentgateway.client.ClientBankAccount;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Transaction {
    private Long id;
    private PaymentIssuingAccount issuingAccount;
    private ClientBankAccount acquiringAccount;
    private Double amount;
    private String gatewayReferenceNumber;
    private LocalDate createdAt;
}
