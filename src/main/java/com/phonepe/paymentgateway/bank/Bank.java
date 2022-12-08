package com.phonepe.paymentgateway.bank;

import com.phonepe.paymentgateway.BankType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class Bank {
    private Long id;
    private String name;
    private BankType type;
}
