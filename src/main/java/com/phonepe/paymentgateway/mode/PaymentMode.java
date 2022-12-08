package com.phonepe.paymentgateway.mode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentMode {
    private Long id;
    private Mode name;
}


