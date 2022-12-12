package com.njha.paymentgateway.payment;

import com.njha.paymentgateway.bank.router.RouterStrategyType;
import com.njha.paymentgateway.client.ClientBankAccount;
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
    private Boolean status;
    private String gatewayReferenceNumber;
    private LocalDate createdAt;
    private RouterStrategyType routerType;
}
