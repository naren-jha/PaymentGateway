package com.phonepe.paymentgateway.bank;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentBankResponse {
    private boolean status;
    private String bankReferenceNumber;
}
